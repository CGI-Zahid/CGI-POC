'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description
 * # pocsacApp
 *
 * Broadcaster service.
 */
cgiWebApp // jshint ignore:line
  .service('Broadcaster', function($http, $location, urls) {

    this.alertMessage = function(message) {
      alert(message);
    };
  });
