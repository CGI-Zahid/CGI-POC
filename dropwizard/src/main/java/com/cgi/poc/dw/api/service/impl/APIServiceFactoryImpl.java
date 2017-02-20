/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.api.service.APIServiceFactory;
import com.cgi.poc.dw.dao.EventFloodDAO;
import com.cgi.poc.dw.service.SearchUserService;
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
       private final SearchUserService searchUserService;

    @Inject
    APIServiceFactoryImpl (SessionFactory factory, SearchUserService searchUserService){
        sessionFactory = factory;
        this.searchUserService = searchUserService;
    }
    
    @Override
    public FireEventAPICallerServiceImpl create(Client client, String eventUrl, FireEventDAO eventDAO) {
        return new FireEventAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory, searchUserService);
    }
    @Override
    public EventWeatherAPICallerServiceImpl create(Client client, String eventUrl, EventWeatherDAO eventDAO) {
        return new EventWeatherAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory);
    }
    @Override
    public EventFloodAPICallerServiceImpl create(Client client, String eventUrl, EventFloodDAO eventDAO) {
        return new EventFloodAPICallerServiceImpl ( eventUrl, client,eventDAO, sessionFactory, searchUserService);
    }
    
}
