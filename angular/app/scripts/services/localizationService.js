'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description
 * # pocsacApp
 *
 * geo localization setter service.
 */
cgiWebApp.service('Localizator',
  ['$http', 'urls', '$sessionStorage',
  function($http, urls, $sessionStorage) {

  this.localize = function(coords) {
    var authToken = $sessionStorage.get('jwt');
    var endpoint = urls.BASE + '/user/geoLocation';
    return $http.put(endpoint, coords, {
      headers: { 'Authorization': 'Bearer ' + authToken }
    });
  };
}]);