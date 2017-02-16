/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventHurricane;
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
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URISyntaxException;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author dawna.floyd
 */
public class EventHurricansValidationTest extends BaseTest {


    public EventHurricansValidationTest() {
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
        EventHurricane event = new EventHurricane();
        event.setId("SomeId");
        event.setPubdate(new Date());

 
        
        event.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventHurricane>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws Exception {
         ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleHurricanesEvent.json").toURI());
 
            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
           EventHurricane event = mapper.readValue(jsonEvent.toString(), EventHurricane.class);

         assertEquals(event.getObjectid().longValue(),jsonEvent.get("OBJECTID").asLong());
         assertEquals(event.getName(),jsonEvent.get("name").asText());
         assertEquals(event.getType(),jsonEvent.get("type").asText());
         assertEquals(event.getId(),jsonEvent.get("id").asText());
         assertEquals(event.getMovement(),jsonEvent.get("movement").asText());
         assertEquals(event.getWind(),jsonEvent.get("wind").asText());
         assertEquals(event.getLink(),jsonEvent.get("link").asText());
         assertEquals(event.getPubdate().getTime(), jsonEvent.get("pubdate").asLong());


    }
    @Test
    public void testFieldLengthValidations() throws Exception {
        EventHurricane testEvent = new EventHurricane();
        testEvent.setId("0123456789001234567890");
        testEvent.setLink("01234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setName("0123456789001234567890ABC");
        testEvent.setType("123450123456789001234567890ABC");
        testEvent.setMovement("01234567890012345678900123456789001234567890");
        testEvent.setWind("0123456789001234567890");
        testEvent.setPubdate(new Date());

        testEvent.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventHurricane>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(6);
        
        
    }
}
