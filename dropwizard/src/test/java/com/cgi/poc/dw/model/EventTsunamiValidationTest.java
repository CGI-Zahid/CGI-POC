/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.model;

import com.cgi.poc.dw.dao.model.EventTsunami;
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
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author dawna.floyd
 */
public class EventTsunamiValidationTest extends BaseTest {


    public EventTsunamiValidationTest() {
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
    public void testValidTsunamiEventMinValid() {
        // The only field we know is required is the ID
        EventTsunami event = new EventTsunami();
        event.setId(BigDecimal.valueOf(123));
 
        
        event.setGeometry(createTestGeo());
        Set<ConstraintViolation<EventTsunami>> validate = validator.validate(event);
        assertThat(validate.isEmpty()).isEqualTo(true);
    }

    /**
     * Test of min required fields on model
     */
    @Test
    public void testExampleFromSource() throws Exception {
          File file = new File(ClassLoader.getSystemResource("exampleTsunamiEvent.json").toURI());
         
            JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
            
           EventTsunami event = mapper.readValue(jsonEvent.toString(), EventTsunami.class);
         assertEquals(event.getId(),new BigDecimal(jsonEvent.get("ID").asLong()));
         assertEquals(event.getYear().intValue(), jsonEvent.get("YEAR").asInt() );
         assertEquals(event.getMonth().intValue(),jsonEvent.get("MONTH").asInt() );
         assertEquals(event.getDay().intValue(),jsonEvent.get("DAY").asInt() );
         assertEquals(event.getHour().intValue(),jsonEvent.get("HOUR").asInt() );
         assertEquals(event.getMinute().intValue(),jsonEvent.get("MINUTE").asInt() );
         assertEquals(event.getSecond().doubleValue(), jsonEvent.get("SECOND").asDouble(),0 );
         assertEquals(event.getDateString(),jsonEvent.get("DATE_STRING").asText());
         assertEquals(event.getLatitude().setScale(20), BigDecimal.valueOf( jsonEvent.get("LATITUDE").asDouble()).setScale(20));
         assertEquals(event.getLongitude().setScale(20), BigDecimal.valueOf( jsonEvent.get("LONGITUDE").asDouble()).setScale(20));

         assertEquals(event.getLocationName(),jsonEvent.get("LOCATION_NAME").asText());
         assertNull(event.getArea());
         assertEquals(event.getCountry(),jsonEvent.get("COUNTRY").asText());
         assertEquals(event.getRegionCode().intValue(),Long.valueOf(jsonEvent.get("REGION_CODE").asLong()).intValue());
         assertEquals(event.getRegion(),jsonEvent.get("REGION").asText());
         assertEquals(event.getCauseCode().intValue(),jsonEvent.get("CAUSE_CODE").asInt());
         assertEquals(event.getCause(),jsonEvent.get("CAUSE").asText());
         assertEquals(event.getEventValidityCode().intValue(),Long.valueOf(jsonEvent.get("EVENT_VALIDITY_CODE").asLong()).intValue());
         assertEquals(event.getEventValidity(),jsonEvent.get("EVENT_VALIDITY").asText());
         assertNull(event.getEqMagUnk());
         assertEquals(event.getEqMagMb().setScale(4), BigDecimal.valueOf(jsonEvent.get("EQ_MAG_MB").asDouble()).setScale(4));
         assertEquals(event.getEqMagMs().setScale(4), BigDecimal.valueOf( jsonEvent.get("EQ_MAG_MS").asDouble()).setScale(4));
         assertEquals(event.getEqMagMw().setScale(4), BigDecimal.valueOf( jsonEvent.get("EQ_MAG_MW").asDouble()).setScale(4));
         assertNull(event.getEqMagMl());
         assertNull(event.getEqMagMta());
         
         assertEquals(event.getEqMagnatude().setScale(4), BigDecimal.valueOf(  jsonEvent.get("EQ_MAGNITUDE").asDouble()).setScale(4));
         assertEquals(event.getEqMagnatudeRank().setScale(4), BigDecimal.valueOf(  jsonEvent.get("EQ_MAGNITUDE_RANK").asDouble()).setScale(4));
         assertEquals(event.getEqDepth().intValue(),jsonEvent.get("EQ_DEPTH").asInt());
         assertEquals(event.getMaxEventRunup().setScale(4), BigDecimal.valueOf( jsonEvent.get("MAX_EVENT_RUNUP").asLong()).setScale(4));
         assertNull(event.getTsMtAbe());
         assertEquals(event.getTsMtIi().setScale(4), BigDecimal.valueOf(  jsonEvent.get("TS_MT_II").asDouble()).setScale(4));
         assertEquals(event.getTsIntensity().setScale(4), BigDecimal.valueOf(  jsonEvent.get("TS_INTENSITY").asDouble()).setScale(4));
         assertNull(event.getDamageMillionsDollars());
         assertEquals(event.getDamageAmountOrder().intValue(),jsonEvent.get("DAMAGE_AMOUNT_ORDER").asInt());
         assertEquals(event.getDamageDescription(),jsonEvent.get("DAMAGE_DESCRIPTION").asText());
         assertNull(event.getHousesDestroyed());
         assertNull(event.getHousesAmountOrder());
         assertNull(event.getHousesDescription());
         assertEquals(event.getDeaths().intValue(),jsonEvent.get("DEATHS").asInt());
         assertEquals(event.getDeathsAmountOrder().intValue(), jsonEvent.get("DEATHS_AMOUNT_ORDER").asInt());
         assertEquals(event.getDeathsDescription(),jsonEvent.get("DEATHS_DESCRIPTION").asText());
         assertEquals(event.getInjuries().intValue(), jsonEvent.get("INJURIES").asInt());
         assertEquals(event.getInjuriesAmountOrder().intValue(), jsonEvent.get("INJURIES_AMOUNT_ORDER").asInt());
         assertEquals(event.getInjuriesDescription(),jsonEvent.get("INJURIES_DESCRIPTION").asText());
         assertNull(event.getMissing());
         assertNull(event.getMissingAmountOrder());
         assertNull(event.getMissingDescription());
         assertEquals(event.getComments(),jsonEvent.get("COMMENTS").asText());
         assertEquals(event.getNumRunup().intValue(), jsonEvent.get("NUM_RUNUP").asInt());
         assertNull(event.getDamageMillionsDollarsTotal());
         assertEquals(event.getDamageAmountOrderTotal().intValue(), jsonEvent.get("DAMAGE_AMOUNT_ORDER_TOTAL").asInt());
         assertEquals(event.getDamageTotalDescription(),jsonEvent.get("DAMAGE_TOTAL_DESCRIPTION").asText());
         assertNull(event.getHousesDestroyedTotal());
         assertNull(event.getHousesAmountOrderTotal());
         assertNull(event.getHousesTotalDescription());
         assertEquals(event.getDeathsTotal().intValue(),jsonEvent.get("DEATHS_TOTAL").asInt());
         assertEquals(event.getDeathsAmountOrderTotal().intValue(),jsonEvent.get("DEATHS_AMOUNT_ORDER_TOTAL").asInt());
         assertEquals(event.getDeathsTotalDescription(),jsonEvent.get("DEATHS_TOTAL_DESCRIPTION").asText());
         assertEquals(event.getInjuriesTotal().intValue(),jsonEvent.get("INJURIES_TOTAL").asInt());
         assertEquals(event.getInjuriesAmountOrderTotal().intValue(),jsonEvent.get("INJURIES_AMOUNT_ORDER_TOTAL").asInt());
         assertEquals(event.getInjuriesTotalDescription(),jsonEvent.get("INJURIES_TOTAL_DESCRIPTION").asText());
         assertNull(event.getMissingTotal());
         assertNull(event.getMissingAmountOrderTotal());
         assertNull(event.getMissingTotalDescription());
         assertEquals(event.getObjectid().intValue(),jsonEvent.get("OBJECTID").asInt());
         assertNull(event.getShape());
         assertEquals(event.getHousesDamaged().intValue(),jsonEvent.get("HOUSES_DAMAGED").asInt());
         assertEquals(event.getHousesDamagedAmountOrder().intValue(),jsonEvent.get("HOUSES_DAMAGED_AMOUNT_ORDER").asInt());
         assertEquals(event.getHousesDamDescription(),jsonEvent.get("HOUSES_DAM_DESCRIPTION").asText());
         assertEquals(event.getHousesDamagedTotal().intValue(),jsonEvent.get("HOUSES_DAMAGED_TOTAL").asInt());
         assertEquals(event.getHousesDamaAmountOrderTotal().intValue(),jsonEvent.get("HOUSES_DAM_AMOUNT_ORDER_TOTAL").asInt());
         assertEquals(event.getHousesDamTotalDescription(),jsonEvent.get("HOUSES_DAM_TOTAL_DESCRIPTION").asText());
         assertEquals(event.getNumDeposits().intValue(),jsonEvent.get("NUM_DEPOSITS").asInt());
         assertEquals(event.getUrl(),jsonEvent.get("URL").asText()); 
    }
    @Test
    public void testFieldLengthValidations() throws  Exception {
        EventTsunami testEvent = new EventTsunami();
        testEvent.setId(new BigDecimal(1));
        testEvent.setDateString("01234567890123456789");
        testEvent.setLocationName("01234567890123456789");
        testEvent.setArea("0123");
        testEvent.setCountry("012345678901234567890123456789012345678901234567890123456789");
         testEvent.setRegion("012345678901234567890123456789012345678901234567890123456789");
         testEvent.setCause("012345678901234567890123456789012345678901234567890123456789");
         testEvent.setEventValidity("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        testEvent.setDamageDescription("01234567890123456789012345678901234567890123456789");
        testEvent.setHousesDescription("01234567890123456789012345678901234567890123456789");
        testEvent.setDeathsDescription("01234567890123456789012345678901234567890123456789");
        testEvent.setInjuriesDescription("01234567890123456789012345678901234567890123456789");
        testEvent.setMissingDescription("01234567890123456789012345678901234567890123456789");
        String str = "01234567890";
        for (int i = 0; i <= 401 ;i++) {
             str = str.concat( "01234567890");
        }
        testEvent.setComments(str);
        testEvent.setDamageTotalDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setHousesTotalDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setDeathsTotalDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setMissingTotalDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setInjuriesTotalDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setHousesDamDescription("012345678901234567890123456789012345678901234567890123456789");
        testEvent.setHousesDamTotalDescription("012345678901234567890123456789012345678901234567890123456789");
 
 
        testEvent.setGeometry( createTestGeo());
        Set<ConstraintViolation<EventTsunami>> validate = validator.validate(testEvent);
        assertThat(validate.size()).isEqualTo(20);
        
        
    }
}
