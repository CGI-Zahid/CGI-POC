/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * Profile Controller
 */

'use strict';

cgiWebApp // jshint ignore:line
.controller('profileController', [ '$scope', '$rootScope', '$state','uswdsLoadService', 'registrationService', '$timeout','$sessionStorage',

function($scope, $rootScope, $state, uswdsLoadService, registrationService, $timeout, $sessionStorage) {
  
  $scope.manageProfile = function(){
     $rootScope.toggleRegistration = false;
     $('#registrationModal').modal('show'); // jshint ignore:line
     document.getElementById('profileEmail').disabled = true;
     document.getElementById('profileEmailLabel').className = 
         document.getElementById('profileEmailLabel').className.replace( /(?:^|\s)usa-input-required(?!\S)/g , '' );
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
      notification:'',
      emailNotification: false,
      pushNotification: false,
      smsNotification:false,
      byPhoneGeolocation: false
      
  };
  var errorNotif = false;
  var errorMessage = false;
  var successMessage = false;
  var successNotif = false;
  
  $scope.popUp = function(code, message, duration) {
      if (code === 'error') {
          errorNotif = true;
          errorMessage = message;
      } else if (code === 'success') {
          successNotif = true;
          successMessage = message;
      }
      $timeout(function() {
          $scope.closeAlert(code);
      }, duration);
  };
  
   var blankUser = $scope.user;
   
  
  
    $scope.regexName = /^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,.\'-]{2,}$/;
    $scope.regexPhone3 = /^\d{3}$/;
    $scope.regexPhone4 = /^\d{4}$/;
    $scope.regexZip = /^\d{5}$/;
    $scope.regexPassword = /^(?=.{8,})((?=.*\d)(?=.*[a-z])(?=.*[A-Z])|(?=.*\d)(?=.*[a-zA-Z])(?=.*[\W_])|(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_])).*/;
    
    $scope.someSelected = function (object) {
        if (!object) {
          return false;
        }
        return Object.keys(object).some(function (key) {
          return object[key];
        });
    };
    
    $scope.submit = function(isValid){
        // this is used by angular validation
        var submitted = true;// jshint ignore:line
        
        if(isValid){
            
            var completePhone = $scope.user.phoneNumberAreaCode + $scope.user.phoneNumberMiddle + $scope.user.phoneNumberEnd;

            var notificationType;
            //Notification: 1 - EMAIL, 2 - SMS, 3 -PUSH
            if($scope.user.emailNotification){
                notificationType.push(1);
            }
            if($scope.user.smsNotification){
                notificationType.push(2); 
            }
            if($scope.user.pushNotification){
                notificationType.push(3); 
            }    
                        
            var sessionToRegister = {
                'firstName': $scope.user.firstName,
                'lastName': $scope.user.lastName,
                'email': $scope.user.email,
                'password': $scope.user.password,
                'phone': completePhone,
                'zipCode': $scope.user.firstName,
                'notificationType': notificationType
            };
            
            //call to register
            registrationService.register(sessionToRegister).then(function(response){
                if (response.status === 200) {
                    errorNotif = false;

                    successNotif = true;
                    successMessage = 'PROFILE.MESSAGE.REGISTERED';
                    //token if user is logged in during registration.
                    //$sessionStorage.put('jwt', response.data.authToken);
                    $timeout(function(){$('#registrationModal').modal('hide')},3);// jshint ignore:line

                } else if (response.status === 401) {
                    $scope.popUp('error', 'PROFILE.MESSAGE.INVALID', POP_UP_DURATION); // jshint ignore:line
                } else {
                    $scope.popUp('error', 'GENERIC.MESSAGE.ERROR.SERVER', POP_UP_DURATION); // jshint ignore:line
                }
                //$scope.profileForm.$setPristine();
                //$scope.profileForm.$setUntouched();
                
                //currently do nothing if the response fails  
            });
        }
    };
    
    $scope.toggleConfirmation = function(){
        $('#registrationModal').modal('hide');// jshint ignore:line
        $('#closeConfirmationModal').modal('show');// jshint ignore:line
    };
    $scope.backToForm = function(){
        $('#closeConfirmationModal').modal('hide');// jshint ignore:line
        $('#registrationModal').modal('show');// jshint ignore:line
    };
    $scope.closeToLogin = function(){
        $scope.user = angular.copy(blankUser);
        $sessionStorage.clear();
        $state.go('login');
    };
   
   var init = function(){
       uswdsLoadService.triggerEvent(document, 'DOMContentLoaded');
       if($rootScope.toggleRegistration === true){
           $('#registrationModal').modal({// jshint ignore:line
               backdrop: 'static',
               keyboard: false
             });
       }  
   };
    
    init();
  
}]);

cgiWebApp// jshint ignore:line
.directive('compareTo', function(){
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
}).directive('autoTabTo', function () {
    return {
        restrict: 'A',
        link: function (scope, el, attrs) {
            el.bind('keyup', function() {
                if (this.value.length === parseInt(attrs.ngMaxlength)) {
                    var element = document.getElementById(attrs.autoTabTo);
                    if (element){
                        element.focus();
                    }
                }
            });
        }
    };
 });

