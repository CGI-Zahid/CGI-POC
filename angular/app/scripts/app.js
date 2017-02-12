'use strict';

var cgiWebApp = angular.module('cgi-web-app', ['pascalprecht.translate', 'ngSessionStorage', 'ngRoute', 'ngWebSocket']);

var POP_UP_DURATION = 30 * 1000; // jshint ignore:line

cgiWebApp
    .constant('urls', {
        //have to be change depending of the envirronment
        BASE: 'http://localhost:8080',
        HOSTNAME: 'localhost'
    })
    .config(['$translateProvider', '$routeProvider', function($translateProvider, $routeProvider) {

      $translateProvider.useStaticFilesLoader({
        prefix: 'language/locale-', // path to translations files
        suffix: '.json' // suffix, currently- extension of the translations
      });
      $translateProvider.preferredLanguage('en');

      $routeProvider.
      when('/', {
          templateUrl: '/views/login.html',
//          controller: 'loginController'
      }).
      when('/login', {
          templateUrl: 'views/login.html',
//          controller: 'loginController'
      }).
      when('/signup', {
          templateUrl: 'views/signup.html',
//          controller: 'signupController'
      }).
      when('/forgetcredential', {
          templateUrl: 'views/forgetcredential.html',
//          controller: 'forgetUPController'
      }).
      when('/restricted', {
          templateUrl: 'views/restricted.html',
//          controller: 'RestrictedController'
      }).
      otherwise({
          redirectTo: '/'
      });
    }]);
