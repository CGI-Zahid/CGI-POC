/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventVolcano;
import com.fasterxml.jackson.core.JsonParser;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Date;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author dawna.floyd
 */
public class EventVolcanoValidationTest extends BaseTest {


    public EventVolcanoValidationTest() {
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
    public void testValidEventWeatherMinValid() {
        // Best guess is that the msgType an ProdType will always be there
        // otherwise... no idea how to tell what it's for
        EventVolcano event = new EventVolcano();
        event.setId("SomeId");
        event.setPubdate(new Date());
 
        
        event.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventVolcano>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws  Exception {
         ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleVolcanoEvent.json").toURI());
         
            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
            
           EventVolcano event = mapper.readValue(jsonEvent.toString(), EventVolcano.class);

         assertEquals(event.getPubdate().getTime(), jsonEvent.get("pubdate").asLong());
         assertEquals(event.getObjectid().longValue(),jsonEvent.get("OBJECTID").asLong());
         assertEquals(event.getId(),jsonEvent.get("id").asText());
         assertEquals(event.getLink(),jsonEvent.get("link").asText());
         assertEquals(event.getAlert(),jsonEvent.get("alert").asText());
         assertEquals(event.getColor(),jsonEvent.get("color").asText());
         assertEquals(event.getDescrpt(),jsonEvent.get("descrpt").asText());


    }
    @Test
    public void testFieldLengthValidations() throws  Exception {
        EventVolcano testEvent = new EventVolcano();
        testEvent.setId("0123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setLink("01234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setAlert("0123456789001234567890");
        testEvent.setColor("0123456789001234567890");
        testEvent.setDescrpt("012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setPubdate(new Date());
 
 
        testEvent.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventVolcano>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(5);
        
        
    }
}
