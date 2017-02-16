/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventHurricane;
import com.cgi.poc.dw.dao.model.EventWeather;
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
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        
        event.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<EventHurricane>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws IOException, ParseException, URISyntaxException {
         JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleHurricanesEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject jsonEvent = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
            
           EventHurricane event = mapper.readValue(jsonEvent.toString(), EventHurricane.class);

         assertEquals(event.getObjectid().longValue(),jsonEvent.get("OBJECTID"));
         assertEquals(event.getName(),jsonEvent.get("name"));
         assertEquals(event.getType(),jsonEvent.get("type"));
         assertEquals(event.getId(),jsonEvent.get("id"));
         assertEquals(event.getMovement(),jsonEvent.get("movement"));
         assertEquals(event.getWind(),jsonEvent.get("wind"));
         assertEquals(event.getLink(),jsonEvent.get("link"));
         assertEquals(event.getPubdate().getTime(), jsonEvent.get("pubdate"));


    }
    @Test
    public void testFieldLengthValidations() throws IOException, ParseException {
        EventHurricane testEvent = new EventHurricane();
        testEvent.setId("0123456789001234567890");
        testEvent.setLink("01234567890012345678900123456789001234567890012345678900123456789001234567890012345678900123456789001234567890");
        testEvent.setName("0123456789001234567890ABC");
        testEvent.setType("123450123456789001234567890ABC");
        testEvent.setMovement("01234567890012345678900123456789001234567890");
        testEvent.setWind("0123456789001234567890");
        testEvent.setPubdate(new Date());

        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
 
        testEvent.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<EventHurricane>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(6);
        
        
    }
}
