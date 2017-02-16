/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.dao.model.EventEarthquake;
import com.cgi.poc.dw.dao.model.EventFlood;
import com.cgi.poc.dw.dao.model.EventTsunami;
import com.cgi.poc.dw.dao.model.EventVolcano;
import com.cgi.poc.dw.dao.model.EventHurricane;
import com.cgi.poc.dw.dao.model.EventWeather;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import io.dropwizard.testing.ResourceHelpers;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

public class HibernateUtil {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("cgi-test-integration.yml");
    private SessionFactory sessionFactory;
    private Session openSession;

    private static HibernateUtil instance = new HibernateUtil();

    public HibernateUtil() {
        try {
            File newConfiguration = new File(CONFIG_PATH);
            Yaml yaml = new Yaml();
            InputStream is = new FileInputStream(newConfiguration);

            LinkedHashMap yamlParsers = (LinkedHashMap<String, ArrayList>) yaml.load(is);

            LinkedHashMap databaseCfg = (LinkedHashMap<String, ArrayList>) yamlParsers.get("database");
            String driver = (String) databaseCfg.get("driverClass");
            String dbUrl = (String) databaseCfg.get("url");
            String userName = (String) databaseCfg.get("user");
            String userPwd = (String) databaseCfg.get("password");

            Configuration configuration = new Configuration();
            // need to be able to read config file to get the uname/pwd for testing.. can't 
            // use in memory DB b/c we need json, and geometry types which are not supported
            // by h2
            configuration.setProperty("connection.driver_class", driver);
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", userName);
            configuration.setProperty("hibernate.connection.password", userPwd);
            configuration.setProperty("hibernate.current_session_context_class", "thread");

            configuration.addAnnotatedClass(FireEvent.class);
            configuration.addAnnotatedClass(EventEarthquake.class);
            configuration.addAnnotatedClass(EventWeather.class);
            configuration.addAnnotatedClass(EventFlood.class);
            configuration.addAnnotatedClass(EventHurricane.class);
            configuration.addAnnotatedClass(EventTsunami.class);
            configuration.addAnnotatedClass(EventVolcano.class);

            sessionFactory = configuration.buildSessionFactory();
            openSession = sessionFactory.openSession();
            Connection sqlConnection = ((SessionImpl) openSession).connection();
            sessionFactory.getCurrentSession();

            JdbcConnection conn = new JdbcConnection(sqlConnection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(conn);
            Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
            String ctx = null;
            liquibase.update(ctx);
        } catch (Exception ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);

        }

    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            return new HibernateUtil();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getOpenSession() {
        return openSession;
    }

}
