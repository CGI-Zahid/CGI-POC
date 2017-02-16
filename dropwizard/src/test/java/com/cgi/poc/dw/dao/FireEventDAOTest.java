/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dawna.floyd
 */
 
public class FireEventDAOTest extends DaoUnitTestBase  {
    
 
    FireEventDAO eventDAO;
     
     public FireEventDAOTest() {
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
        eventDAO = new FireEventDAO(getSessionFactory(),validator); 
        
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of save method, of class FireEventDAO.
     */
    @Test
    public void testCRUD() throws IOException, URISyntaxException, ParseException {
        ClassLoader classLoader = getClass().getClassLoader();
           File file = new File(ClassLoader.getSystemResource("exampleFireEvent.json").toURI());
         
            Object obj = parser.parse(new FileReader(file));

           JSONObject jsonObject = (JSONObject) obj;
           JSONObject jsonEvent = (JSONObject)jsonObject.get("attributes");
           JSONObject geo = (JSONObject)jsonObject.get("geometry");
           ObjectMapper mapper = new ObjectMapper();
           FireEvent event = mapper.readValue(jsonEvent.toString(), FireEvent.class);
           
         event.setGeometry(geo.toJSONString());
         
        FireEvent result = eventDAO.save(event);
        Set<ConstraintViolation<FireEvent>> errors = validator.validate(result);
         flush(); // have to do this.. so that the sql is actually executed.
        assertThat(result.getIncidentname()).isEqualTo(event.getIncidentname());
        assertThat(result.getHotlink()).isEqualTo(event.getHotlink());
        assertThat(result.getStatus()).isEqualTo(event.getStatus());
        assertThat(result.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result.getLatitude()).isEqualTo(event.getLatitude());
        assertThat(result.getLongitude()).isEqualTo(event.getLongitude());
        assertThat(result.getAcres()).isEqualTo(event.getAcres());
        assertThat(result.getGacc()).isEqualTo(event.getGacc());
        assertThat(result.getState()).isEqualTo(event.getState());
        assertThat(result.getShape()).isEqualTo(event.getShape());
        assertThat(result.getIrwinid()).isEqualTo(event.getIrwinid());
        assertThat(result.getIscomplex()).isEqualTo(event.getIscomplex());
        assertThat(result.getComplexparentirwinid()).isEqualTo(event.getComplexparentirwinid());
        assertThat(result.getFirecause()).isEqualTo(event.getFirecause());
        assertThat(result.getReportdatetime()).isEqualTo(event.getReportdatetime());
        assertThat(result.getPercentcontained()).isEqualTo(event.getPercentcontained());
        assertThat(result.getFirediscoverydatetime()).isEqualTo(event.getFirediscoverydatetime());
        assertThat(result.getPooresponsibleunit()).isEqualTo(event.getPooresponsibleunit());
        assertThat(result.getIrwinmodifiedon()).isEqualTo(event.getIrwinmodifiedon());
        assertThat(result.getMapsymbol()).isEqualTo(event.getMapsymbol());
        assertThat(result.getDatecurrent()).isEqualTo(event.getDatecurrent());
        assertThat(result.getPooownerunit()).isEqualTo(event.getPooownerunit());
        assertThat(result.getOwneragency()).isEqualTo(event.getOwneragency());
        assertThat(result.getFireyear()).isEqualTo(event.getFireyear());
        assertThat(result.getLocalincidentidentifier()).isEqualTo(event.getLocalincidentidentifier());
        assertThat(result.getIncidenttypecategory()).isEqualTo(event.getIncidenttypecategory());
        assertNotNull(result.getLastModified());
        

        FireEvent result2 = eventDAO.findById(event.getUniquefireidentifier());        
        assertThat(result2.getIncidentname()).isEqualTo(event.getIncidentname());
        assertThat(result2.getHotlink()).isEqualTo(event.getHotlink());
        assertThat(result2.getStatus()).isEqualTo(event.getStatus());
        assertThat(result2.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(result2.getLatitude()).isEqualTo(event.getLatitude());
        assertThat(result2.getLongitude()).isEqualTo(event.getLongitude());
        assertThat(result2.getAcres()).isEqualTo(event.getAcres());
        assertThat(result2.getGacc()).isEqualTo(event.getGacc());
        assertThat(result2.getState()).isEqualTo(event.getState());
        assertThat(result2.getShape()).isEqualTo(event.getShape());
        assertThat(result2.getIrwinid()).isEqualTo(event.getIrwinid());
        assertThat(result2.getIscomplex()).isEqualTo(event.getIscomplex());
        assertThat(result2.getComplexparentirwinid()).isEqualTo(event.getComplexparentirwinid());
        assertThat(result2.getFirecause()).isEqualTo(event.getFirecause());
        assertThat(result2.getReportdatetime()).isEqualTo(event.getReportdatetime());
        assertThat(result2.getPercentcontained()).isEqualTo(event.getPercentcontained());
        assertThat(result2.getFirediscoverydatetime()).isEqualTo(event.getFirediscoverydatetime());
        assertThat(result2.getPooresponsibleunit()).isEqualTo(event.getPooresponsibleunit());
        assertThat(result2.getIrwinmodifiedon()).isEqualTo(event.getIrwinmodifiedon());
        assertThat(result2.getMapsymbol()).isEqualTo(event.getMapsymbol());
        assertThat(result2.getDatecurrent()).isEqualTo(event.getDatecurrent());
        assertThat(result2.getPooownerunit()).isEqualTo(event.getPooownerunit());
        assertThat(result2.getOwneragency()).isEqualTo(event.getOwneragency());
        assertThat(result2.getFireyear()).isEqualTo(event.getFireyear());
        assertThat(result2.getLocalincidentidentifier()).isEqualTo(event.getLocalincidentidentifier());
        assertThat(result2.getIncidenttypecategory()).isEqualTo(event.getIncidenttypecategory());
        assertEquals(result2.getLastModified(),result.getLastModified());

        compareGeoJson(event.getGeometry(),result.getGeometry());
 
        FireEvent selectForUpdate = eventDAO.selectForUpdate(result);
        assertThat(selectForUpdate.getIncidentname()).isEqualTo(event.getIncidentname());
        assertThat(selectForUpdate.getHotlink()).isEqualTo(event.getHotlink());
        assertThat(selectForUpdate.getStatus()).isEqualTo(event.getStatus());
        assertThat(selectForUpdate.getObjectid()).isEqualTo(event.getObjectid());
        assertThat(selectForUpdate.getLatitude()).isEqualTo(event.getLatitude());
        assertThat(selectForUpdate.getLongitude()).isEqualTo(event.getLongitude());
        assertThat(selectForUpdate.getAcres()).isEqualTo(event.getAcres());
        assertThat(selectForUpdate.getGacc()).isEqualTo(event.getGacc());
        assertThat(selectForUpdate.getState()).isEqualTo(event.getState());
        assertThat(selectForUpdate.getShape()).isEqualTo(event.getShape());
        assertThat(selectForUpdate.getIrwinid()).isEqualTo(event.getIrwinid());
        assertThat(selectForUpdate.getIscomplex()).isEqualTo(event.getIscomplex());
        assertThat(selectForUpdate.getComplexparentirwinid()).isEqualTo(event.getComplexparentirwinid());
        assertThat(selectForUpdate.getFirecause()).isEqualTo(event.getFirecause());
        assertThat(selectForUpdate.getReportdatetime()).isEqualTo(event.getReportdatetime());
        assertThat(selectForUpdate.getPercentcontained()).isEqualTo(event.getPercentcontained());
        assertThat(selectForUpdate.getFirediscoverydatetime()).isEqualTo(event.getFirediscoverydatetime());
        assertThat(selectForUpdate.getPooresponsibleunit()).isEqualTo(event.getPooresponsibleunit());
        assertThat(selectForUpdate.getIrwinmodifiedon()).isEqualTo(event.getIrwinmodifiedon());
        assertThat(selectForUpdate.getMapsymbol()).isEqualTo(event.getMapsymbol());
        assertThat(selectForUpdate.getDatecurrent()).isEqualTo(event.getDatecurrent());
        assertThat(selectForUpdate.getPooownerunit()).isEqualTo(event.getPooownerunit());
        assertThat(selectForUpdate.getOwneragency()).isEqualTo(event.getOwneragency());
        assertThat(selectForUpdate.getFireyear()).isEqualTo(event.getFireyear());
        assertThat(selectForUpdate.getLocalincidentidentifier()).isEqualTo(event.getLocalincidentidentifier());
        assertThat(selectForUpdate.getIncidenttypecategory()).isEqualTo(event.getIncidenttypecategory());
        assertEquals(selectForUpdate.getLastModified(),result.getLastModified());

        
        
    }
    @Test
    public void findNotFound() {
        System.out.println("update");
        FireEvent event = new FireEvent();
        event.setUniquefireidentifier("Uniq1");
        event.setNotificationId(1);
        event.setHotlink("http://msn.com");
        event.setIncidenttypecategory("A");
        FireEvent result = eventDAO.findById("noWay");   
         flush(); // have to do this.. so that the sql is actually executed.
        assertNull(result);

    
 
    }
    @Test
    public void invalidInsert() {
        System.out.println("update");
        FireEvent event = new FireEvent();
        event.setUniquefireidentifier("");
        event.setNotificationId(1);
        event.setHotlink("http://msn.com");
        event.setIncidenttypecategory("A");
        FireEvent result = eventDAO.save(event);
        boolean bExceptionCaught = false;
        try {
             flush();            
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for ( ConstraintViolation violation : constraintViolations ) {
                
              String tmp = ((PathImpl)violation.getPropertyPath())
                .getLeafNode().getName();
              if (tmp.equals("uniquefireidentifier")){
               assertThat(tmp).isEqualTo("uniquefireidentifier");
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
