/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventEarthquake;
import com.cgi.poc.dw.dao.model.EventEarthquakePK;
import com.cgi.poc.dw.dao.model.EventFlood;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
 
 
/**
 *
 * @author dawna.floyd
 */
 
public class EventEarthquakeDAOTest extends DaoUnitTestBase  {
    
 
    EventEarthquakeDAO eventDAO;
     
     public EventEarthquakeDAOTest() {
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
        eventDAO = new EventEarthquakeDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class FireEventDAO.
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testCRUD() throws Exception {
           ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleQuakeEvent.json").toURI());
         
         
             JsonParser  parser  = jsonFactory.createParser(new FileReader(file));
	    parser.setCodec(mapper);
            ObjectNode node = parser.readValueAs(ObjectNode.class);
            JsonNode jsonEvent = node.get("attributes");
            JsonNode geo = node.get("attributes");
            
           EventEarthquake event = mapper.readValue(jsonEvent.toString(), EventEarthquake.class);


        event.setGeometry(geo.toString());
        EventEarthquake result = eventDAO.save(event);
        Set<ConstraintViolation<EventEarthquake>> errors = validator.validate(result);
         flush(); // have to do this.. so that the sql is actually executed.
        assertThat(result.getEqid()).isEqualTo(event.getEqid());
        assertEquals(result.getNotificationId(), event.getNotificationId());
        assertEquals(result.getDatetime(), event.getDatetime());
        assertEquals(result.getObjectid(), event.getObjectid());
        assertEquals(result.getDepth(), event.getDepth());
        assertEquals(result.getLatitude(), event.getLatitude());
        assertEquals(result.getLongitude(), event.getLongitude());
        assertEquals(result.getMagnitude(), event.getMagnitude());
        assertEquals(result.getNumstations(), event.getNumstations());
        assertEquals(result.getRegion(), event.getRegion());
        assertEquals(result.getSource(), event.getSource());
        assertEquals(result.getVersion(), event.getVersion());
        assertEquals(result.getShape(), event.getShape());

        assertNotNull(result.getLastModified());
        compareGeoJson(event.getGeometry(),result.getGeometry());

  
        EventEarthquake selectForUpdate = eventDAO.selectForUpdate(result);
         flush(); // have to do this.. so that the sql is actually executed.
        assertThat(result.getEqid()).isEqualTo(selectForUpdate.getEqid());
        assertEquals(result.getNotificationId(), selectForUpdate.getNotificationId());
        assertEquals(result.getDatetime(), selectForUpdate.getDatetime());
        assertEquals(result.getObjectid(), selectForUpdate.getObjectid());
        assertEquals(result.getDepth(), selectForUpdate.getDepth());
        assertEquals(result.getLatitude(), selectForUpdate.getLatitude());
        assertEquals(result.getLongitude(), selectForUpdate.getLongitude());
        assertEquals(result.getMagnitude(), selectForUpdate.getMagnitude());
        assertEquals(result.getNumstations(), selectForUpdate.getNumstations());
        assertEquals(result.getRegion(), selectForUpdate.getRegion());
        assertEquals(result.getSource(), selectForUpdate.getSource());
        assertEquals(result.getVersion(), selectForUpdate.getVersion());
        assertEquals(result.getShape(), selectForUpdate.getShape());
         assertEquals(result.getLastModified(),selectForUpdate.getLastModified());
        compareGeoJson(event.getGeometry(),result.getGeometry());

        
        List<EventEarthquake> resultList = eventDAO.findById(result.getEventEarthquakePK().getEqid());  
        EventEarthquake result2  = resultList.get(0);
        assertThat(result.getEqid()).isEqualTo(result2.getEqid());
        assertEquals(result.getNotificationId(), result2.getNotificationId());
        assertEquals(result.getDatetime(), result2.getDatetime());
        assertEquals(result.getObjectid(), result2.getObjectid());
        assertEquals(result.getDepth(), result2.getDepth());
        assertEquals(result.getLatitude(), result2.getLatitude());
        assertEquals(result.getLongitude(), result2.getLongitude());
        assertEquals(result.getMagnitude(), result2.getMagnitude());
        assertEquals(result.getNumstations(), result2.getNumstations());
        assertEquals(result.getRegion(), result2.getRegion());
        assertEquals(result.getSource(), result2.getSource());
        assertEquals(result.getVersion(), result2.getVersion());
        assertEquals(result.getShape(), result2.getShape());
         assertEquals(result.getLastModified(),result2.getLastModified());
        compareGeoJson(event.getGeometry(),result2.getGeometry());

 
    }
    @Test
    public void findNotFound() {
        System.out.println("update");

        List<EventEarthquake> resultList   = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);
    }
    @Test
    public void invalidInsert() {
        System.out.println("update");
        EventEarthquakePK eventEarthquakePK = new EventEarthquakePK();
        EventEarthquake event = new EventEarthquake();
        eventEarthquakePK.setEqid("");
        eventEarthquakePK.setDatetime(new Date());
        event.setEventEarthquakePK(eventEarthquakePK);
        event.setMagnitude(BigDecimal.valueOf(4.0));

        EventEarthquake result = eventDAO.save(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("eqid")){
               assertThat(tmp).isEqualTo("eqid");
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
