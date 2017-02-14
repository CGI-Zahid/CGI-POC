'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * usdws load service.
 */
cgiWebApp // jshint ignore:line
.service('uswdsLoadService', [ '$timeout', function($timeout) {
    this.triggerEvent = function(el, eventName) {
        $timeout(function() {
            var event;
            if (window.CustomEvent) {
                event = new CustomEvent(eventName);
            } else {
                event = document.createEvent('DOMContentLoaded');
            }
            el.dispatchEvent(event);
        }, 0);
    };
} ]);
