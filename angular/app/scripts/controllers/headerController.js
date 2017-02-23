/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 */

'use strict';

cgiWebApp.controller('headerController',
  ['$scope', '$sessionStorage', '$state','$http',
  function ($scope, $sessionStorage, $state,$http) {

    $scope.isAdminUser = function(){
      return $sessionStorage.get('role') === 'ADMIN';
    };

    $scope.logout = function(){
        $sessionStorage.remove('role');
        $sessionStorage.remove('jwt');
        $http.defaults.headers.common.Authorization =  'Bearer ';
        $state.go('login');
    };
}]);
