/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventEarthquake;
import com.cgi.poc.dw.dao.model.EventEarthquakePK;
import com.cgi.poc.dw.dao.model.EventEarthquake;
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
import io.dropwizard.testing.ResourceHelpers;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.internal.engine.path.PathImpl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author dawna.floyd
 */
public class EventEarthquakeValidationTest extends BaseTest {


    public EventEarthquakeValidationTest() {
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
     * Test of Nulls in unique key
     */
    @Test
    public void testUniqueKeyNull() {

        EventEarthquake testEvent = new EventEarthquake();
        
        Set<ConstraintViolation<EventEarthquake>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(false);
        assertThat(validate.size()).isEqualTo(3);
        for ( ConstraintViolation violation : validate ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("eqid")){
               assertThat(tmp).isEqualTo("eqid");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("geometry")){
               assertThat(tmp).isEqualTo("geometry");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("datetime")){
               assertThat(tmp).isEqualTo("datetime");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }
              
              else {
                  fail("not an expected constraint violation");
              }

        }

        
    }
    /**
     * Test of Nulls in unique key
     */
    @Test
    public void testEqIdSizeInvalid() {

        EventEarthquake testEvent = new EventEarthquake();
        testEvent.setEqid("1234567890123456789012345678901234567890123456789012345678901234567890");
        
        Set<ConstraintViolation<EventEarthquake>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(false);
        assertThat(validate.size()).isEqualTo(3);
        for ( ConstraintViolation violation : validate ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("eqid")){
               assertThat(tmp).isEqualTo("eqid");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.Size.message}");
                  
              }else if (tmp.equals("geometry")){
               assertThat(tmp).isEqualTo("geometry");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }else if (tmp.equals("datetime")){
               assertThat(tmp).isEqualTo("datetime");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }
              
              else {
                  fail("not an expected constraint violation");
              }

        }

        
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws IOException, ParseException, URISyntaxException {
         JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleQuakeEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject event = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
           EventEarthquake tst = mapper.readValue(event.toString(), EventEarthquake.class);
           
         assertEquals(tst.getEventEarthquakePK().getEqid(),event.get("eqid"));
         assertEquals(tst.getEventEarthquakePK().getDatetime().getTime(),event.get("datetime"));
         assertEquals(tst.getDepth(),event.get("depth"));
         assertEquals(tst.getLatitude().setScale(20), BigDecimal.valueOf( (double)event.get("latitude")).setScale(20));
         assertEquals(tst.getLongitude().setScale(20), BigDecimal.valueOf( (double)event.get("longitude")).setScale(20));

         assertEquals(tst.getObjectid().longValue(),event.get("objectid"));
         assertEquals(tst.getMagnitude().setScale(2),BigDecimal.valueOf( (double)event.get("magnitude")).setScale(2));
         assertEquals(tst.getNumstations().longValue(),event.get("numstations"));
         assertEquals(tst.getRegion(),event.get("region"));
         assertEquals(tst.getSource(),event.get("source"));
 
         
 
    }
    @Test
    public void testFieldLengthValidations() throws IOException, ParseException {
        EventEarthquakePK eventEarthquakePK = new EventEarthquakePK();
        EventEarthquake event = new EventEarthquake();
        eventEarthquakePK.setEqid("1234");
        eventEarthquakePK.setDatetime(new Date());
        event.setEventEarthquakePK(eventEarthquakePK);
        event.setRegion("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        event.setSource("123456789012345678912345678901234567891234567890123456789123456789012345678912345678901234567891234567890123456789");
        event.setVersion("123456789012345678912345678901234567891234567890123456789123456789012345678912345678901234567891234567890123456789");
 
                
        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        
        event.setGeometry(geo.toJSONString());
        
        Set<ConstraintViolation<EventEarthquake>> validate = validator.validate(event);
        
        assertThat(validate.size()).isEqualTo(3);
        
        
    }
}
