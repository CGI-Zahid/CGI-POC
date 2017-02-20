'use strict';

describe('EventSerivce', function() {
  var eventService;
  var $httpBackend;
  var urls;

  beforeEach(module('cgi-web-app'));

  beforeEach(inject(function(_EventSerivce_, _urls_, _$httpBackend_) {
    eventService = _EventSerivce_;
    urls = _urls_;
    $httpBackend = _$httpBackend_;
  }));

  describe('publish', function() {
    it('should post to the expected publish endpoint', function() {
      var notification = {};
      $httpBackend.expectPOST('http://localhost:8080/notification', notification)
        .respond(200, {});

      eventService.publish(credentials);
      $httpBackend.flush();

      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

    it('should construct the endpoint URL', function() {
      var notification = {};
      $httpBackend.expectPOST(urls.BASE + '/notification', notification)
        .respond(200, {});

      eventService.publish(credentials);
      $httpBackend.flush();

      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });
  });
});
