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
  ['$scope','$filter','$timeout','EventNotificationService' ,
  function ($scope,$filter,$timeout,EventNotificationService ) {
  $scope.apiErrors = [];
 $scope.currentSelectedEvent=null;
    $scope.eventTypes = [
        { name: 'All', id: undefined},
        { name: 'Emergency', id: 'ADMIN_E'},
        { name: 'Non-Emervecy', id: 'ADMIN_I'},
        { name: 'Weather', id: 'Weather'},
        { name: 'Flood', id: 'Flood'},
        { name: 'Fire', id: 'Fire'}];
    $scope.eventTimeFrames = [
        { name: '30 Days', id: 30},
        { name: '60 Days', id: 60},
        { name: '90 Days', id: 90},
        { name: '180 Days', id: 180},
        { name: '1 year', id: 365}];
    $scope.model = {
      notifications: []
    };
    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter=30;
    $scope.changeFilters = function(){
        $scope.model.filteredNotifications = angular.copy( $scope.model.notifications); 
        $scope.model.filteredNotifications  =  $filter('filter')($scope.model.filteredNotifications, {type: $scope.eventTypeFilter}, true);
        $scope.model.filteredNotifications  =   $filter('eventTime')([$scope.model.filteredNotifications, $scope.eventTimeFilter]);
    };

   $scope.processApiErrors = function(response) {
    $scope.apiErrors = [];
    if (response.data && response.data.errors) {
      for (var i = 0; i < response.data.errors.length; i++) {
        if (response.data.errors[i].message) {
          $scope.apiErrors.push(response.data.errors[i].message);
        }
      }
    }
   };
 
    $scope.initLoad = function(){
        EventNotificationService.allNotifications().then(function(response) {
                     $scope.model.notifications = response.data;
                    // need to conver date string into a proper date.
                    angular.forEach($scope.model.notifications,function(value){
                       value.generationDate = Date.parse(value.generationDate); 
                    });
                    $scope.changeFilters();
        }).catch(function(response) {
                    // omce implemented...this changes to report an error
                        $scope.processApiErrors(response);
  
       });
       
        
        
    };
    
    $scope.loadEventDetails = function(selectedEvent){
           $scope.currentSelectedEvent = selectedEvent;
           $scope.showMapOrDetails='DETAILS';
            
    };
    $scope.loadMap = function(selectedEvent){
           $scope.currentSelectedEvent = selectedEvent;
           $scope.showMapOrDetails='MAP';
           // TODO .. more to do here...

    };

    $scope.initLoad();

}]);
