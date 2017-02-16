/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.EventHurricane;
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
 
public class EventHurricaneDAOTest extends DaoUnitTestBase  {
    
 
    EventHurricaneDAO eventDAO;
     
     public EventHurricaneDAOTest() {
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
        eventDAO = new EventHurricaneDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of update method, of class EventHurricaneDAO.
     */
    @Test
    public void testCRUD() throws IOException,   URISyntaxException,    ParseException {
         ClassLoader classLoader = getClass().getClassLoader(); 
           File file = new File(ClassLoader.getSystemResource("exampleHurricanesEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject jsonEvent = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
            
           EventHurricane event = mapper.readValue(jsonEvent.toString(), EventHurricane.class);
        event.setGeometry(geo.toJSONString());
           
        EventHurricane result = eventDAO.save(event);
         flush(); // have to do this.. so that the sql is actually executed.
        Set<ConstraintViolation<EventHurricane>> errors = validator.validate(result);
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getPubdate()).isEqualTo(event.getPubdate());
        assertThat(result.getLink()).isEqualTo(event.getLink());
        assertThat(result.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result.getShape()).isEqualTo(event.getShape());
        assertThat(result.getName()).isEqualTo(event.getName());
        assertThat(result.getType()).isEqualTo(event.getType());
        assertThat(result.getMovement()).isEqualTo(event.getMovement());
        assertThat(result.getWind()).isEqualTo(event.getWind());
        
  
        assertNotNull(result.getLastModified());
        compareGeoJson(event.getGeometry(),result.getGeometry());


        List<EventHurricane> resultList= eventDAO.findById(event.getId());        
        EventHurricane result2  = resultList.get(0);
        assertThat(result.getId()).isEqualTo(result2.getId());
        assertThat(result.getPubdate()).isEqualTo(result2.getPubdate());
        assertThat(result.getLink()).isEqualTo(result2.getLink());
        assertThat(result.getObjectid()).isEqualTo(result2.getObjectid());
        assertThat(result.getShape()).isEqualTo(result2.getShape());
        assertThat(result.getName()).isEqualTo(result2.getName());
        assertThat(result.getType()).isEqualTo(result2.getType());
        assertThat(result.getMovement()).isEqualTo(result2.getMovement());
        assertThat(result.getWind()).isEqualTo(result2.getWind());
        assertEquals(result.getLastModified(),result2.getLastModified());
        compareGeoJson(result.getGeometry(),result2.getGeometry());
       
 
        EventHurricane selectForUpdate = eventDAO.selectForUpdate(result);
        assertThat(result.getId()).isEqualTo(selectForUpdate.getId());
        assertThat(result.getPubdate()).isEqualTo(selectForUpdate.getPubdate());
        assertThat(result.getLink()).isEqualTo(selectForUpdate.getLink());
        assertThat(result.getObjectid()).isEqualTo(selectForUpdate.getObjectid());
        assertThat(result.getShape()).isEqualTo(selectForUpdate.getShape());
        assertThat(result.getName()).isEqualTo(selectForUpdate.getName());
        assertThat(result.getType()).isEqualTo(selectForUpdate.getType());
        assertThat(result.getMovement()).isEqualTo(selectForUpdate.getMovement());
        assertThat(result.getWind()).isEqualTo(selectForUpdate.getWind());
        assertEquals(result.getLastModified(),selectForUpdate.getLastModified());
        compareGeoJson(result.getGeometry(),selectForUpdate.getGeometry());

    }
    @Test
    public void findNotFound() {

        List<EventHurricane> resultList = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);

    
 
    }
    @Test
    public void invalidInsert() {
        System.out.println("update");
        EventHurricane event = new EventHurricane();
        event.setId("");
        event.setNotificationId(1);
        event.setLink("http://msn.com");
        event.setWind("blah blah");
        
        
        EventHurricane result = eventDAO.save(event);
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
