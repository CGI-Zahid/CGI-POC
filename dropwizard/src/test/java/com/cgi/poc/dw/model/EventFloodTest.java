/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventFlood;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.validator.internal.engine.path.PathImpl;
 
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dawna.floyd
 */
public class EventFloodTest extends BaseTest {
    
    public EventFloodTest() {
                super();

    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
               super.setUp();

    }
    
    @After
    public void tearDown() {
               super.tearDown();
    }

    /**
     * Test of getEventFloodPK method, of class EventFlood.
     */
    @Test
    public void testGetEventFloodPKNull() {
        System.out.println("getEventFloodPK");
        EventFlood testEvent = new EventFlood();
        Set<ConstraintViolation<EventFlood>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(false);
        assertThat(validate.size()).isEqualTo(3);
        for ( ConstraintViolation violation : validate ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("waterbody")){
               assertThat(tmp).isEqualTo("waterbody");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("geometry")){
               assertThat(tmp).isEqualTo("geometry");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("obstime")){
               assertThat(tmp).isEqualTo("obstime");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }
              
              else {
                  fail("not an expected constraint violation");
              }

        }

    }
    @Test
    public void testPKSizeInvalid() {
        System.out.println("testPKSizeInvalid");
        EventFlood testEvent = new EventFlood();
        // 1- 255
        testEvent.setWaterbody("");
        Set<ConstraintViolation<EventFlood>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(false);
        assertThat(validate.size()).isEqualTo(3);
        for ( ConstraintViolation violation : validate ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("waterbody")){
               assertThat(tmp).isEqualTo("waterbody");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.Size.message}");
                  
              }else if (tmp.equals("geometry")){
               assertThat(tmp).isEqualTo("geometry");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("obstime")){
               assertThat(tmp).isEqualTo("obstime");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }
              
              else {
                  fail("not an expected constraint violation");
              }

        }
    }
        @Test
    public void testExampleFromSource() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleFloodEvent.json").toURI());

            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode event = node.get("attributes");
            JsonNode geo = node.get("attributes");
 

           EventFlood tst = mapper.readValue(event.toString(), EventFlood.class);
           
           
           
         assertEquals(tst.getWaterbody(),event.get("waterbody").asText());
         assertEquals(tst.getObstime(),event.get("obstime").asText());
         assertEquals(tst.getObjectid().longValue(),event.get("objectid").asLong());
         // have to hard code this.. b/c the source if event is weird... 
         assertEquals(tst.getLatitude().setScale(20), new BigDecimal("3.1996666999999999" ).setScale(20));
         // have to hard code this.. b/c the source if event is weird... 
         assertEquals(tst.getLongitude().setScale(20), new BigDecimal("-8.3279167000000001").setScale(20));
         assertEquals(tst.getState(),event.get("state").asText());
         assertEquals(tst.getIdpSource(),event.get("idp_source").asText());
         assertEquals(tst.getIdpSubset(),event.get("idp_subset").asText());
         assertNull(tst.getShape()); // the atual data coming in doesn't seem to have this field.
         assertEquals(tst.getGaugelid(),event.get("gaugelid").asText());
         assertEquals(tst.getLocation(),event.get("location").asText());
         assertEquals(tst.getObserved(),event.get("observed").asText());
         assertEquals(tst.getUnits(),event.get("units").asText());
         assertEquals(tst.getAction(),event.get("action").asText());
         assertEquals(tst.getFlood(),event.get("flood").asText());
         assertEquals(tst.getModerate(),event.get("moderate").asText());
         assertEquals(tst.getMajor(),event.get("major").asText());
         assertEquals(tst.getLowthresh(),event.get("lowthresh").asText());
         assertEquals(tst.getLowthreshu(),event.get("lowthreshu").asText());
         assertEquals(tst.getWfo(),event.get("wfo").asText());
         assertEquals(tst.getHdatum(),event.get("hdatum").asText());
         assertEquals(tst.getPedts(),event.get("pedts").asText());
         assertEquals(tst.getSecvalue(),event.get("secvalue").asText());
         assertEquals(tst.getSecunit(),event.get("secunit").asText());
         assertEquals(tst.getUrl(),event.get("url").asText());
         assertEquals(tst.getStatus(),event.get("status").asText());
         assertNull(tst.getForecast());

         
         
 
    }
    @Test
    public void testFieldLengthValidations() throws Exception {
        EventFlood testEvent = new EventFlood();
        // 1- 255
        testEvent.setWaterbody("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");        
        testEvent.setObstime("012345678901234567890123456789");
        testEvent.setState("123");
        testEvent.setIdpSource("012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setIdpSubset("012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setGaugelid("123456");
        testEvent.setLocation("012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setObserved("012345678900123456789001234567890");
        testEvent.setUnits("123456");
        testEvent.setAction("012345678900123456789001234567890");
        testEvent.setFlood("012345678900123456789001234567890");
        testEvent.setModerate("012345678900123456789001234567890");
        testEvent.setMajor("012345678900123456789001234567890");
        testEvent.setLowthresh("012345678900123456789001234567890");
        testEvent.setLowthreshu("123456");
        testEvent.setWfo("123456");
        testEvent.setHdatum("0123456789012345678901234567890123456789");
        testEvent.setPedts("123456");
        testEvent.setSecvalue("0123456789012345678901234567890123456789");
        testEvent.setSecunit("123456");
        testEvent.setUrl("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setStatus("012345678901234567890123456789");
        testEvent.setForecast("012345678901234567890123456789");
        
 
        testEvent.setGeometry(createTestGeo());
        
        
        Set<ConstraintViolation<EventFlood>> validate = validator.validate(testEvent);
         
        assertThat(validate.size()).isEqualTo(23);
        
        
    }
 
}
