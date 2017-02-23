'use strict';

var cgiWebApp = angular.module('cgi-web-app', [ 'pascalprecht.translate','ngSessionStorage', 'ui.router', 'ngWebSocket', 'ngMessages','uiGmapgoogle-maps', 'ngGeolocation' ]);

cgiWebApp.constant('urls', {
  // have to be change depending of the environment
  BASE: 'http://localhost:8080',
  WS_BASE: 'ws://localhost:8080'
})
.config([ '$translateProvider', '$urlRouterProvider', '$stateProvider','$sceDelegateProvider',
    function($translateProvider, $urlRouterProvider, $stateProvider,$sceDelegateProvider ) {


$sceDelegateProvider.resourceUrlWhitelist([
    // Allow same origin resource loads.
    'self',
    // Allow loading from our assets domain.  Notice the difference between * and **.
    'https://maps.google.com/**'


  ]);


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
      'pageContent':{
        templateUrl: '/views/login.html',
        controller: 'loginController'
  }
    }
  }).state('register', {
    url: '/register',
    views:{
      'pageContent':{
        templateUrl: '/views/profile.html',
        controller: 'ProfileController'
    }
    }
  }).state('landing', {
       url : '/landing',
    views:{
      'pageContent':{
       templateUrl: 'views/landing.html',
       controller: 'landingController'
    }
    }
  }).state('manageProfile', {
    url: '/manageProfile',
    views: {
      'pageContent': {
        templateUrl: '/views/profile.html',
        controller: 'ProfileController'
      }
    }
  }).state('publish', {
    url: '/publish',
    views: {
      'pageContent': {
        templateUrl: '/views/publish.html',
        controller: 'eventController'
      }
    }
  });
}])

.run(['$sessionStorage', '$http', '$rootScope', '$location', function ($sessionStorage, $http, $rootScope, $location) {

        var authToken = $sessionStorage.get('jwt');
        // Setup api access token
        $http.defaults.headers.common['Content-Type'] = 'application/json';
        $http.defaults.headers.common.Authorization =  'Bearer ' + authToken;
        //Caching will be set by the nginx, so lets take advantage of that.
        //$http.defaults.headers.common['Cache-Control'] = 'no-cache';
        
        $rootScope.isLoggedIn = function(){
            return $sessionStorage.get('jwt') !== null ;
          };

        // redirect to login page if not logged in and trying to access a restricted page
        $rootScope.$on('$locationChangeStart', function (event, next, current) { // jshint ignore:line
            var publicPages = ['/login'];
            var restrictedPage = publicPages.indexOf($location.path()) === -1;
            if (restrictedPage && !$sessionStorage.get('jwt')) {
                $location.path('/login');
            }
        });

    }]);
