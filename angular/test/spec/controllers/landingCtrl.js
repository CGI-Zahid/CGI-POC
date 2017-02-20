'use strict';

describe('landingController', function() {
  var landingController;
  var $scope;
  var notificationService;
  var $state;
  var $q;
  var deferred;

  beforeEach(module('cgi-web-app'));
    beforeEach(inject(function(_$rootScope_, _$controller_, _EventNotificationService_, _$state_, _$q_) {

    $q = _$q_;
    $scope = _$rootScope_.$new();
    notificationService = _EventNotificationService_;
    $state = _$state_;

    deferred = _$q_.defer();
    spyOn(notificationService, 'allNotifications').and.returnValue(deferred.promise);

    landingController = _$controller_('landingController', {
      $scope: $scope,
      ProfileService: notificationService
    });
  }));
  it('initializes the apiErrors', function() {
    expect($scope.apiErrors.length).toBe(0);
  });
  it('should filter by type and date', function() {
    spyOn($scope, 'initLoad');
       var todaysDate = new Date();
       var day30Date = new Date();
       var day60Date = new Date();
       var day90Date = new Date();
       day30Date.setDate(todaysDate.getDate() - 31);
       day60Date.setDate(todaysDate.getDate() - 61);
       day90Date.setDate(todaysDate.getDate() - 91);
       
      $scope.model.notifications.push({type: 'Weather', generationDate: todaysDate, zipcodes: [], description: 'Pick up essentials and leave', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: todaysDate, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Fire', generationDate: day30Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: day30Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: day60Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Fire', generationDate: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Weather', generationDate: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});
      $scope.model.notifications.push({type: 'Flood', generationDate: day90Date, zipcodes: ['99999','94545-444'], description: 'Urgent message', citizensAffected: 111});

     
    $scope.changeFilters();
 
    expect($scope.model.notifications.length).toBe(10);
    expect($scope.model.filteredNotifications.length).toBe(2);
    
    $scope.eventTypeFilter='Weather';
    $scope.eventTimeFilter=30;
    $scope.changeFilters();
    expect($scope.model.filteredNotifications.length).toBe(1);

    $scope.eventTypeFilter='Flood';
    $scope.eventTimeFilter=365;
    $scope.changeFilters();
    expect($scope.model.filteredNotifications.length).toBe(6);


    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter=365;
    $scope.changeFilters();
    expect($scope.model.filteredNotifications.length).toBe(10);      

    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter=60;
    $scope.changeFilters();
    expect($scope.model.filteredNotifications.length).toBe(4);      
      
    $scope.eventTypeFilter=undefined;
    $scope.eventTimeFilter=90;
    $scope.changeFilters();
    expect($scope.model.filteredNotifications.length).toBe(7);      
      
  });
  it('set the current select event and the showMapOrDetails', function() {
    var todaysDate = new Date();
    var selectedEvent =  {type: 'Weather', generationDate: todaysDate, zipcodes: [], description: 'Pick up essentials and leave', citizensAffected: 111};
    $scope.loadEventDetails(selectedEvent);

      expect($scope.currentSelectedEvent).toBe(selectedEvent);
      expect($scope.showMapOrDetails).toBe('DETAILS');
  });

 it('set the current select event and the showMapOrDetails', function() {
    var todaysDate = new Date();
    var selectedEvent =  {type: 'Weather', generationDate: todaysDate, zipcodes: [], description: 'Pick up essentials and leave', citizensAffected: 111};
    $scope.loadMap(selectedEvent);

      expect($scope.currentSelectedEvent).toBe(selectedEvent);
      expect($scope.showMapOrDetails).toBe('MAP');
 });

    
  
});
