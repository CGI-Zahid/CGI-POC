/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.api.service.APIServiceFactory;
import com.cgi.poc.dw.dao.EventFloodDAO;
import com.cgi.poc.dw.dao.EventNotificationDAO;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.service.EmailService;
import com.cgi.poc.dw.service.TextMessageService;
import com.google.inject.Inject;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.EventWeatherDAO;
import javax.ws.rs.client.Client;
import org.hibernate.SessionFactory;

/**
 *
 * @author dawna.floyd
 */
public class APIServiceFactoryImpl implements APIServiceFactory{
       private final SessionFactory sessionFactory;
       private final TextMessageService textMessageService;
       private final EmailService emailService;
       private final UserDao userDao;
       private final EventNotificationDAO eventNotificationDAO;

    @Inject
    APIServiceFactoryImpl (SessionFactory factory, TextMessageService textMessageService,
      EmailService emailService, UserDao userDao, EventNotificationDAO eventNotificationDAO){
        sessionFactory = factory;
        this.textMessageService = textMessageService;
        this.emailService = emailService;
        this.userDao = userDao;
        this.eventNotificationDAO = eventNotificationDAO;
    }
    
    @Override
    public FireEventAPICallerServiceImpl create(Client client, String eventUrl, FireEventDAO eventDAO) {
        return new FireEventAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory, textMessageService, emailService, userDao, eventNotificationDAO);
    }
    @Override
    public EventWeatherAPICallerServiceImpl create(Client client, String eventUrl, EventWeatherDAO eventDAO) {
        return new EventWeatherAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory, textMessageService, emailService, userDao, eventNotificationDAO);
    }
    @Override
    public EventFloodAPICallerServiceImpl create(Client client, String eventUrl, EventFloodDAO eventDAO) {
        return new EventFloodAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory, textMessageService, emailService, userDao, eventNotificationDAO);
    }
    
}
