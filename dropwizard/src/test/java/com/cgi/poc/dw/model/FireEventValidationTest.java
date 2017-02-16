/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.util.Set;
import javax.validation.ConstraintViolation;
import static org.assertj.core.api.Assertions.assertThat;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author dawna.floyd
 */
public class FireEventValidationTest extends BaseTest {

 
    public FireEventValidationTest() {
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
     * Test of min required fields on model
     */
    @Test
    public void testValidFireEventMinValid() {
        FireEvent testEvent = new FireEvent();
        testEvent.setUniquefireidentifier("onlyReqField");
        testEvent.setGeometry(createTestGeo());

        Set<ConstraintViolation<FireEvent>> validate = validator.validate(testEvent);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws Exception {
         ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleFireEvent.json").toURI());
         
          
            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode event = node.get("attributes");
            JsonNode geo = node.get("attributes");
            
           FireEvent tst = mapper.readValue(event.toString(), FireEvent.class);
           //tst.setGeometry(geo.toString());  
           
         assertEquals(tst.getUniquefireidentifier(),event.get("uniquefireidentifier").asText());
         assertEquals(tst.getIncidentname(),event.get("incidentname").asText());
         assertEquals(tst.getHotlink(),event.get("hotlink").asText());
         assertEquals(tst.getStatus(),event.get("status").asText());
         assertEquals(tst.getObjectid().longValue(),event.get("objectid").asLong());
         assertEquals(tst.getReportdatetime().getTime(),event.get("reportdatetime").asLong());
         assertEquals(tst.getLatitude().setScale(20), BigDecimal.valueOf( event.get("latitude").asDouble()).setScale(20));
         assertEquals(tst.getLongitude().setScale(20), BigDecimal.valueOf( event.get("longitude").asDouble()).setScale(20));
         assertEquals(tst.getAcres().longValue(),event.get("acres").asLong());
         assertEquals(tst.getGacc(),event.get("gacc").asText());
         assertEquals(tst.getState(),event.get("state").asText());
         assertEquals(tst.getIrwinid(),event.get("irwinid").asText());
         assertEquals(tst.getIscomplex(),event.get("iscomplex").asText());
         assertEquals(tst.getIscomplex(),event.get("iscomplex").asText());
         assertNull(tst.getComplexparentirwinid());
         assertEquals(tst.getFirecause(),event.get("firecause").asText());
         assertEquals(tst.getReportdatetime().getTime(),event.get("reportdatetime").asLong());
         assertEquals(tst.getPercentcontained().intValue(),event.get("percentcontained").asInt());
         assertEquals(tst.getFirediscoverydatetime().getTime(),event.get("firediscoverydatetime").asLong());
          assertEquals(tst.getPooresponsibleunit(),event.get("pooresponsibleunit").asText());
         assertEquals(tst.getIrwinmodifiedon().getTime(),event.get("irwinmodifiedon").asLong());
         assertEquals(tst.getMapsymbol(),event.get("mapsymbol").asText());
         assertEquals(tst.getDatecurrent().getTime(),event.get("datecurrent").asLong());
         assertNull(tst.getPooownerunit());
         assertNull(tst.getOwneragency());
         assertNull(tst.getFireyear());
         assertNull(tst.getLocalincidentidentifier());
         assertNull(tst.getIncidenttypecategory());
 
 
    }
    @Test
    public void testFieldLengthValidations() throws  Exception {
                FireEvent testEvent = new FireEvent();
        testEvent.setUniquefireidentifier("");
        testEvent.setIncidentname("123456789012345678901234567890");
        testEvent.setHotlink("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setStatus("123");
        testEvent.setState("1123");
        testEvent.setIscomplex("123456789012345678901234567890");
        testEvent.setComplexparentirwinid("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setFirecause("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setPooresponsibleunit("1234567890");
        testEvent.setMapsymbol("1234567890");
        testEvent.setPooownerunit("1234567890");
        testEvent.setOwneragency("123456789012345678901234567890");
        testEvent.setLocalincidentidentifier("123456789012345678901234567890");
        testEvent.setIncidenttypecategory("123456789012345678901234567890");
       
        
        testEvent.setGeometry(this.createTestGeo());
        Set<ConstraintViolation<FireEvent>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(14);
        
        
    }
}
