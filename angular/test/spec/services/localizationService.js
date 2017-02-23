'use strict';

describe('Localizator', function() {
  var $httpBackend;
  var urls;
  var LocalizeService;
  var $sessionStorage;

  beforeEach(module('cgi-web-app'));

  beforeEach(inject(function(_Localizator_, _urls_, _$httpBackend_, _$sessionStorage_) {
    LocalizeService = _Localizator_;
    urls = _urls_;
    $httpBackend = _$httpBackend_;
    $sessionStorage = _$sessionStorage_;
  }));

  describe('localizer', function() {
    it('should post to the expected localize endpoint', function() {
      var coords = {geoloclatitude : 53.00, geoloclongitude : -121.00};
      $httpBackend.expectPUT('http://localhost:8080/user/geoLocation', coords)
        .respond(200, {});

      LocalizeService.localize(coords);
      $httpBackend.flush();

      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

    it('should construct the endpoint URL', function() {
      var coords = {geoloclatitude : 53.00, geoloclongitude : -121.00};
      $httpBackend.expectPUT(urls.BASE + '/user/geoLocation', coords)
        .respond(200, {});

      LocalizeService.localize(coords);
      $httpBackend.flush();

      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });
});
