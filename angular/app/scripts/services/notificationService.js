'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description
 * # pocsacApp
 *
 * Broadcaster service.
 */
cgiWebApp
  .service('Broadcaster', function() {
    this.alertMessage = function(message) {
      console.log(message);
    };
  });
