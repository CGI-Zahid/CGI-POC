/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.helper.IntegrationTest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author dawna.floyd
 */
public class BaseTest extends IntegrationTest {
      public  Validator validator;

    public BaseTest() {
        super();
    }
        
    
     public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

     public void tearDown() {

    }

}
