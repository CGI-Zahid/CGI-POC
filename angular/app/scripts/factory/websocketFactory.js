'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description
 * # pocsacApp
 *
 * Websocket Factory.
 */
cgiWebApp // jshint ignore:line
  .factory('WebsocketInit', ['$websocket', 'urls', 'Broadcaster', function($websocket, urls, Broadcaster) {
    // Open a WebSocket connection
    var dataStream = $websocket('ws://' + urls.HOSTNAME + ':8080' + '/alert');

    // Inital connection callback.
    dataStream.onOpen(function(){
      console.log('Websockets connected');
    });

    // Process message.
    dataStream.onMessage(function(message) {
      Broadcaster.alertMessage(message);
    });

    var methods = {
      get: function() {
        dataStream.send(JSON.stringify({ action: 'get' }));
      }
    };

    return methods;
  }]);
