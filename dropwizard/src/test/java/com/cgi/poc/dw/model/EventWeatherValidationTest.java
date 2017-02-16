/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventWeather;
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
public class EventWeatherValidationTest extends BaseTest {


    public EventWeatherValidationTest() {
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
        EventWeather event = new EventWeather();
        event.setWarnid("Uniq1");
        event.setMsgType("FFS");
        event.setProdType("Flash Flood Warning");

        event.setGeometry(createTestGeo());

        Set<ConstraintViolation<EventWeather>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws  Exception {
         ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleWeatherEvent.json").toURI());
         
         
            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode event = node.get("attributes");
            JsonNode geo = node.get("attributes");
            
           
           EventWeather tst = mapper.readValue(event.toString(), EventWeather.class);

         assertEquals(tst.getObjectid().longValue(),event.get("objectid").asLong());
         assertEquals(tst.getPhenom(),event.get("phenom").asText());
         assertEquals(tst.getSig(),event.get("sig").asText());
         assertEquals(tst.getWarnid(),event.get("warnid").asText());
         assertEquals(tst.getWfo(),event.get("wfo").asText());
         assertEquals(tst.getEvent(),event.get("event").asText());
         assertEquals(tst.getIssuance(),event.get("issuance").asText());
         assertEquals(tst.getExpiration(),event.get("expiration").asText());
         assertEquals(tst.getUrl(),event.get("url").asText());
         assertEquals(tst.getMsgType(),event.get("msg_type").asText());
         assertEquals(tst.getProdType(),event.get("prod_type").asText());
         assertEquals(tst.getIdpSource(),event.get("idp_source").asText());
         assertEquals(tst.getIdpSubset(),event.get("idp_subset").asText());
         assertEquals(tst.getIdpFiledate().getTime(),event.get("idp_filedate").asLong());
         assertEquals(tst.getIdpIngestdate().getTime(),event.get("idp_ingestdate").asLong());
             assertNull(tst.getIdpTimeSeries());
             assertNull(tst.getIdpCurrentForecast());
 
         assertNull(tst.getIdpIssueddate());
         
         assertNull(tst.getIdpValidtime());
         
         assertNull(tst.getIdpValidendtime());
         assertNull(tst.getIdpFcstHour());
         
         assertEquals(tst.getStArea().setScale(20),BigDecimal.valueOf(  event.get("st_area(shape)").asDouble()).setScale(20));
         assertEquals(tst.getStLength().setScale(20),BigDecimal.valueOf(  event.get("st_length(shape)").asDouble()).setScale(20));
    }
    @Test
    public void testFieldLengthValidations() throws  Exception {
                EventWeather testEvent = new EventWeather();
        testEvent.setWarnid("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setPhenom("12345");
        testEvent.setSig("12345");
        testEvent.setWfo("12345");
        testEvent.setIssuance("123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setExpiration("123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setUrl("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testEvent.setMsgType("12345");
        testEvent.setProdType("123456789012345678901234567890123456789012345678901234567890123456789123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setIdpSource("123456789012345678901234567890123456789012345678901234567890123456789123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setIdpSubset("123456789012345678901234567890123456789012345678901234567890123456789123456789012345678901234567890123456789012345678901234567890123456789");
 
 
 
        testEvent.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventWeather>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(11);
        
        
    }
}
