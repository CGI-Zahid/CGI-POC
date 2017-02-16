/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventWeather;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        
        event.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<EventWeather>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws IOException, ParseException, URISyntaxException {
         JSONParser parser = new JSONParser();
        ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleWeatherEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject event = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
           
           EventWeather tst = mapper.readValue(event.toString(), EventWeather.class);

         assertEquals(tst.getObjectid().longValue(),event.get("objectid"));
         assertEquals(tst.getPhenom(),event.get("phenom"));
         assertEquals(tst.getSig(),event.get("sig"));
         assertEquals(tst.getWarnid(),event.get("warnid"));
         assertEquals(tst.getWfo(),event.get("wfo"));
         assertEquals(tst.getEvent(),event.get("event"));
         assertEquals(tst.getIssuance(),event.get("issuance"));
         assertEquals(tst.getExpiration(),event.get("expiration"));
         assertEquals(tst.getUrl(),event.get("url"));
         assertEquals(tst.getMsgType(),event.get("msg_type"));
         assertEquals(tst.getProdType(),event.get("prod_type"));
         assertEquals(tst.getIdpSource(),event.get("idp_source"));
         assertEquals(tst.getIdpSubset(),event.get("idp_subset"));
         assertEquals(tst.getIdpFiledate().getTime(),event.get("idp_filedate"));
         assertEquals(tst.getIdpIngestdate().getTime(),event.get("idp_ingestdate"));
         assertEquals(tst.getIdpCurrentForecast(),event.get("idp_current_forecast"));
         assertEquals(tst.getIdpTimeSeries(),event.get("idp_time_series"));
         if (event.get("idp_issueddate") == null){
             assertNull(tst.getIdpIssueddate());
         }else
         {
             assertEquals(tst.getIdpIssueddate().getTime(),event.get("idp_issueddate"));
         }
         if (event.get("idp_validtime") == null){
             assertNull(tst.getIdpValidtime());
         }else
         {
            assertEquals(tst.getIdpValidtime().getTime(),event.get("idp_validtime"));
         }
         if (event.get("idp_validendtime") == null){
             assertNull(tst.getIdpValidendtime());
         }else
         {
         assertEquals(tst.getIdpValidendtime().getTime(),event.get("idp_validendtime"));
         }
         assertEquals(tst.getIdpFcstHour(),event.get("idp_fcst_hour"));
         assertEquals(tst.getStArea().setScale(20),BigDecimal.valueOf( (double)event.get("st_area(shape)")).setScale(20));
         assertEquals(tst.getStLength().setScale(20),BigDecimal.valueOf( (double)event.get("st_length(shape)")).setScale(20));
    }
    @Test
    public void testFieldLengthValidations() throws IOException, ParseException {
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
 
        
        JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
 
        testEvent.setGeometry(geo.toJSONString());
        Set<ConstraintViolation<EventWeather>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(11);
        
        
    }
}
