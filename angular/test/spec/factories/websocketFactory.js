'use strict';

describe('Service: Socket', function() {

  var socketMock, socketSpy, localConfigMock, service, $broadcaster,
    onOpenCallback,
    onCloseCallback,
    onErrorCallback,
    onMessageCallback;

  beforeEach(module('cgi-web-app'));

  beforeEach(function() {

    socketMock = {
      onOpen: function(callback) {
        onOpenCallback = callback;
      },
      onMessage: function(callback) {
        onMessageCallback = callback;
      },
      onError: function(callback) {
        onErrorCallback = callback;
      },
      onClose: function(callback) {
        onCloseCallback = callback;
      },
      send: function() {}
    };

    socketSpy = jasmine.createSpy().and.callFake(function() {
      return socketMock;
    });

    localConfigMock = {
      apiUrl: 'localhost:8080/alert'
    };

    module(function($provide) {
      $provide.value('$websocket', socketSpy);

      $provide.value('localConfig', localConfigMock);
    });

  });

  beforeEach(inject(function(WebsocketInit) {
    service = WebsocketInit;
  }));

  beforeEach(inject(function(Broadcaster) {
    $broadcaster = Broadcaster;
  }));

  it('should connect to socket api', function() {
    var url = 'ws://' + localConfigMock.apiUrl;
    expect(socketSpy).toHaveBeenCalledWith(url);
  });

  it('should call alert message from websocket message', function() {
    spyOn($broadcaster, 'alertMessage');

    onMessageCallback('message');

    expect($broadcaster.alertMessage).toHaveBeenCalled();
  });
});
