/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.CgiPocApplication;
import com.cgi.poc.dw.CgiPocConfiguration;
import com.cgi.poc.dw.dao.model.FireEvent;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;

public class HibernateUtil {
@ClassRule
  public static final DropwizardAppRule<CgiPocConfiguration> RULE = new DropwizardAppRule<CgiPocConfiguration>(
      CgiPocApplication.class, ResourceHelpers.resourceFilePath("cgi-test-integration.yml"));
    private static HibernateUtil instance = new HibernateUtil();
private SessionFactory sessionFactory;
private HibernateUtil(){
    this.sessionFactory = buildSessionFactory();
}

private synchronized static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
         
        configuration.setProperty("connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/cgi_poc?createDatabaseIfNotExist=true");
        configuration.setProperty("hibernate.connection.username", "cgi_user");
        configuration.setProperty("hibernate.connection.password", "cgi_P@ssw0rd");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.addAnnotatedClass(FireEvent.class);

        return configuration.buildSessionFactory();

}

public static HibernateUtil getInstance() {
    if(instance == null){
        return new HibernateUtil();
    }
    return instance;
}

public SessionFactory getSessionFactory() {
    return sessionFactory;
}

}
