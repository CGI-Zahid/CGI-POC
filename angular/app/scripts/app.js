'use strict';

var cgiWebApp = angular.module('cgi-web-app', [ 'pascalprecht.translate', 'ngSessionStorage', 'ui.router' ]);

var POP_UP_DURATION = 30 * 1000; // jshint ignore:line

cgiWebApp.constant('urls', {
    // have to be change depending of the environment
    BASE : 'http://localhost:8080',
}).config([ '$translateProvider', '$urlRouterProvider', '$stateProvider', function($translateProvider, $urlRouterProvider, $stateProvider) {

    $translateProvider.useStaticFilesLoader({
        prefix : 'language/locale-', // path to translations files
        suffix : '.json' // suffix, currently- extension of the translations
    });
    $translateProvider.preferredLanguage('en');
    
    //if no url is specified redirect to login
    $urlRouterProvider.otherwise('login');

    $stateProvider.state('login', {
        url : '/login',
        views:{
          //'header': {
          //},
          'pageContent':{
            templateUrl: '/views/login.html',
            controller: 'loginController'
          }/*,
          'footer':{
          }*/
        }
    }).state('registration', {
        url : '/registration',
        views:{
          //'header': {
          //},
          'pageContent':{
            templateUrl: '/views/registration.html',
            controller: 'registrationCtrl'
          }/*,
          'footer':{
          }*/
        }
    // controller: 'signupController'
    }).state('profile', {
        url : '/profile',
        views:{
          'header': {
            templateUrl: 'views/userHeader.html',
            controller: 'profileController'
          },
          'pageContent':{
            templateUrl: 'views/userProfile.html',
            controller: 'profileController'
          },
          'footer':{
            templateUrl: 'views/userFooter.html'
          }
        }
        //controller : 'customizationCtrl'            
    }).state('restricted', {
        url : '/restricted',
        views:{
          //'header': {
          //},
          'pageContent':{
            templateUrl: 'views/restricted.html',
            controller: 'loginController'
          }/*,
          'footer':{
          }*/
        }
    // controller: 'RestrictedController'
    });
} ]);
