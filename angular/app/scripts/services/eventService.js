'use strict';

cgiWebApp.service('EventSerivce',
  ['$http', 'urls',
  function($http, urls) {

  this.publish = function(notification) {
    var endpoint = urls.BASE + '/login';
    return $http.post(endpoint, notification);
  };
}]);
