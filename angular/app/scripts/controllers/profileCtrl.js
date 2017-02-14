/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * Profile Controller
 */

'use strict';

cgiWebApp // jshint ignore:line
.controller('profileController', [ '$scope', '$rootScope', '$state','uswdsLoadService', '$sessionStorage',

function($scope, $rootScope, $state, uswdsLoadService, $sessionStorage) {
  
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
      phone:'',
      phoneNumberAreaCode: '',
      phoneNumberMiddle:'',
      phoneNumberEnd:'',
      zipCode: ''
    };
   var blankUser = $scope.user;
   
  
  
    $scope.regexName = /^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð,.\'-]{2,}$/;
    $scope.regexPhone3 = /^\d{3}$/;
    $scope.regexPhone4 = /^\d{4}$/;
    $scope.regexZip = /^\d{5}$/;
    $scope.regexPassword = /^(?=.{8,})((?=.*\d)(?=.*[a-z])(?=.*[A-Z])|(?=.*\d)(?=.*[a-zA-Z])(?=.*[\W_])|(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_])).*/;
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
    
    $scope.submit = function(){
        var submitted = true;
        //Save user
//        {
//            "firstName": "first",
//            "lastName": "last",
//            "email": "firstlast@cgi.com",
//            "password": "abc123",
//            "phone": "4031234567",
//            "zipCode": "92109",
//            "notificationType": [
//              {
//                "notificationId": 1
//              },
//              {
//                "notificationId": 2
//              }
//          }
    }
    
    $scope.toggleConfirmation = function(){
        $('#registrationModal').modal('hide');
        $('#closeConfirmationModal').modal('show');
    }
    $scope.backToForm = function(){
        $('#closeConfirmationModal').modal('hide');
        $('#registrationModal').modal('show');
    };
    $scope.closeToLogin = function(){
        $scope.user = angular.copy(blankUser);
        $state.go('login');
    };
   
   var init = function(){
       uswdsLoadService.triggerEvent(document, 'DOMContentLoaded');
       if($rootScope.toggleRegistration === true){
           $('#registrationModal').modal({
               backdrop: 'static',
               keyboard: false
             });// jshint ignore:line
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
        restrict: "A",
        link: function (scope, el, attrs) {
            el.bind('keyup', function(e) {
                if (this.value.length === parseInt(attrs.ngMaxlength)) {
                    var element = document.getElementById(attrs.autoTabTo);
                    if (element){
                        element.focus();
                    }
                }
            });
        }
    }
 });

