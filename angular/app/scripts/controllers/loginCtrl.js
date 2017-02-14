/**
 * @ngdoc overview
 * @name pocsacApp
 * @description # pocsacApp
 * 
 * Login Controller.
 */

'use strict';

cgiWebApp // jshint ignore:line
.controller('loginController', [ '$scope', '$rootScope', '$state','Authenticator', 'uswdsLoadService','$timeout', '$sessionStorage',

function ($scope, $rootScope, $state, Authenticator, uswdsLoadService, $timeout, $sessionStorage) {

    $scope.popUp = function(code, message, duration) {
        if (code === 'error') {
            model.errorNotif = true;
            model.errorMessage = message;
        } else if (code === 'success') {
            model.successNotif = true;
            model.successMessage = message;
        }
        $timeout(function() {
            $scope.closeAlert(code);
        }, duration);
    };

    var model = this;

    model.errorNotif = false;
    model.successNotif = false;
    model.errorMessage = 'GENERIC.MESSAGE.ERROR.SERVER';
    model.successMessage = 'GENERIC.MESSAGE.SUCCESS';

    $scope.user = {
        username : '',
        password : ''
    };

       
    $scope.submitForm = function(isValid) {
        if (isValid) {

            var dataObject = {
                email : $scope.user.username,
                password : $scope.user.password
            };

            // call to the authenticate service
            Authenticator.authenticate(dataObject).then(function(response) {
                if (response.status === 200) {
                    model.errorNotif = false;

                    model.successNotif = true;
                    model.successMessage = 'LOGIN.MESSAGE.LOGGEDIN';;
                    $sessionStorage.put('jwt', response.data.authToken);
                    $state.go('profile');

                } else if (response.status === 401) {
                    $scope.popUp('error', 'LOGIN.MESSAGE.INVALID', POP_UP_DURATION); // jshint ignore:line
                } else {
                    $scope.popUp('error', 'GENERIC.MESSAGE.ERROR.SERVER', POP_UP_DURATION); // jshint ignore:line
                }

                //$scope.authForm.$setPristine();
                //$scope.authForm.$setUntouched();

            });

            // Making the fields empty
            $scope.user.username = '';
            $scope.user.password = '';
        }
    };
    
    $scope.toggleRegistrationModal = function(){
      $rootScope.toggleRegistration = true;  
      $state.go('profile');
    };

    $scope.closeAlert = function(code) {
        if (code === 'error') {
            model.errorNotif = false;
            model.errorMessage = '';
        } else {
            model.successNotif = false;
            model.successMessage = '';
        }
    };
    var init = function(){
        $rootScope.toggleRegistration = false;
        uswdsLoadService.triggerEvent(document, 'DOMContentLoaded');
    };
    
    init();
} ]);
