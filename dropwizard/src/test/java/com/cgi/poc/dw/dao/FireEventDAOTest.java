/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.helper.IntegrationTest;

/**
 *
 * @author dawna.floyd
 */
 
public class FireEventDAOTest extends IntegrationTest  {
    
    protected  SessionFactory sessionFactory;
    private Session session;
    FireEventDAO eventDAO;
    Validator validator;
    
     public FireEventDAOTest() {

        
    }
    
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
         sessionFactory = HibernateUtil.getInstance().getSessionFactory();
         session = sessionFactory.openSession();
        Transaction beginTransaction = sessionFactory.getCurrentSession().beginTransaction();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        eventDAO = new FireEventDAO(sessionFactory,validator); 
        
    }
    
    @After
    public void tearDown() {
        
        sessionFactory.getCurrentSession().close();
    }
 
    /**
     * Test of update method, of class FireEventDAO.
     */
    @Test
    public void testCRUD() {
        System.out.println("update");
        FireEvent event = new FireEvent();
        event.setUniquefireidentifier("Uniq1");
        event.setNotificationId(1);
        event.setHotlink("http://msn.com");
        event.setIncidenttypecategory("A");
                JSONObject geo = new JSONObject();
        JSONObject ele = new JSONObject();
        geo.put("geometry", ele);
        ele.put("x", -10677457.159137897);
        ele.put("y", 4106537.9944933983);
        /*
        "geometry": {
           "x": -10677457.159137897,
           "y": 4106537.9944933983
         }
              */
        event.setGeometry(geo.toJSONString());
        FireEvent result = eventDAO.update(event);
        Set<ConstraintViolation<FireEvent>> errors = validator.validate(result);


        
        sessionFactory.getCurrentSession().flush(); // have to do this.. so that the sql is actually executed.

        FireEvent result2 = eventDAO.findById(event.getUniquefireidentifier());        
        assertThat(result2.getUniquefireidentifier()).isEqualTo(event.getUniquefireidentifier());
        assertEquals(result2.getLastModified(), result.getLastModified());
        assertEquals(result2.getNotificationId(), result.getNotificationId());
        assertEquals(result2.getHotlink(), result.getHotlink());
        assertEquals(result2.getIncidenttypecategory(), result.getIncidenttypecategory());
        assertEquals(result2.getGeometry(), result.getGeometry());
 
        FireEvent selectForUpdate = eventDAO.selectForUpdate(result);
        assertThat(selectForUpdate.getUniquefireidentifier()).isEqualTo(event.getUniquefireidentifier());
        assertEquals(selectForUpdate.getLastModified(), result.getLastModified());
        assertEquals(selectForUpdate.getNotificationId(), result.getNotificationId());
        assertEquals(selectForUpdate.getHotlink(), result.getHotlink());
        assertEquals(selectForUpdate.getIncidenttypecategory(), result.getIncidenttypecategory());
        
 
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
        sessionFactory.getCurrentSession().flush(); // have to do this.. so that the sql is actually executed.
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
        FireEvent result = eventDAO.update(event);
        boolean bExceptionCaught = false;
        try {
            sessionFactory.getCurrentSession().flush();            
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
