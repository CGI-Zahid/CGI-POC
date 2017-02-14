/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.helper.IntegrationTest;
import java.io.File;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author dawna.floyd
 */
public class FireEventValidationTest extends IntegrationTest {

    Validator validator;

    public FireEventValidationTest() {

    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @After
    public void tearDown() {

    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testValidFireEventMinValid() {
        FireEvent testEvent = new FireEvent();
        testEvent.setUniquefireidentifier("onlyReqField");
        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        /*
        "geometry": {
           "x": -10677457.159137897,
           "y": 4106537.9944933983
         }
         */
        testEvent.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<FireEvent>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws IOException, ParseException {
         JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sameEvents/exampleFireEvent.json").getFile());
        
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject event = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
           FireEvent tst = mapper.readValue(event.toString(), FireEvent.class);
           //tst.setGeometry(geo.toString());  
           
         assertEquals(tst.getUniquefireidentifier(),event.get("uniquefireidentifier"));
         assertEquals(tst.getIncidentname(),event.get("incidentname"));
         assertEquals(tst.getHotlink(),event.get("hotlink"));
         assertEquals(tst.getStatus(),event.get("status"));
         assertEquals(tst.getObjectid().longValue(),event.get("objectid"));
         assertEquals(tst.getReportdatetime().getTime(),event.get("reportdatetime"));

 
    }
    @Test
    public void testFieldLengthValidations() throws IOException, ParseException {
                FireEvent testEvent = new FireEvent();
        testEvent.setUniquefireidentifier("");
        testEvent.setIncidentname("123456789012345678901234567890");
        testEvent.setHotlink("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setStatus("123");
        testEvent.setState("1123");
        testEvent.setIscomplex("1123");
        testEvent.setComplexparentirwinid("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setFirecause("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setPooresponsibleunit("1234567890");
        testEvent.setMapsymbol("1234567890");
        testEvent.setPooownerunit("1234567890");
        testEvent.setOwneragency("123456789012345678901234567890");
        testEvent.setLocalincidentidentifier("123456789012345678901234567890");
        testEvent.setIncidenttypecategory("123456789012345678901234567890");
        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        /*
        "geometry": {
           "x": -10677457.159137897,
           "y": 4106537.9944933983
         }
         */
        testEvent.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<FireEvent>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(14);
        
        
    }
}
