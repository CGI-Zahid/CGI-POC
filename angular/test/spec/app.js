'use strict';

describe('app', function() {
  var $scope;
  var $sessionStorage;
  
  beforeEach(module('cgi-web-app'));
  
  beforeEach(inject(function(_$rootScope_, _$sessionStorage_) {
    $scope = _$rootScope_.$new();
    $sessionStorage = _$sessionStorage_;
  }));
  
    describe('isLoggedIn', function() {
    it('should consider those with authToken to be logged in', function() {
      spyOn($sessionStorage, 'get').and.returnValue('the jwt token');
      expect($scope.isLoggedIn()).toBe(true);
    });

    it('should consider those without authToken to be logged out', function() {
      spyOn($sessionStorage, 'get').and.returnValue(null);
      expect($scope.isLoggedIn()).toBe(false);
    });
  });
});