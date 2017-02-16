'use strict';

describe('test for user profile', function() {
    var profileController;
    var $scope; // jshint ignore:line
    var $rootScope; // jshint ignore:line
    var $controller; // jshint ignore:line
    
    var scope, formElement, form;

    beforeEach(module('cgi-web-app', function($translateProvider) {
        $translateProvider.translations('en', {});
    }));
    
   //beforeEach(module('templates'));
    
    
    beforeEach(inject(function($compile, $rootScope, $controller, $templateCache){ // jshint ignore:line
        
        scope = $rootScope.$new();
        
        profileController = $controller('profileController', {
            $scope : scope
        });
        
        
        //templateHtml = $templateCache.get('views/userProfile.html');
        
        //formElement = angular.element('<div>' + templateHtml + '</div>');
        formElement = angular.element('<form name="form"><input type="email" class="profile-form-input" name="profileEmail" id="profileEmail" data-ng-model="user.email" required="true"/>'+
                                      '<input type="password" class="profile-form-input" name="profilePassword" id="profilePassword" data-ng-model="user.password" required data-ng-pattern="regexPassword"/>'+
                                      '<input type="password" class="profile-form-input" name="profilePasswordConfirmation" id="profilePasswordConfirmation" data-ng-model="user.confirmPassword" required data-compare-to="user.password" data-ng-pattern="regexPassword">'+ 
                                      '<input class="profile-form-input" id="zipCode" type="text" name="profileZipCode" maxlength="5" data-ng-model="user.zipCode" data-ng-pattern="regexZip" required></form>');
        
        $compile(formElement)(scope);

        form = scope.form;
       

    }));
    
 
    //email validation test
    it('should be false when given invalid email', function() {
        
        //empty condition
        form.profileEmail.$setViewValue('');
        scope.$digest();
        expect(form.profileEmail.$valid).toBe(false);
        
        form.profileEmail.$setViewValue('ddd@');
        scope.$digest();
        expect(form.profileEmail.$valid).toBe(false);
    });

    it('should be true when given a valid email', function() {
        
        form.profileEmail.$setViewValue('mmm@cgi.com');
        
        scope.$digest();
        expect(form.profileEmail.$valid).toBe(true);
    });
    
  //password validation test
    it('should be false when given invalid password', function() {
        
        //doesn't contain 3 of 4 conditions
        form.profilePassword.$setViewValue('abcd1234'); 
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(false);
        
        //doesn't contain 8 letters
        form.profilePassword.$setViewValue('Abc1234');    
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(false);
    });
    
    it('should be true when given a valid password', function() {
        
        //contains 3 of 4 conditions
        form.profilePassword.$setViewValue('abc_1234'); 
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(true);
        
        //contains 3 of 4 conditions
        form.profilePassword.$setViewValue('Abcd1234');    
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(true);
        
        //contains 4 of 4 conditions
        form.profilePassword.$setViewValue('Abc_1234');    
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(true);
    });
    
  //password confirmation validation test
    it('should be false when given non matching password', function() {
        
        //doesn't match
        form.profilePassword.$setViewValue('Abc_1234');
        form.profilePasswordConfirmation.$setViewValue('Abc_12345'); 
        scope.$digest();
        expect(form.profilePasswordConfirmation.$valid).toBe(false);      
        
    });
    
    it('should be true when given a matching password', function() {
        
        //match
        form.profilePassword.$setViewValue('Abc_1234');
        form.profilePasswordConfirmation.$setViewValue('Abc_1234'); 
        scope.$digest();
        expect(form.profilePasswordConfirmation.$valid).toBe(true);      
        
    });
    
  //zip code validation test
    it('should be false when given invalid zip code', function() {
        
        //empty condition
        form.profileZipCode.$setViewValue(''); 
        scope.$digest();
        expect(form.profileZipCode.$valid).toBe(false);
        
        //contains less than 5 numbers
        form.profileZipCode.$setViewValue('1234'); 
        scope.$digest();
        expect(form.profileZipCode.$valid).toBe(false);
        
        
        //doesn't contain number only
        form.profilePassword.$setViewValue('A1234');    
        scope.$digest();
        expect(form.profilePassword.$valid).toBe(false);
    });
    
    it('should be true when given a valid zip code', function() {
        
        //contains 5 numbers
        form.profileZipCode.$setViewValue('12345'); 
        scope.$digest();
        expect(form.profileZipCode.$valid).toBe(true);
    });

});
