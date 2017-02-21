'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description
 * # pocsacApp
 *
 * profile service.
 */
cgiWebApp.service('EventNotificationService',
  ['$http', 'urls',
  function($http, urls) {

  this.allNotifications = function() {
    var endpoint = urls.BASE + '/notification/admin';
    return $http.get(endpoint);
  };

  this.userNotifications = function() {
    var endpoint = urls.BASE + '/notification/user';
    return $http.get(endpoint);
  };

  this.publish = function(notification) {
    var endpoint = urls.BASE + '/notification';
    return $http.post(endpoint, notification);
  };
}]);
