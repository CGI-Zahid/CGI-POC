'use strict';

cgiWebApp.controller('eventController',
  ['$scope', '$sessionStorage', '$state',
  function ($scope, $sessionStorage, $state) {

  $scope.publishEvent = function() {
    $sessionStorage.put('state', $state.current.name);
  }
}]);
