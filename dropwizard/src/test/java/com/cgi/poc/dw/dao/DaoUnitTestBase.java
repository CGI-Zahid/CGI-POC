/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import java.util.HashMap;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

/**
 *
 * @author dawna.floyd
 */
public class DaoUnitTestBase {
    private  SessionFactory sessionFactory;
    private Session session;
    Validator validator;
    JSONParser parser;
    HibernateUtil dbUtil;
    Transaction dbTransaction;

    @Before
    public void setUp() throws Exception {
        dbUtil = HibernateUtil.getInstance();
        sessionFactory = dbUtil.getSessionFactory();
        session = dbUtil.getOpenSession();
         
        dbTransaction = sessionFactory.getCurrentSession().beginTransaction();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
         parser = new JSONParser();

    }
    
    @After
    public void tearDown() {
        dbTransaction.rollback();
        sessionFactory.getCurrentSession().close();
    }
    
    public Session getSession(){
        return session;
    }
    public void flush(){
        sessionFactory.getCurrentSession().flush();
    }   
    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }    
    
    public void compareGeoJson (String oldValue, String newValue){
        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            JSONObject newItems = mapper.readValue(newValue, JSONObject.class);
            JSONObject oldItems = mapper.readValue(oldValue, JSONObject.class);
            
            assertThat(newItems.size()).isEqualTo(oldItems.size());
            
            assertThat(oldItems).isEqualTo(newItems);
            //assertThat(oldItems.get("geometry")).isEqualTo(newItems.get("geometry"));            
        } catch (IOException iOException) {
                              fail("could not passe geometry entries");

        }
       
    }
}
