'use strict';

/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * authentication service.
 */
cgiWebApp // jshint ignore:line
.service('Authenticator', [ '$http', '$location', 'urls', function($http, $location, urls) {

    this.authenticate = function(dataObject) {

        var res = $http.post(urls.BASE + '/login',
        // 'http://localhost:8080/login',
        dataObject);
        var promise = res.then(function successCallback(response) {

            return response;

        }, function errorCallback(response) {
            return response;
        });

        return promise;
    };

} ]);
