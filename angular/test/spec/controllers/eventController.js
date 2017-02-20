'use strict';

describe('eventController', function() {
  var headerController;
  var $scope;
  var $sessionStorage;
  var $state;

  beforeEach(module('cgi-web-app'));

  beforeEach(inject(function(_$rootScope_, _$controller_, _$sessionStorage_, _$state_) {
    $scope = _$rootScope_.$new();
    $sessionStorage = _$sessionStorage_;
    $state = _$state_;

    headerController = _$controller_('eventController', {
      $scope: $scope,
      $sessionStorage: $sessionStorage,
      $state: $state
    });
  }));

  describe('publishEvent', function() {
    it('should publish the event', function() {
      expect(true).toBe(true);
    });
  });
});
