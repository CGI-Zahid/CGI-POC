/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventWeather;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.validator.internal.engine.path.PathImpl;
 


/**
 *
 * @author dawna.floyd
 */
 
public class EventWeatherDAOTest extends DaoUnitTestBase  {
    
 
    EventWeatherDAO eventDAO;
     
     public EventWeatherDAOTest() {
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
        eventDAO = new EventWeatherDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class EventWeatherDAO.
     */
    @Test
    public void testCRUD() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(ClassLoader.getSystemResource("exampleWeatherEvent.json").toURI());
         
         
             JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
           
        EventWeather event = mapper.readValue(jsonEvent.toString(), EventWeather.class);
        event.setGeometry(geo.toString());
           
        EventWeather result = eventDAO.update(event);
         flush(); // have to do this.. so that the sql is actually executed.
        Set<ConstraintViolation<EventWeather>> errors = validator.validate(result);
        assertThat(result.getWarnid()).isEqualTo(event.getWarnid());
        assertThat(result.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result.getGeometry()).isEqualTo(event.getGeometry());
        assertThat(result.getPhenom()).isEqualTo(event.getPhenom());
        assertThat(result.getSig()).isEqualTo(event.getSig());
        assertThat(result.getWfo()).isEqualTo(event.getWfo());
        assertThat(result.getEvent()).isEqualTo(event.getEvent());
        assertThat(result.getIssuance()).isEqualTo(event.getIssuance());
        assertThat(result.getExpiration()).isEqualTo(event.getExpiration());
        assertThat(result.getUrl()).isEqualTo(event.getUrl());
        assertThat(result.getMsgType()).isEqualTo(event.getMsgType());
        assertThat(result.getProdType()).isEqualTo(event.getProdType());
        assertThat(result.getIdpSource()).isEqualTo(event.getIdpSource());
        assertThat(result.getIdpSubset()).isEqualTo(event.getIdpSubset());
        assertThat(result.getIdpFiledate()).isEqualTo(event.getIdpFiledate());
        assertThat(result.getIdpIngestdate()).isEqualTo(event.getIdpIngestdate());
        assertThat(result.getIdpCurrentForecast()).isEqualTo(event.getIdpCurrentForecast());
        assertThat(result.getIdpTimeSeries()).isEqualTo(event.getIdpTimeSeries());
        assertThat(result.getIdpIssueddate()).isEqualTo(event.getIdpIssueddate());
        assertThat(result.getIdpValidtime()).isEqualTo(event.getIdpValidtime());
        assertThat(result.getIdpValidendtime()).isEqualTo(event.getIdpValidendtime());
        assertThat(result.getIdpFcstHour()).isEqualTo(event.getIdpFcstHour());
        assertThat(result.getShape()).isEqualTo(event.getShape());
        assertThat(result.getStArea()).isEqualTo(event.getStArea());
        assertThat(result.getStLength()).isEqualTo(event.getStLength());
  
        assertNotNull(result.getLastModified());
        compareGeoJson(event.getGeometry(),result.getGeometry());


        EventWeather result2 = eventDAO.findById(event.getWarnid());        
        assertThat(result2.getWarnid()).isEqualTo(event.getWarnid());
        assertThat(result2.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result2.getGeometry()).isEqualTo(event.getGeometry());
        assertThat(result2.getPhenom()).isEqualTo(event.getPhenom());
        assertThat(result2.getSig()).isEqualTo(event.getSig());
        assertThat(result2.getWfo()).isEqualTo(event.getWfo());
        assertThat(result2.getEvent()).isEqualTo(event.getEvent());
        assertThat(result2.getIssuance()).isEqualTo(event.getIssuance());
        assertThat(result2.getExpiration()).isEqualTo(event.getExpiration());
        assertThat(result2.getUrl()).isEqualTo(event.getUrl());
        assertThat(result2.getMsgType()).isEqualTo(event.getMsgType());
        assertThat(result2.getProdType()).isEqualTo(event.getProdType());
        assertThat(result2.getIdpSource()).isEqualTo(event.getIdpSource());
        assertThat(result2.getIdpSubset()).isEqualTo(event.getIdpSubset());
        assertThat(result2.getIdpFiledate()).isEqualTo(event.getIdpFiledate());
        assertThat(result2.getIdpIngestdate()).isEqualTo(event.getIdpIngestdate());
        assertThat(result2.getIdpCurrentForecast()).isEqualTo(event.getIdpCurrentForecast());
        assertThat(result2.getIdpTimeSeries()).isEqualTo(event.getIdpTimeSeries());
        assertThat(result2.getIdpIssueddate()).isEqualTo(event.getIdpIssueddate());
        assertThat(result2.getIdpValidtime()).isEqualTo(event.getIdpValidtime());
        assertThat(result2.getIdpValidendtime()).isEqualTo(event.getIdpValidendtime());
        assertThat(result2.getIdpFcstHour()).isEqualTo(event.getIdpFcstHour());
        assertThat(result2.getShape()).isEqualTo(event.getShape());
        assertThat(result2.getStArea()).isEqualTo(event.getStArea());
        assertThat(result2.getStLength()).isEqualTo(event.getStLength());
         assertEquals(result2.getLastModified(),result.getLastModified());
         compareGeoJson(result2.getGeometry(),result.getGeometry());
       
 
        EventWeather selectForUpdate = eventDAO.selectForUpdate(result);
        assertThat(selectForUpdate.getWarnid()).isEqualTo(event.getWarnid());
        assertThat(selectForUpdate.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(selectForUpdate.getGeometry()).isEqualTo(event.getGeometry());
        assertThat(selectForUpdate.getPhenom()).isEqualTo(event.getPhenom());
        assertThat(selectForUpdate.getSig()).isEqualTo(event.getSig());
        assertThat(selectForUpdate.getWfo()).isEqualTo(event.getWfo());
        assertThat(selectForUpdate.getEvent()).isEqualTo(event.getEvent());
        assertThat(selectForUpdate.getIssuance()).isEqualTo(event.getIssuance());
        assertThat(selectForUpdate.getExpiration()).isEqualTo(event.getExpiration());
        assertThat(selectForUpdate.getUrl()).isEqualTo(event.getUrl());
        assertThat(selectForUpdate.getMsgType()).isEqualTo(event.getMsgType());
        assertThat(selectForUpdate.getProdType()).isEqualTo(event.getProdType());
        assertThat(selectForUpdate.getIdpSource()).isEqualTo(event.getIdpSource());
        assertThat(selectForUpdate.getIdpSubset()).isEqualTo(event.getIdpSubset());
        assertThat(selectForUpdate.getIdpFiledate()).isEqualTo(event.getIdpFiledate());
        assertThat(selectForUpdate.getIdpIngestdate()).isEqualTo(event.getIdpIngestdate());
        assertThat(selectForUpdate.getIdpCurrentForecast()).isEqualTo(event.getIdpCurrentForecast());
        assertThat(selectForUpdate.getIdpTimeSeries()).isEqualTo(event.getIdpTimeSeries());
        assertThat(selectForUpdate.getIdpIssueddate()).isEqualTo(event.getIdpIssueddate());
        assertThat(selectForUpdate.getIdpValidtime()).isEqualTo(event.getIdpValidtime());
        assertThat(selectForUpdate.getIdpValidendtime()).isEqualTo(event.getIdpValidendtime());
        assertThat(selectForUpdate.getIdpFcstHour()).isEqualTo(event.getIdpFcstHour());
        assertThat(selectForUpdate.getShape()).isEqualTo(event.getShape());
        assertThat(selectForUpdate.getStArea()).isEqualTo(event.getStArea());
        assertThat(selectForUpdate.getStLength()).isEqualTo(event.getStLength());
         assertEquals(selectForUpdate.getLastModified(),result.getLastModified());
        compareGeoJson(result.getGeometry(),selectForUpdate.getGeometry());

    }
    @Test
    public void findNotFound() {

        EventWeather result = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
        assertNull(result);

    
 
    }
    @Test
    public void invalidInsert() {
        System.out.println("update");
        EventWeather event = new EventWeather();
        event.setWarnid("");
        event.setNotificationId(1);
        event.setUrl("http://msn.com");
        event.setMsgType("FFS");
        event.setProdType("Flash Flood Warning");
        
        
        EventWeather result = eventDAO.update(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("warnid")){
               assertThat(tmp).isEqualTo("warnid");
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
