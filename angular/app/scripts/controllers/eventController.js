'use strict';

cgiWebApp.controller('eventController',
  ['$scope', '$sessionStorage', '$state', 'EventNotificationService',
  function ($scope, $sessionStorage, $state, EventNotificationService) {

  $scope.init = function() {
    $scope.apiErrors = [];

    $scope.notification = {
      messageMaxLength: 135,
      notificationType: '',
      zipCodes: '',
      zipCodesSplit: [],
      message: ''
    };

    $scope.regexZipCodes = /^\d{5}(?:\s*,\s*\d{5})*$/;
  };

  $scope.generateZipCodes = function() {
    $scope.notification.zipCodesSplit = $scope.notification.zipCodes.match(/\d{5}/g);
  };

  $scope.processApiErrors = function(response) {
    $scope.apiErrors = [];
    if (response.data && response.data.errors) {
      for (var i = 0; i < response.data.errors.length; i++) {
        if (response.data.errors[i].message) {
          $scope.apiErrors.push(response.data.errors[i].message);
        }
      }
    } else if (response.status === 401 && response.data) {
      $scope.apiErrors.push(response.data);
    }
  };

  $scope.publishEvent = function() {
    $scope.generateZipCodes();

    var toPost = {
      notificationType: $scope.notification.notificationType,
      zipcodes: $scope.notification.zipCodesSplit,
      message: $scope.notification.message
    };

    //putting this on scope so I can test
    $scope.toSend = toPost;

    EventNotificationService.publish(toPost).then(function() {
      $state.go('landing');
    }).catch(function(response) {
      $scope.processApiErrors(response);
    });
  };

  $scope.init();
}]);
