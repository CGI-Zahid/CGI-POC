/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventTsunami;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.hibernate.validator.internal.engine.path.PathImpl;
import static org.assertj.core.api.Assertions.assertThat;
 
 
/**
 *
 * @author dawna.floyd
 */
 
public class EventTsunamiDAOTest extends DaoUnitTestBase  {
    
 
    EventTsunamiDAO eventDAO;
     
     public EventTsunamiDAOTest() {
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
        eventDAO = new EventTsunamiDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class FireEventDAO.
     */
    @Test
    public void testCRUD() throws Exception {
         File file = new File(ClassLoader.getSystemResource("exampleTsunamiEvent.json").toURI());
         
         
             JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
           
           EventTsunami event = mapper.readValue(jsonEvent.toString(), EventTsunami.class);



        event.setGeometry(geo.toString());
        EventTsunami result = eventDAO.save(event);
         flush(); // have to do this.. so that the sql is actually executed.
        Set<ConstraintViolation<EventTsunami>> errors = validator.validate(result);
         
         assertEquals(result.getId(),event.getId());
         assertEquals(result.getYear(),event.getYear());
         assertEquals(result.getMonth(),event.getMonth());
         assertEquals(result.getDay(),event.getDay());
         assertEquals(result.getHour(),event.getHour());
         assertEquals(result.getMinute(),event.getMinute());
         assertEquals(result.getSecond(),event.getSecond());
         assertEquals(result.getDateString(),event.getDateString());
         assertEquals(result.getLatitude(),event.getLatitude());
         assertEquals(result.getLongitude(),event.getLongitude());
         assertEquals(result.getLocationName(),event.getLocationName());
         assertEquals(result.getArea(),event.getArea());
         assertEquals(result.getCountry(),event.getCountry());
         assertEquals(result.getRegionCode(),event.getRegionCode());
         assertEquals(result.getRegion(),event.getRegion());
         assertEquals(result.getCauseCode(),event.getCauseCode());
         assertEquals(result.getCause(),event.getCause());
         assertEquals(result.getEventValidityCode(),event.getEventValidityCode());
         assertEquals(result.getEventValidity(),event.getEventValidity());
         assertEquals(result.getEqMagUnk(),event.getEqMagUnk());
         assertEquals(result.getEqMagMb(),event.getEqMagMb());
         assertEquals(result.getEqMagMs(),event.getEqMagMs());
         assertEquals(result.getEqMagMw(),event.getEqMagMw());
         assertEquals(result.getEqMagMl(),event.getEqMagMl());
         assertEquals(result.getEqMagMta(),event.getEqMagMta());
         assertEquals(result.getEqMagnatude(),event.getEqMagnatude());
         assertEquals(result.getEqMagnatudeRank(),event.getEqMagnatudeRank());
         assertEquals(result.getEqDepth(),event.getEqDepth());
         assertEquals(result.getMaxEventRunup(),event.getMaxEventRunup());
         assertEquals(result.getTsMtAbe(),event.getTsMtAbe());
         assertEquals(result.getTsMtIi(),event.getTsMtIi());
         assertEquals(result.getTsIntensity(),event.getTsIntensity());
         assertEquals(result.getDamageMillionsDollars(),event.getDamageMillionsDollars());
         assertEquals(result.getDamageAmountOrder(),event.getDamageAmountOrder());
         assertEquals(result.getDamageDescription(),event.getDamageDescription());
         assertEquals(result.getHousesDestroyed(),event.getHousesDestroyed());
         assertEquals(result.getHousesAmountOrder(),event.getHousesAmountOrder());
         assertEquals(result.getHousesDescription(),event.getHousesDescription());
         assertEquals(result.getDeaths(),event.getDeaths());
         assertEquals(result.getDeathsAmountOrder(),event.getDeathsAmountOrder());
         assertEquals(result.getDeathsDescription(),event.getDeathsDescription());
         assertEquals(result.getInjuries(),event.getInjuries());
         assertEquals(result.getInjuriesAmountOrder(),event.getInjuriesAmountOrder());
         assertEquals(result.getInjuriesDescription(),event.getInjuriesDescription());
         assertEquals(result.getMissing(),event.getMissing());
         assertEquals(result.getMissingAmountOrder(),event.getMissingAmountOrder());
         assertEquals(result.getMissingDescription(),event.getMissingDescription());
         assertEquals(result.getComments(),event.getComments());
         assertEquals(result.getNumRunup(),event.getNumRunup());
         assertEquals(result.getDamageMillionsDollarsTotal(),event.getDamageMillionsDollarsTotal());
         assertEquals(result.getDamageAmountOrderTotal(),event.getDamageAmountOrderTotal());
         assertEquals(result.getDamageTotalDescription(),event.getDamageTotalDescription());
         assertEquals(result.getHousesDestroyedTotal(),event.getHousesDestroyedTotal());
         assertEquals(result.getHousesDestroyedTotal(),event.getHousesDestroyedTotal());
         assertEquals(result.getHousesAmountOrderTotal(),event.getHousesAmountOrderTotal());
         assertEquals(result.getHousesTotalDescription(),event.getHousesTotalDescription());
         assertEquals(result.getDeathsTotal(),event.getDeathsTotal());
         assertEquals(result.getDeathsAmountOrderTotal(),event.getDeathsAmountOrderTotal());
         assertEquals(result.getDeathsTotalDescription(),event.getDeathsTotalDescription());
         assertEquals(result.getInjuriesTotal(),event.getInjuriesTotal());
         assertEquals(result.getInjuriesAmountOrderTotal(),event.getInjuriesAmountOrderTotal());
         assertEquals(result.getInjuriesTotalDescription(),event.getInjuriesTotalDescription());
         assertEquals(result.getMissingTotal(),event.getMissingTotal());
         assertEquals(result.getMissingAmountOrderTotal(),event.getMissingAmountOrderTotal());
         assertEquals(result.getMissingTotalDescription(),event.getMissingTotalDescription());
         assertEquals(result.getObjectid(),event.getObjectid());
         assertEquals(result.getShape(),event.getShape());
         assertEquals(result.getHousesDamaged(),event.getHousesDamaged());
         assertEquals(result.getHousesDamagedAmountOrder(),event.getHousesDamagedAmountOrder());
         assertEquals(result.getHousesDamDescription(),event.getHousesDamDescription());
         assertEquals(result.getHousesDamagedTotal(),event.getHousesDamagedTotal());
         assertEquals(result.getHousesDamaAmountOrderTotal(),event.getHousesDamaAmountOrderTotal());
         assertEquals(result.getHousesDamTotalDescription(),event.getHousesDamTotalDescription());
         assertEquals(result.getNumDeposits(),event.getNumDeposits());
         assertEquals(result.getUrl(),event.getUrl()); 
         
        assertNotNull(result.getLastModified());
         compareGeoJson(event.getGeometry(),result.getGeometry());

         flush(); // have to do this.. so that the sql is actually executed.

 
        EventTsunami selectForUpdate = eventDAO.selectForUpdate(result);
         assertEquals(selectForUpdate.getId(),event.getId());
         assertEquals(selectForUpdate.getYear(),event.getYear());
         assertEquals(selectForUpdate.getMonth(),event.getMonth());
         assertEquals(selectForUpdate.getDay(),event.getDay());
         assertEquals(selectForUpdate.getHour(),event.getHour());
         assertEquals(selectForUpdate.getMinute(),event.getMinute());
         assertEquals(selectForUpdate.getSecond(),event.getSecond());
         assertEquals(selectForUpdate.getDateString(),event.getDateString());
         assertEquals(selectForUpdate.getLatitude(),event.getLatitude());
         assertEquals(selectForUpdate.getLongitude(),event.getLongitude());
         assertEquals(selectForUpdate.getLocationName(),event.getLocationName());
         assertEquals(selectForUpdate.getArea(),event.getArea());
         assertEquals(selectForUpdate.getCountry(),event.getCountry());
         assertEquals(selectForUpdate.getRegionCode(),event.getRegionCode());
         assertEquals(selectForUpdate.getRegion(),event.getRegion());
         assertEquals(selectForUpdate.getCauseCode(),event.getCauseCode());
         assertEquals(selectForUpdate.getCause(),event.getCause());
         assertEquals(selectForUpdate.getEventValidityCode(),event.getEventValidityCode());
         assertEquals(selectForUpdate.getEventValidity(),event.getEventValidity());
         assertEquals(selectForUpdate.getEqMagUnk(),event.getEqMagUnk());
         assertEquals(selectForUpdate.getEqMagMb(),event.getEqMagMb());
         assertEquals(selectForUpdate.getEqMagMs(),event.getEqMagMs());
         assertEquals(selectForUpdate.getEqMagMw(),event.getEqMagMw());
         assertEquals(selectForUpdate.getEqMagMl(),event.getEqMagMl());
         assertEquals(selectForUpdate.getEqMagMta(),event.getEqMagMta());
         assertEquals(selectForUpdate.getEqMagnatude(),event.getEqMagnatude());
         assertEquals(selectForUpdate.getEqMagnatudeRank(),event.getEqMagnatudeRank());
         assertEquals(selectForUpdate.getEqDepth(),event.getEqDepth());
         assertEquals(selectForUpdate.getMaxEventRunup(),event.getMaxEventRunup());
         assertEquals(selectForUpdate.getTsMtAbe(),event.getTsMtAbe());
         assertEquals(selectForUpdate.getTsMtIi(),event.getTsMtIi());
         assertEquals(selectForUpdate.getTsIntensity(),event.getTsIntensity());
         assertEquals(selectForUpdate.getDamageMillionsDollars(),event.getDamageMillionsDollars());
         assertEquals(selectForUpdate.getDamageAmountOrder(),event.getDamageAmountOrder());
         assertEquals(selectForUpdate.getDamageDescription(),event.getDamageDescription());
         assertEquals(selectForUpdate.getHousesDestroyed(),event.getHousesDestroyed());
         assertEquals(selectForUpdate.getHousesAmountOrder(),event.getHousesAmountOrder());
         assertEquals(selectForUpdate.getHousesDescription(),event.getHousesDescription());
         assertEquals(selectForUpdate.getDeaths(),event.getDeaths());
         assertEquals(selectForUpdate.getDeathsAmountOrder(),event.getDeathsAmountOrder());
         assertEquals(selectForUpdate.getDeathsDescription(),event.getDeathsDescription());
         assertEquals(selectForUpdate.getInjuries(),event.getInjuries());
         assertEquals(selectForUpdate.getInjuriesAmountOrder(),event.getInjuriesAmountOrder());
         assertEquals(selectForUpdate.getInjuriesDescription(),event.getInjuriesDescription());
         assertEquals(selectForUpdate.getMissing(),event.getMissing());
         assertEquals(selectForUpdate.getMissingAmountOrder(),event.getMissingAmountOrder());
         assertEquals(selectForUpdate.getMissingDescription(),event.getMissingDescription());
         assertEquals(selectForUpdate.getComments(),event.getComments());
         assertEquals(selectForUpdate.getNumRunup(),event.getNumRunup());
         assertEquals(selectForUpdate.getDamageMillionsDollarsTotal(),event.getDamageMillionsDollarsTotal());
         assertEquals(selectForUpdate.getDamageAmountOrderTotal(),event.getDamageAmountOrderTotal());
         assertEquals(selectForUpdate.getDamageTotalDescription(),event.getDamageTotalDescription());
         assertEquals(selectForUpdate.getHousesDestroyedTotal(),event.getHousesDestroyedTotal());
         assertEquals(selectForUpdate.getHousesDestroyedTotal(),event.getHousesDestroyedTotal());
         assertEquals(selectForUpdate.getHousesAmountOrderTotal(),event.getHousesAmountOrderTotal());
         assertEquals(selectForUpdate.getHousesTotalDescription(),event.getHousesTotalDescription());
         assertEquals(selectForUpdate.getDeathsTotal(),event.getDeathsTotal());
         assertEquals(selectForUpdate.getDeathsAmountOrderTotal(),event.getDeathsAmountOrderTotal());
         assertEquals(selectForUpdate.getDeathsTotalDescription(),event.getDeathsTotalDescription());
         assertEquals(selectForUpdate.getInjuriesTotal(),event.getInjuriesTotal());
         assertEquals(selectForUpdate.getInjuriesAmountOrderTotal(),event.getInjuriesAmountOrderTotal());
         assertEquals(selectForUpdate.getInjuriesTotalDescription(),event.getInjuriesTotalDescription());
         assertEquals(selectForUpdate.getMissingTotal(),event.getMissingTotal());
         assertEquals(selectForUpdate.getMissingAmountOrderTotal(),event.getMissingAmountOrderTotal());
         assertEquals(selectForUpdate.getMissingTotalDescription(),event.getMissingTotalDescription());
         assertEquals(selectForUpdate.getObjectid(),event.getObjectid());
         assertEquals(selectForUpdate.getShape(),event.getShape());
         assertEquals(selectForUpdate.getHousesDamaged(),event.getHousesDamaged());
         assertEquals(selectForUpdate.getHousesDamagedAmountOrder(),event.getHousesDamagedAmountOrder());
         assertEquals(selectForUpdate.getHousesDamDescription(),event.getHousesDamDescription());
         assertEquals(selectForUpdate.getHousesDamagedTotal(),event.getHousesDamagedTotal());
         assertEquals(selectForUpdate.getHousesDamaAmountOrderTotal(),event.getHousesDamaAmountOrderTotal());
         assertEquals(selectForUpdate.getHousesDamTotalDescription(),event.getHousesDamTotalDescription());
         assertEquals(selectForUpdate.getNumDeposits(),event.getNumDeposits());
         assertEquals(selectForUpdate.getUrl(),event.getUrl()); 
         assertEquals(selectForUpdate.getLastModified(),selectForUpdate.getLastModified());
         compareGeoJson(event.getGeometry(),selectForUpdate.getGeometry());

         
   
         EventTsunami result2  = eventDAO.findById(result.getId()); 
         assertEquals(result2.getId(),result.getId());
         assertEquals(result2.getYear(),result.getYear());
         assertEquals(result2.getMonth(),result.getMonth());
         assertEquals(result2.getDay(),result.getDay());
         assertEquals(result2.getHour(),result.getHour());
         assertEquals(result2.getMinute(),result.getMinute());
         assertEquals(result2.getSecond(),result.getSecond());
         assertEquals(result2.getDateString(),result.getDateString());
         assertEquals(result2.getLatitude(),result.getLatitude());
         assertEquals(result2.getLongitude(),result.getLongitude());
         assertEquals(result2.getLocationName(),result.getLocationName());
         assertEquals(result2.getArea(),result.getArea());
         assertEquals(result2.getCountry(),result.getCountry());
         assertEquals(result2.getRegionCode(),result.getRegionCode());
         assertEquals(result2.getRegion(),result.getRegion());
         assertEquals(result2.getCauseCode(),result.getCauseCode());
         assertEquals(result2.getCause(),result.getCause());
         assertEquals(result2.getEventValidityCode(),result.getEventValidityCode());
         assertEquals(result2.getEventValidity(),result.getEventValidity());
         assertEquals(result2.getEqMagUnk(),result.getEqMagUnk());
         assertEquals(result2.getEqMagMb(),result.getEqMagMb());
         assertEquals(result2.getEqMagMs(),result.getEqMagMs());
         assertEquals(result2.getEqMagMw(),result.getEqMagMw());
         assertEquals(result2.getEqMagMl(),result.getEqMagMl());
         assertEquals(result2.getEqMagMta(),result.getEqMagMta());
         assertEquals(result2.getEqMagnatude(),result.getEqMagnatude());
         assertEquals(result2.getEqMagnatudeRank(),result.getEqMagnatudeRank());
         assertEquals(result2.getEqDepth(),result.getEqDepth());
         assertEquals(result2.getMaxEventRunup(),result.getMaxEventRunup());
         assertEquals(result2.getTsMtAbe(),result.getTsMtAbe());
         assertEquals(result2.getTsMtIi(),result.getTsMtIi());
         assertEquals(result2.getTsIntensity(),result.getTsIntensity());
         assertEquals(result2.getDamageMillionsDollars(),result.getDamageMillionsDollars());
         assertEquals(result2.getDamageAmountOrder(),result.getDamageAmountOrder());
         assertEquals(result2.getDamageDescription(),result.getDamageDescription());
         assertEquals(result2.getHousesDestroyed(),result.getHousesDestroyed());
         assertEquals(result2.getHousesAmountOrder(),result.getHousesAmountOrder());
         assertEquals(result2.getHousesDescription(),result.getHousesDescription());
         assertEquals(result2.getDeaths(),result.getDeaths());
         assertEquals(result2.getDeathsAmountOrder(),result.getDeathsAmountOrder());
         assertEquals(result2.getDeathsDescription(),result.getDeathsDescription());
         assertEquals(result2.getInjuries(),result.getInjuries());
         assertEquals(result2.getInjuriesAmountOrder(),result.getInjuriesAmountOrder());
         assertEquals(result2.getInjuriesDescription(),result.getInjuriesDescription());
         assertEquals(result2.getMissing(),result.getMissing());
         assertEquals(result2.getMissingAmountOrder(),result.getMissingAmountOrder());
         assertEquals(result2.getMissingDescription(),result.getMissingDescription());
         assertEquals(result2.getComments(),result.getComments());
         assertEquals(result2.getNumRunup(),result.getNumRunup());
         assertEquals(result2.getDamageMillionsDollarsTotal(),result.getDamageMillionsDollarsTotal());
         assertEquals(result2.getDamageAmountOrderTotal(),result.getDamageAmountOrderTotal());
         assertEquals(result2.getDamageTotalDescription(),result.getDamageTotalDescription());
         assertEquals(result2.getHousesDestroyedTotal(),result.getHousesDestroyedTotal());
         assertEquals(result2.getHousesDestroyedTotal(),result.getHousesDestroyedTotal());
         assertEquals(result2.getHousesAmountOrderTotal(),result.getHousesAmountOrderTotal());
         assertEquals(result2.getHousesTotalDescription(),result.getHousesTotalDescription());
         assertEquals(result2.getDeathsTotal(),result.getDeathsTotal());
         assertEquals(result2.getDeathsAmountOrderTotal(),result.getDeathsAmountOrderTotal());
         assertEquals(result2.getDeathsTotalDescription(),result.getDeathsTotalDescription());
         assertEquals(result2.getInjuriesTotal(),result.getInjuriesTotal());
         assertEquals(result2.getInjuriesAmountOrderTotal(),result.getInjuriesAmountOrderTotal());
         assertEquals(result2.getInjuriesTotalDescription(),result.getInjuriesTotalDescription());
         assertEquals(result2.getMissingTotal(),result.getMissingTotal());
         assertEquals(result2.getMissingAmountOrderTotal(),result.getMissingAmountOrderTotal());
         assertEquals(result2.getMissingTotalDescription(),result.getMissingTotalDescription());
         assertEquals(result2.getObjectid(),result.getObjectid());
         assertEquals(result2.getShape(),result.getShape());
         assertEquals(result2.getHousesDamaged(),result.getHousesDamaged());
         assertEquals(result2.getHousesDamagedAmountOrder(),result.getHousesDamagedAmountOrder());
         assertEquals(result2.getHousesDamDescription(),result.getHousesDamDescription());
         assertEquals(result2.getHousesDamagedTotal(),result.getHousesDamagedTotal());
         assertEquals(result2.getHousesDamaAmountOrderTotal(),result.getHousesDamaAmountOrderTotal());
         assertEquals(result2.getHousesDamTotalDescription(),result.getHousesDamTotalDescription());
         assertEquals(result2.getNumDeposits(),result.getNumDeposits());
         assertEquals(result2.getUrl(),result.getUrl()); 
         assertEquals(result2.getLastModified(),result.getLastModified());
        

 
    }
    @Test
    public void findNotFound() {
        System.out.println("update");

         EventTsunami result   = eventDAO.findById(BigDecimal.valueOf(0));   
         flush(); // have to do this.. so that the sql is actually executed.
        assertNull(result);
    }
    @Test
    public void invalidInsert() {
        EventTsunami event = new EventTsunami();
        // 1- 255
        event.setId(BigDecimal.valueOf(123));
        event.setDateString("0123456789001234567890");
        

        EventTsunami result = eventDAO.save(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("dateString")){
               assertThat(tmp).isEqualTo("dateString");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.Size.message}");
                  
              }else if (tmp.equals("geometry")){
               assertThat(tmp).isEqualTo("geometry");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
              }
              else {
                  fail("not an expected constraint violation");
              }

            }
            bExceptionCaught = true;

        } 
        
        
        assertThat(bExceptionCaught).isEqualTo(true);

 
    }
}
