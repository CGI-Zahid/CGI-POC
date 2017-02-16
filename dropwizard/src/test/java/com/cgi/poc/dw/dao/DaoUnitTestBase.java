/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.FileReader;
import java.io.IOException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;

/**
 *
 * @author dawna.floyd
 */
public class DaoUnitTestBase {
    private  SessionFactory sessionFactory;
    private Session session;
    Validator validator;
    JsonFactory jsonFactory;
    HibernateUtil dbUtil;
    Transaction dbTransaction;
    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        dbUtil = HibernateUtil.getInstance();
        sessionFactory = dbUtil.getSessionFactory();
        session = dbUtil.getOpenSession();
         
        dbTransaction = sessionFactory.getCurrentSession().beginTransaction();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        jsonFactory = new JsonFactory();

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
             JsonParser  parserNew  = jsonFactory.createParser(newValue);
             JsonParser  parserOld  = jsonFactory.createParser(oldValue);
	    parserNew.setCodec(mapper);
	    parserOld.setCodec(mapper);
            ObjectNode nodeNew = parserNew.readValueAs(ObjectNode.class);
            ObjectNode nodeOld = parserOld.readValueAs(ObjectNode.class);
 
            
            assertThat(nodeNew.size()).isEqualTo(nodeOld.size());
            
            assertThat(nodeNew).isEqualTo(nodeOld);
            //assertThat(oldItems.get("geometry")).isEqualTo(newItems.get("geometry"));            
        } catch (IOException iOException) {
                              fail("could not passe geometry entries");

        }
       
    }
}
