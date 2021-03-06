/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 *
 * Profile Controller.
 */

'use strict';

cgiWebApp.controller('ProfileController',
  ['$scope', 'ProfileService', '$state', 'Authenticator', '$anchorScroll',
  function ($scope, ProfileService, $state, Authenticator, $anchorScroll) {

  $scope.init = function() {
    $scope.apiErrors = [];

    $scope.profile = {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      passwordConfirmation: '',
      phone: '',
      address1: '',
      address2: '',
      city: '',
      state: '',
      zipCode: '',
      emailNotification: false,
      smsNotification: false,
      shouldComparePassword: false
    };

    if ($scope.isNew()) {
      $scope.profile.emailNotification = true;
      $scope.profile.shouldComparePassword = true;
    }

    if ($scope.isEdit()) {
      ProfileService.getProfile().then(function(response) {
        $scope.profile = response.data;

        $scope.processForEmptyString($scope.profile, 'firstName');
        $scope.processForEmptyString($scope.profile, 'lastName');
        $scope.processForEmptyString($scope.profile, 'phone');
        $scope.processForEmptyString($scope.profile, 'address1');
        $scope.processForEmptyString($scope.profile, 'address2');
        $scope.processForEmptyString($scope.profile, 'city');
        $scope.processForEmptyString($scope.profile, 'state');

        // the password is not sent from the API
        $scope.profile.password = '';
        $scope.profile.passwordConfirmation = '';
        $scope.profile.shouldComparePassword = false;
      });
    }

    $scope.regexZip = /^\d{5}$/;
    $scope.regexPassword = /^(?=.{8,})((?=.*\d)(?=.*[a-z])(?=.*[A-Z])|(?=.*\d)(?=.*[a-zA-Z])(?=.*[\W_])|(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_])).*/;
    $scope.regexPhone = /^\d{3}-?\d{3}-?\d{4}$/;
  };

  $scope.processApiErrors = function(response) {
    $scope.apiErrors = [];
    if (response.data && response.data.errors) {
      for (var i = 0; i < response.data.errors.length; i++) {
        if (response.data.errors[i].message) {
          $scope.apiErrors.push(response.data.errors[i].message);
        }
      }
    } else if (response.status === 401 && response.data) {
      $scope.apiErrors.push(response.data);
    } else {
      $scope.apiErrors.push('Server error occurred. Please try again later.');
    }

    if ($scope.apiErrors.length > 0) {
      $anchorScroll();
    }
  };

  $scope.generatePhoneNumber = function() {
    $scope.profile.phone = $scope.profile.phone.replace(/-/g, '');
  };

  // this is meant to null out any empty strings, be careful with false/0
  $scope.processForNull = function(toSend, property) {
    if(!toSend[property]) {
      toSend[property] = null;
    }
  };

  // this is meant to set empty strings for nulls, be careful with non-strings
  $scope.processForEmptyString = function(retrievedProfile, property) {
    if(retrievedProfile[property] === null) {
      retrievedProfile[property] = '';
    }
  };

  $scope.process = function(beforeNavPromise) {
    $scope.generatePhoneNumber();

    var toSend = {
      email: $scope.profile.email.toLowerCase(),
      password: $scope.profile.password,
      firstName: $scope.profile.firstName,
      lastName: $scope.profile.lastName,
      phone: $scope.profile.phone,
      address1: $scope.profile.address1,
      address2: $scope.profile.address2,
      city: $scope.profile.city,
      state: $scope.profile.state,
      zipCode: $scope.profile.zipCode,
      emailNotification: $scope.profile.emailNotification,
      pushNotification: false,
      smsNotification: $scope.profile.smsNotification
    };

    $scope.processForNull(toSend, 'firstName');
    $scope.processForNull(toSend, 'lastName');
    $scope.processForNull(toSend, 'phone');
    $scope.processForNull(toSend, 'address1');
    $scope.processForNull(toSend, 'address2');
    $scope.processForNull(toSend, 'city');
    $scope.processForNull(toSend, 'state');

    //putting this on scope so I can test
    $scope.toSend = toSend;

    var toCall;
    if ($scope.isNew()) {
      toCall = ProfileService.register;
    } else {
      toCall = ProfileService.update;
      if ($scope.profile.password === '') {
        toSend.password = '';
      }
    }

    toCall(toSend).then(function() {
      if (beforeNavPromise) {
        beforeNavPromise(toSend).then(function() {
          $scope.proceedForward('landing');
        });
      }
      else {
        $scope.proceedForward('landing');
      }
    }).catch(function(response) {
      $scope.processApiErrors(response);
    });
  };

  $scope.saveProfile = function(form) {
    if (!form.$valid) {
      return;
    }
    if ($scope.isNew()) {
      var beforeNavPromise = function(toSend) {
        var credentials = {
          email: toSend.email,
          password: toSend.password
        };

        return Authenticator.authenticate(credentials);
      };
      $scope.process(beforeNavPromise);
    } else {
      $scope.process();
    }
  };

  $scope.isNew = function() {
    return $state.current.name === 'register';
  };

  $scope.isEdit = function() {
    return $state.current.name === 'manageProfile';
  };

  $scope.isPasswordValid = function() {
    if ($scope.isNew()) {
      return $scope.profile.password !== '' && $scope.regexPassword.test($scope.profile.password);
    }
    else {
      return $scope.profile.password === '' || $scope.regexPassword.test($scope.profile.password);
    }
  };

  $scope.goBack = function() {
    if ($scope.isNew()) {
      $state.go('login');
    }
    else {
      $state.go('landing');
    }
  };

  $scope.proceedForward = function() {
    $state.go('landing');
  };

  $scope.keyCallback = function($event) {
    $scope.keyCode = $event.which;
    if($scope.keyCode === 27){
      $scope.goBack();
    }
  };

  $scope.shouldComparePasswords = function() {
    return $scope.profile.shouldComparePassword || $scope.profile.password !== '' || $scope.profile.passwordConfirmation !== '';
  };

  $scope.init();

}]);

cgiWebApp.directive('compareTo', function() {
  return {
    require: 'ngModel',
    scope: {
      otherModelValue: '=compareTo'
    },
    link: function(scope, element, attributes, ngModel) {
      ngModel.$validators.compareTo = function(modelValue) {
        if (attributes.shouldCompare) {
          return modelValue === scope.otherModelValue;
        }
        return true;
      };

      scope.$watch('otherModelValue', function() {
        ngModel.$validate();
      });

      attributes.$observe('shouldCompare', function() {
        ngModel.$validate();
      });
    }
  };
});
