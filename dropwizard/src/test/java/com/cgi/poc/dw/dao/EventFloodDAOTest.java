/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventFlood;
import com.cgi.poc.dw.dao.model.EventFloodPK;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
/**
 *
 * @author dawna.floyd
 */
 
public class EventFloodDAOTest extends DaoUnitTestBase  {
    
 
    EventFloodDAO eventDAO;
     
     public EventFloodDAOTest() {
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
        eventDAO = new EventFloodDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class FireEventDAO.
     */
    @Test
    public void testCRUD() throws IOException, ParseException, URISyntaxException {
         File file = new File(ClassLoader.getSystemResource("exampleFloodEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject jsonEvent = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
           EventFlood event = mapper.readValue(jsonEvent.toString(), EventFlood.class);



        event.setGeometry(geo.toJSONString());
        EventFlood result = eventDAO.save(event);
         flush(); // have to do this.. so that the sql is actually executed.
        Set<ConstraintViolation<EventFlood>> errors = validator.validate(result);
         
         assertEquals(result.getWaterbody(),event.getWaterbody());
         assertEquals(result.getObstime(),event.getObstime());
         assertEquals(result.getObjectid(),event.getObjectid());
         assertEquals(result.getLatitude(),event.getLatitude());
         assertEquals(result.getLongitude(),event.getLongitude());
         assertEquals(result.getState(),event.getState());
         assertEquals(result.getIdpSource(),event.getIdpSource());
         assertEquals(result.getIdpSubset(),event.getIdpSubset());
         assertEquals(result.getShape(),event.getShape());
         assertEquals(result.getGaugelid(),event.getGaugelid());
         assertEquals(result.getLocation(),event.getLocation());
         assertEquals(result.getObserved(),event.getObserved());
         assertEquals(result.getUnits(),event.getUnits());
         assertEquals(result.getAction(),event.getAction());
         assertEquals(result.getFlood(),event.getFlood());
         assertEquals(result.getModerate(),event.getModerate());
         assertEquals(result.getMajor(),event.getMajor());
         assertEquals(result.getLowthresh(),event.getLowthresh());
         assertEquals(result.getLowthreshu(),event.getLowthreshu());
         assertEquals(result.getWfo(),event.getWfo());
         assertEquals(result.getHdatum(),event.getHdatum());
         assertEquals(result.getPedts(),event.getPedts());
         assertEquals(result.getSecvalue(),event.getSecvalue());
         assertEquals(result.getSecunit(),event.getSecunit());
         assertEquals(result.getUrl(),event.getUrl());
         assertEquals(result.getStatus(),event.getStatus());
         assertEquals(result.getForecast(),event.getForecast());
        assertNotNull(result.getLastModified());
         compareGeoJson(event.getGeometry(),result.getGeometry());

         flush(); // have to do this.. so that the sql is actually executed.

 
        EventFlood selectForUpdate4 = eventDAO.selectForUpdate(result);
         assertEquals(selectForUpdate4.getWaterbody(),event.getWaterbody());
         assertEquals(selectForUpdate4.getObstime(),event.getObstime());
         assertEquals(selectForUpdate4.getObjectid(),event.getObjectid());
         assertEquals(selectForUpdate4.getLatitude(),event.getLatitude());
         assertEquals(selectForUpdate4.getLongitude(),event.getLongitude());
         assertEquals(selectForUpdate4.getState(),event.getState());
         assertEquals(selectForUpdate4.getIdpSource(),event.getIdpSource());
         assertEquals(selectForUpdate4.getIdpSubset(),event.getIdpSubset());
         assertEquals(selectForUpdate4.getShape(),event.getShape());
         assertEquals(selectForUpdate4.getGaugelid(),event.getGaugelid());
         assertEquals(selectForUpdate4.getLocation(),event.getLocation());
         assertEquals(selectForUpdate4.getObserved(),event.getObserved());
         assertEquals(selectForUpdate4.getUnits(),event.getUnits());
         assertEquals(selectForUpdate4.getAction(),event.getAction());
         assertEquals(selectForUpdate4.getFlood(),event.getFlood());
         assertEquals(selectForUpdate4.getModerate(),event.getModerate());
         assertEquals(selectForUpdate4.getMajor(),event.getMajor());
         assertEquals(selectForUpdate4.getLowthresh(),event.getLowthresh());
         assertEquals(selectForUpdate4.getLowthreshu(),event.getLowthreshu());
         assertEquals(selectForUpdate4.getWfo(),event.getWfo());
         assertEquals(selectForUpdate4.getHdatum(),event.getHdatum());
         assertEquals(selectForUpdate4.getPedts(),event.getPedts());
         assertEquals(selectForUpdate4.getSecvalue(),event.getSecvalue());
         assertEquals(selectForUpdate4.getSecunit(),event.getSecunit());
         assertEquals(selectForUpdate4.getUrl(),event.getUrl());
         assertEquals(selectForUpdate4.getStatus(),event.getStatus());
         assertEquals(selectForUpdate4.getForecast(),event.getForecast());
         assertEquals(selectForUpdate4.getLastModified(),result.getLastModified());
         compareGeoJson(event.getGeometry(),result.getGeometry());

         
   
        List<EventFlood> resultList = eventDAO.findById(result.getEventFloodPK().getWaterbody());  
        EventFlood result2  = resultList.get(0);
         assertEquals(result2.getWaterbody(),event.getWaterbody());
         assertEquals(result2.getObstime(),event.getObstime());
         assertEquals(result2.getObjectid(),event.getObjectid());
         assertEquals(result2.getLatitude(),event.getLatitude());
         assertEquals(result2.getLongitude(),event.getLongitude());
         assertEquals(result2.getState(),event.getState());
         assertEquals(result2.getIdpSource(),event.getIdpSource());
         assertEquals(result2.getIdpSubset(),event.getIdpSubset());
         assertEquals(result2.getShape(),event.getShape());
         assertEquals(result2.getGaugelid(),event.getGaugelid());
         assertEquals(result2.getLocation(),event.getLocation());
         assertEquals(result2.getObserved(),event.getObserved());
         assertEquals(result2.getUnits(),event.getUnits());
         assertEquals(result2.getAction(),event.getAction());
         assertEquals(result2.getFlood(),event.getFlood());
         assertEquals(result2.getModerate(),event.getModerate());
         assertEquals(result2.getMajor(),event.getMajor());
         assertEquals(result2.getLowthresh(),event.getLowthresh());
         assertEquals(result2.getLowthreshu(),event.getLowthreshu());
         assertEquals(result2.getWfo(),event.getWfo());
         assertEquals(result2.getHdatum(),event.getHdatum());
         assertEquals(result2.getPedts(),event.getPedts());
         assertEquals(result2.getSecvalue(),event.getSecvalue());
         assertEquals(result2.getSecunit(),event.getSecunit());
         assertEquals(result2.getUrl(),event.getUrl());
         assertEquals(result2.getStatus(),event.getStatus());
         assertEquals(result2.getForecast(),event.getForecast());
         assertEquals(result2.getLastModified(),result.getLastModified());
        

 
    }
    @Test
    public void findNotFound() {
        System.out.println("update");

        List<EventFlood> resultList   = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);
    }
    @Test
    public void invalidInsert() {
        EventFlood event = new EventFlood();
        // 1- 255
        event.setWaterbody("");
        event.setObstime("someValidTime");

        EventFlood result = eventDAO.save(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("waterbody")){
               assertThat(tmp).isEqualTo("waterbody");
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
