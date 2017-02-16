/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.helper.IntegrationTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory jsonFactory;

    public BaseTest() {
        super();
                jsonFactory = new JsonFactory();

    }
        
    
     public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

     public void tearDown() {

    }
     
    public String createTestGeo() {
        ObjectNode geo = mapper.createObjectNode();
        ObjectNode ele = mapper.createObjectNode();
        geo.set("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        return geo.asText();
    }

}
