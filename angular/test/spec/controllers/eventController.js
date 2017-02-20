'use strict';

describe('eventController', function() {
  var eventController;
  var $scope;
  var $sessionStorage;
  var $state;
  var eventService;
  var $q;
  var deferred;

  beforeEach(module('cgi-web-app'));

  beforeEach(inject(function(_$rootScope_, _$controller_, _$sessionStorage_, _$state_, _EventNotificationService_, _$q_) {
    $q = _$q_;
    $scope = _$rootScope_.$new();
    $sessionStorage = _$sessionStorage_;
    $state = _$state_;
    eventService = _EventNotificationService_;

    deferred = _$q_.defer();
    spyOn(eventService, 'publish').and.returnValue(deferred.promise);

    eventController = _$controller_('eventController', {
      $scope: $scope,
      $sessionStorage: $sessionStorage,
      $state: $state,
      EventNotificationService: eventService
    });
  }));

  describe('init', function() {
    it('should initialize the controller', function() {
      $scope.init();
      expect($scope.notification.messageMaxLength).toBe(135);
      expect($scope.notification.notificationType).toBe('');
      expect($scope.notification.zipCodes).toBe('');
      expect($scope.notification.message).toBe('');
    });

    it('should have these pattern validations', function() {
      $scope.init();
      expect('12345').toMatch($scope.regexZipCodes);
      expect('12345,23456').toMatch($scope.regexZipCodes);
      expect('12345,23456,23567').toMatch($scope.regexZipCodes);
      expect('12345, 23456, 23567').toMatch($scope.regexZipCodes);
      expect('12345 , 23456 , 23567').toMatch($scope.regexZipCodes);
    });
  });

  describe('publishEvent', function() {
    it('should call the EventNotificationService.publish', function() {
      $scope.publishEvent();
      deferred.resolve({ status: 200, data: {} });
      $scope.$apply();
      expect(eventService.publish).toHaveBeenCalledWith($scope.toSend);
    });

    it('should construct the zipCodes', function() {
      $scope.notification.zipCodes = '12345, 23456, 34567';
      spyOn($scope, 'generateZipCodes').and.callThrough();
      $scope.publishEvent();
      deferred.resolve({ status: 200, data: {} });
      $scope.$apply();
      expect($scope.generateZipCodes).toHaveBeenCalled();
    });

    it('should redirect if successful', function() {
      spyOn($state, 'go');
      $scope.publishEvent();
      deferred.resolve({ status: 200, data: {} });
      $scope.$apply();
      expect($state.go).toHaveBeenCalledWith('landing');
    });

    it('should construct apiErrors if failed', function() {
      spyOn($scope, 'processApiErrors');
      $scope.publishEvent();
      var response = { status: 404, data: {} };
      deferred.reject(response);
      $scope.$apply();
      expect($scope.processApiErrors).toHaveBeenCalledWith(response);
    });

    it('should send this data to the EventNotificationService', function() {
      $scope.notification.notificationType = 'ADMIN_I';
      $scope.notification.zipCodes = '12345, 23456 , 45678';
      $scope.notification.message = 'blah blah message. get away!';

      $scope.publishEvent();
      deferred.resolve({ status: 200, data: {} });
      $scope.$apply();
      expect($scope.toSend.notificationType).toBe('ADMIN_I');
      expect($scope.toSend.message).toBe('blah blah message. get away!');
      expect($scope.toSend.zipcodes.length).toBe(3);
      expect($scope.toSend.zipcodes[0]).toBe('12345');
      expect($scope.toSend.zipcodes[1]).toBe('23456');
      expect($scope.toSend.zipcodes[2]).toBe('45678');
    });
  });

  describe('processApiErrors', function() {
    it('should construct the apiErrors', function() {
      var response = {
        status: 404,
        data: {
          errors: [
            { code: 'ERR3', message: 'Stuff cannot be blank' },
            { code: 'ERR4', message: 'You should be blank' },
            { code: 'ERR3', message: 'Please do not be blank' }
          ]
        }
      };
      $scope.processApiErrors(response);
      expect($scope.apiErrors[0]).toBe('Stuff cannot be blank');
      expect($scope.apiErrors[1]).toBe('You should be blank');
      expect($scope.apiErrors[2]).toBe('Please do not be blank');
    });

    it('should construct the apiErrors for unauthorized', function() {
      var response = {
        status: 401,
        statusText: 'Unauthorized',
        data: 'Credentials are required to access this resource.'
      };
      $scope.processApiErrors(response);
      expect($scope.apiErrors[0]).toBe('Credentials are required to access this resource.');
    });

    it('should ignore other pieces of data', function() {
      var response = {
        status: 404,
        data: {
          err: [
            { code: 'ERR3', message: 'Stuff cannot be blank' }
          ],
          errors: [
            { code: 'ERR4', error: 'You should be blank' },
            { code: 'ERR3', errorMessage: 'Please do not be blank' }
          ]
        },
        datas: {
          errors: [
            { code: 'ERR3', message: 'Stuff cannot be blank' },
            { code: 'ERR4', message: 'You should be blank' },
            { code: 'ERR3', message: 'Please do not be blank' }
          ]
        }
      };
      $scope.processApiErrors(response);
      expect($scope.apiErrors.length).toBe(0);
    });
  });

  describe('generateZipCodes', function() {
    it('generates the zip codes by splitting them', function() {
      $scope.notification.zipCodes = '12345, 23456, 34567';
      $scope.generateZipCodes();
      expect($scope.notification.zipCodesSplit.length).toBe(3);
      expect($scope.notification.zipCodesSplit[0]).toBe('12345');
      expect($scope.notification.zipCodesSplit[1]).toBe('23456');
      expect($scope.notification.zipCodesSplit[2]).toBe('34567');
    });
  });
});
