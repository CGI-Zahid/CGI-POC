'use strict';

cgiWebApp.controller('eventController',
  ['$scope', '$sessionStorage', '$state',
  function ($scope, $sessionStorage, $state) {

  $scope.notification = {
    messageCharsRemaining: 135
  };

  $scope.publishEvent = function() {
    $sessionStorage.put('state', $state.current.name);
  };
}]);
