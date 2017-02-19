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
      $scope.sourceEvent1 = {type: 'Fire', geometry: { 'x': -10677457.159137897, 'y': 4106537.9944933983 }};
      $scope.sourceEvent2 = {type: 'Flood', geometry: { 'x':-79.910832999842967, 'y': 37.266943999256185 }};
      $scope.embeddedLine = 'www.google.com/maps/search/';
      $scope.embeddedLine = $scope.embeddedLine + $scope.sourceEvent1.geometry.y;
      $scope.embeddedLine = $scope.embeddedLine +', ';
      $scope.embeddedLine = $scope.embeddedLine + $scope.sourceEvent1.geometry.x;
      $scope.embeddedLine = $scope.embeddedLine +'&hl=es;z=14&output=embed';


    $scope.eventTypes = [
        { name: 'All', id: undefined},
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
                    $scope.model.notifications = response;
                    $scope.changeFilters();
            }
       }).catch(function(response) {
                    // omce implemented...this changes to report an error
                    $scope.fakeData();
                    $scope.changeFilters();
       });
       
        
        
    };
    
    $scope.loadEventDetails = function(){
                    $window.alert("LOAD Events");
            
    };
    $scope.loadMap = function(){
                    $window.alert("LOAD MAP TBD");

    };

    
    $scope.fakeData = function(){
      //http request api
      //then, error handle
       var todaysDate = new Date();
       var day30Date = new Date();
       var day60Date = new Date();
       var day90Date = new Date();
       day30Date.setDate(todaysDate.getDate() - 30);
       day60Date.setDate(todaysDate.getDate() - 60);
       day90Date.setDate(todaysDate.getDate() - 90);

        
      $scope.model.notifications.push({type: 'Weather', date: todaysDate, zipcodes: [], description: 'Pick up essentials and leave', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Fire', date: day30Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: day30Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Fire', date: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: todaysDate, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Weather', date: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', date: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
     

      
        
    };

    $scope.initLoad();

}]);
