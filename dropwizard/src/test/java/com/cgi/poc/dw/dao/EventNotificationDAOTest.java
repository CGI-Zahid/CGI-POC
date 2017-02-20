/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

 import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationZipcode;
import com.cgi.poc.dw.dao.model.User;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.hibernate.validator.internal.engine.path.PathImpl;
 

/**
 *
 * @author dawna.floyd
 */
 
public class EventNotificationDAOTest extends DaoUnitTestBase  {
    
 
    EventNotificationDAO eventDAO;
     
     public EventNotificationDAOTest() {
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
        eventDAO = new EventNotificationDAO(getSessionFactory(),validator);
    }
    
    @After
    public void tearDown() {
              super.tearDown();

     }
 
    /**
     * Test of save method, of class FireEventDAO.
     */
    @Test
    public void testCRUD() throws Exception {
        signupAdminUser();
        EventNotification event = new EventNotification();
        User tmpUser = new User();
        tmpUser.setId(Long.valueOf(100));
        event.setType("FIRE");
        event.setUrl1("www.msn.com");
        event.setUrl2("www.cnn.com");
        event.setUserId(tmpUser);
        event.setDescription("CRUD TEST EVENT");
        event.setCitizensAffected(Integer.valueOf(1000));
 
        EventNotificationZipcode eventNotificationZipcode1 = new EventNotificationZipcode();
        eventNotificationZipcode1.setZipCode("92105");
        EventNotificationZipcode eventNotificationZipcode2 = new EventNotificationZipcode();
        eventNotificationZipcode2.setZipCode("92106");
        event.addZipcode(eventNotificationZipcode1);
        event.addZipcode(eventNotificationZipcode2);

        String geo  = "\"geometry\": {\n" +
        "    \"x\": -10677457.159137897,\n" +
        "    \"y\": 4106537.9944933983\n" +
        "  }";
        event.setGeometry(geo);
        EventNotification result = eventDAO.save(event);
         
        assertNotNull(result.getId());
        assertEquals(result.getType(),event.getType());
        assertEquals(result.getUrl1(),event.getUrl1());
        assertEquals(result.getUrl2(),event.getUrl2());
        assertEquals(result.getDescription(),event.getDescription());
        assertEquals(result.getUserId(),event.getUserId());
        assertEquals(result.getCitizensAffected(),event.getCitizensAffected());
        assertEquals(result.getEventNotificationZipcodes(),event.getEventNotificationZipcodes());
         assertNotNull(result.getGenerationDate());
        
        List<EventNotification> resultList= eventDAO.retrieveAll( );        
        EventNotification result2  = resultList.get(0);    
        assertEquals(result2.getId(),result.getId());
        assertEquals(result2.getType(),result.getType());
        assertEquals(result2.getUrl1(),result.getUrl1());
        assertEquals(result2.getUrl2(),result.getUrl2());
        assertEquals(result2.getDescription(),result.getDescription());
        assertEquals(result2.getUserId(),result.getUserId());
        assertEquals(result2.getCitizensAffected(),result.getCitizensAffected());
        assertEquals(result2.getEventNotificationZipcodes(),result.getEventNotificationZipcodes());
        assertEquals(result2.getGenerationDate(),result.getGenerationDate());

        List<EventNotification> resultList2= eventDAO.retrieveAllForUser(tmpUser);
        EventNotification result3  = resultList2.get(0);    
        assertEquals(result3.getId(),result.getId());
        assertEquals(result3.getType(),result.getType());
        assertEquals(result3.getUrl1(),result.getUrl1());
        assertEquals(result3.getUrl2(),result.getUrl2());
        assertEquals(result3.getDescription(),result.getDescription());
        assertEquals(result3.getUserId(),result.getUserId());
        assertEquals(result3.getCitizensAffected(),result.getCitizensAffected());
        assertEquals(result3.getEventNotificationZipcodes(),result.getEventNotificationZipcodes());
        assertEquals(result3.getGenerationDate(),result.getGenerationDate());
         
    }
    @Test
    public void retriveAllGetsNothing() {
        EventNotification event = new EventNotification();
        List<EventNotification> resultList = eventDAO.retrieveAll( );   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);
    }
    @Test
    public void retriveForUserGetsNothing() {
        User tmpUser = new User();
        tmpUser.setId(Long.valueOf(555));
        List<EventNotification> resultList = eventDAO.retrieveAllForUser(tmpUser);   
         flush(); // have to do this.. so that the sql is actually executed.
         assertThat(resultList.size()).isEqualTo(0);
    }
    @Test
    public void invalidInsert() {
        EventNotification event = new EventNotification();
        boolean bExceptionCaught = false;
        try {
            EventNotification result = eventDAO.save(event);
            flush();
        } catch (ConstraintViolationException hibernateException) {
            Set<ConstraintViolation<?>> constraintViolations = hibernateException.getConstraintViolations();
            for (ConstraintViolation violation : constraintViolations) {
                String tmp = ((PathImpl) violation.getPropertyPath())
                        .getLeafNode().getName();
                if (tmp.equals("userId")) {
                    assertThat(tmp).isEqualTo("userId");
                    assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");

                } else if (tmp.equals("eventNotificationZipcodes")) {
                    assertThat(tmp).isEqualTo("eventNotificationZipcodes");
                    assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");

                } else if (tmp.equals("type")) {
                    assertThat(tmp).isEqualTo("type");
                    assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");

                } else if (tmp.equals("description")) {
                    assertThat(tmp).isEqualTo("description");
                    assertThat(violation.getMessageTemplate()).isEqualTo("{javax.validation.constraints.NotNull.message}");

                } else {
                    fail("not an expected constraint violation");
                }
            }
            bExceptionCaught = true;
        }        
        assertThat(bExceptionCaught).isEqualTo(true); 
    }
}
