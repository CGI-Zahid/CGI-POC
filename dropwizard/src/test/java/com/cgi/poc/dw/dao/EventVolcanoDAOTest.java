/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventVolcano;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
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
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author dawna.floyd
 */
 
public class EventVolcanoDAOTest extends DaoUnitTestBase  {
    
 
    EventVolcanoDAO eventDAO;
     
     public EventVolcanoDAOTest() {
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
        eventDAO = new EventVolcanoDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class EventVolcanoDAO.
     */
    @Test
    public void testCRUD() throws IOException,   URISyntaxException,    ParseException {
         ClassLoader classLoader = getClass().getClassLoader(); 
           File file = new File(ClassLoader.getSystemResource("exampleVolcanoEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject jsonEvent = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
            
           EventVolcano event = mapper.readValue(jsonEvent.toString(), EventVolcano.class);
        event.setGeometry(geo.toJSONString());
           
        EventVolcano result = eventDAO.save(event);
         flush(); // have to do this.. so that the sql is actually executed.
        Set<ConstraintViolation<EventVolcano>> errors = validator.validate(result);
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getPubdate()).isEqualTo(event.getPubdate());
        assertThat(result.getLink()).isEqualTo(event.getLink());
        assertThat(result.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result.getShape()).isEqualTo(event.getShape());
        assertThat(result.getAlert()).isEqualTo(event.getAlert());
        assertThat(result.getColor()).isEqualTo(event.getColor());
        assertThat(result.getDescrpt()).isEqualTo(event.getDescrpt());
        assertNotNull(result.getLastModified());
        compareGeoJson(event.getGeometry(),result.getGeometry());


        List<EventVolcano> resultList= eventDAO.findById(event.getId());        
        EventVolcano result2  = resultList.get(0);
        assertThat(result.getId()).isEqualTo(result2.getId());
        assertThat(result.getPubdate()).isEqualTo(result2.getPubdate());
        assertThat(result.getLink()).isEqualTo(result2.getLink());
        assertThat(result.getObjectid()).isEqualTo(result2.getObjectid());
        assertThat(result.getShape()).isEqualTo(result2.getShape());
        assertThat(result.getAlert()).isEqualTo(result2.getAlert());
        assertThat(result.getColor()).isEqualTo(result2.getColor());
        assertThat(result.getDescrpt()).isEqualTo(result2.getDescrpt());
        assertEquals(result.getLastModified(),result2.getLastModified());
        compareGeoJson(result.getGeometry(),result2.getGeometry());
       
 
        EventVolcano selectForUpdate = eventDAO.selectForUpdate(result);
        assertThat(result.getId()).isEqualTo(selectForUpdate.getId());
        assertThat(result.getPubdate()).isEqualTo(selectForUpdate.getPubdate());
        assertThat(result.getLink()).isEqualTo(selectForUpdate.getLink());
        assertThat(result.getObjectid()).isEqualTo(selectForUpdate.getObjectid());
        assertThat(result.getShape()).isEqualTo(selectForUpdate.getShape());
        assertThat(result.getAlert()).isEqualTo(selectForUpdate.getAlert());
        assertThat(result.getColor()).isEqualTo(selectForUpdate.getColor());
        assertThat(result.getDescrpt()).isEqualTo(selectForUpdate.getDescrpt());
        assertEquals(result.getLastModified(),selectForUpdate.getLastModified());
        compareGeoJson(result.getGeometry(),selectForUpdate.getGeometry());

    }
    @Test
    public void findNotFound() {

        List<EventVolcano> resultList = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);

    
 
    }
    @Test
    public void invalidInsert() {
        System.out.println("update");
        EventVolcano event = new EventVolcano();
        event.setId("");
        event.setNotificationId(1);
        event.setLink("http://msn.com");
        
        
        EventVolcano result = eventDAO.save(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("id")){
               assertThat(tmp).isEqualTo("id");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.Size.message}");
                  
              }else if (tmp.equals("pubdate")){
               assertThat(tmp).isEqualTo("pubdate");
               assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");
                  
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
