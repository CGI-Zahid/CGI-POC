/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 *
 * Landing Controller.
 * Will be responsible for loading data for notifications and displaying a map component
 * This component will be re-used for admin functionality and end user
 * Header will have different options for an end user
 * Admin will be able to filter data and publish notifications
 */

'use strict';

cgiWebApp.controller('landingController',
  ['$scope','$filter','$timeout','EventNotificationService','$window',
  function ($scope,$filter,$timeout,EventNotificationService,$window ) {
  $scope.apiErrors = [];
 
    $scope.eventTypes = [
        { name: 'All', id: undefined},
        { name: 'Emergency', id: 'ADMIN_E'},
        { name: 'Non-Emervecy', id: 'ADMIN_I'},
        { name: 'Weather', id: 'Weather'},
        { name: 'Flood', id: 'Flood'},
        { name: 'Fire', id: 'Fire'}];
    $scope.eventTimeFrames = [
        { name: '30 Days', id: '1'},
        { name: '60 Days', id: '2'},
        { name: '90 Days', id: '3'},
        { name: '180 Days', id: '4'},
        { name: '1 year', id: '5'}];
    $scope.model = {
      notifications: []
    };
    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter='1';
    $scope.changeFilters = function(){
        $scope.model.filterredNotifications = angular.copy( $scope.model.notifications); 
        $scope.model.filterredNotifications  =  $filter('filter')($scope.model.filterredNotifications, {type: $scope.eventTypeFilter}, true);
        $scope.model.filterredNotifications  =   $filter('eventTime')([$scope.model.filterredNotifications, $scope.eventTimeFilter]);
    };

 
    $scope.initLoad = function(){
        EventNotificationService.notifications().then(function(response) {
            if (response.status === 200) {
                    $scope.model.notifications = response.data;
                    // need to conver date string into a proper date.
                    angular.forEach($scope.model.notifications,function(value){
                       value.generationDate = Date.parse(value.generationDate); 
                    });
                    $scope.changeFilters();
            }
       }).catch(function(response) {
                    // omce implemented...this changes to report an error
                    
       });
       
        
        
    };
    
    $scope.loadEventDetails = function(){
                    $window.alert('LOAD Events');
            
    };
    $scope.loadMap = function(){
                    $window.alert('LOAD MAP TBD');

    };

    $scope.initLoad();

}]);
