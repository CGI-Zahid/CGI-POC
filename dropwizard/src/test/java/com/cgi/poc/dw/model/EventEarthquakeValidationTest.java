/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventEarthquakePK;
import com.cgi.poc.dw.dao.model.EventEarthquake;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.FileReader;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.validator.internal.engine.path.PathImpl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void testExampleFromSource() throws Exception {
         ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleQuakeEvent.json").toURI());
             JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode event = node.get("attributes");
            JsonNode geo = node.get("attributes");
 
           EventEarthquake tst = mapper.readValue(event.toString(), EventEarthquake.class);
            
           
         assertEquals(tst.getEventEarthquakePK().getEqid(),event.get("eqid").asText());
         assertEquals(tst.getEventEarthquakePK().getDatetime().getTime(),event.get("datetime").asLong());
         assertNull(tst.getDepth());
         assertEquals(tst.getLatitude().setScale(20), BigDecimal.valueOf( event.get("latitude").asDouble()).setScale(20));
         assertEquals(tst.getLongitude().setScale(20), BigDecimal.valueOf( event.get("longitude").asDouble()).setScale(20));

         assertEquals(tst.getObjectid().longValue(),event.get("objectid").asLong());
         assertEquals(tst.getMagnitude().setScale(2),BigDecimal.valueOf(  event.get("magnitude").asDouble()).setScale(2));
         assertEquals(tst.getNumstations().longValue(),event.get("numstations").asLong());
         assertEquals(tst.getRegion(),event.get("region").asText());
         assertEquals(tst.getSource(),event.get("source").asText());
         assertNull(tst.getVersion());
 
         
 
    }
    @Test
    public void testFieldLengthValidations() throws  Exception {
        EventEarthquakePK eventEarthquakePK = new EventEarthquakePK();
        EventEarthquake event = new EventEarthquake();
        eventEarthquakePK.setEqid("1234");
        eventEarthquakePK.setDatetime(new Date());
        event.setEventEarthquakePK(eventEarthquakePK);
        event.setRegion("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        event.setSource("123456789012345678912345678901234567891234567890123456789123456789012345678912345678901234567891234567890123456789");
        event.setVersion("123456789012345678912345678901234567891234567890123456789123456789012345678912345678901234567891234567890123456789");
 
        event.setGeometry(createTestGeo());
        
        Set<ConstraintViolation<EventEarthquake>> validate = validator.validate(event);
        
        assertThat(validate.size()).isEqualTo(3);
        
        
    }
}
