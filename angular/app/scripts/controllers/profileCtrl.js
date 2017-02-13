/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * Profile Controller
 */

'use strict';

cgiWebApp // jshint ignore:line
.controller('profileController', [ '$scope', '$rootScope',

function($scope, $rootScope) {

  $scope.user = {
      email: 'aUserEmail@cantChangeAccountName.com'       
  };
  $scope.saveProfile = function(){
      $scope.user.username = 'dafddsfdasfdsafdasfsda';
      $('#registrationModal').modal('show');// jshint ignore:line
  };
  $scope.$on('toggleRegistrationModal', function(){
      $('#registrationModal').modal('show');// jshint ignore:line
  });
  if($rootScope.toggleRegistration === true){
      $('#registrationModal').modal('show');// jshint ignore:line
  }
  
  $scope.manageProfile = function(){
    $('#registrationModal').modal('show'); // jshint ignore:line
  };
  
  $scope.user = {
      firstName: '',
      lastName: '',
      email: '',
      confirmEmail: '',
      password:'',
      confirmPassword:'',
      phoneNumberAreaCode: '',
      phoneNumberMiddle:'',
      phoneNumberEnd:'',
      zipCode: '',
    };
    $scope.regexName = /^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,.\'-]{2,}$/;
    $scope.regexPhone3 = /^\d{3}$/;
    $scope.regexPhone4 = /^\d{4}$/;
    $scope.regexZip = /^\d{5}$/;
    $scope.regexPassword = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
    $scope.cancel = function(){
       confirm('You have not saved your profile. Are you sure you want to cancel?');// jshint ignore:line
    };
    
    $scope.someSelected = function (object) {
        if (!object) {
          return false;
        }
        return Object.keys(object).some(function (key) {
          return object[key];
        });
    };
  
}]);

var compareTo = function() {
  return {
      require: 'ngModel',
      scope: {
          otherModelValue: '=compareTo'
      },
      link: function(scope, element, attributes, ngModel) {

          ngModel.$validators.compareTo = function(modelValue) {
              return modelValue === scope.otherModelValue;
          };

          scope.$watch('otherModelValue', function() {
              ngModel.$validate();
          });
      }
  };
};

cgiWebApp// jshint ignore:line
.directive('compareTo', compareTo);