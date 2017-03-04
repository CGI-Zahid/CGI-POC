'use strict';

var cgiWebApp = angular.module('cgi-web-app',
  ['pascalprecht.translate', 'ngSessionStorage', 'ui.router', 'ngMessages', 'uiGmapgoogle-maps']);

cgiWebApp.constant('urls', {
  // have to be change depending of the environment
  BASE: 'http://localhost:8080'
})
.config(['$translateProvider', '$urlRouterProvider', '$stateProvider', '$sceDelegateProvider',
  function($translateProvider, $urlRouterProvider, $stateProvider, $sceDelegateProvider) {


  $sceDelegateProvider.resourceUrlWhitelist([
    // Allow same origin resource loads.
    'self',
    // Allow loading from our assets domain.  Notice the difference between * and **.
    'https://maps.google.com/**'
  ]);


  $translateProvider.useStaticFilesLoader({
    prefix: 'language/locale-', // path to translations files
    suffix: '.json' // suffix, currently- extension of the translations
  });
  $translateProvider.preferredLanguage('en');
  //disabling escaping for the time being, eliminates warnings for browsers
  //plan: introduce $sanitize library to support sanitization of characters when used in translate 
  $translateProvider.useSanitizeValueStrategy(null);

  //if no url is specified redirect to login
  $urlRouterProvider.otherwise('login');

  $stateProvider.state('login', {
    url: '/login',
    module: 'public',
    views: {
      'pageContent': {
        templateUrl: '/views/login.html',
        controller: 'loginController'
      }
    }
  }).state('register', {
    url: '/register',
    module: 'public',
    views: {
      'pageContent': {
        templateUrl: '/views/profile.html',
        controller: 'ProfileController'
      }
    }
  }).state('landing', {
    url: '/landing',
    module: 'restricted',
    views: {
      'pageContent': {
        templateUrl: 'views/landing.html',
        controller: 'landingController'
      }
    }
  }).state('manageProfile', {
    url: '/manageProfile',
    module: 'restricted',
    views: {
      'pageContent': {
        templateUrl: '/views/profile.html',
        controller: 'ProfileController'
      }
    }
  }).state('publish', {
    url: '/publish',
    module: 'restricted',
    views: {
      'pageContent': {
        templateUrl: '/views/publish.html',
        controller: 'eventController'
      }
    }
  });
}])

.run(['$sessionStorage', '$rootScope', '$state', 'Authenticator', '$http',
  function ($sessionStorage, $rootScope, $state, Authenticator, $http) {

  var authToken = $sessionStorage.get('jwt');
  if (authToken) {
    $http.defaults.headers.common['Content-Type'] = 'application/json';
    $http.defaults.headers.common.Authorization =  'Bearer ' + authToken;
  }

  $rootScope.$on('$stateChangeStart', function(e, toState, toParams, fromState, fromParams) { // jshint ignore:line
    var authenticated = Authenticator.isLoggedIn();
    if (toState.module === 'restricted' && !authenticated) {
      // If logged out and transitioning to a logged in page:
      e.preventDefault();
      $state.go('login');
    }
  });
}]);
