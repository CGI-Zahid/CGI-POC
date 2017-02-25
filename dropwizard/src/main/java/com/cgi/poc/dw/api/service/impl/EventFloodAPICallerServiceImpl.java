/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.dao.EventFloodDAO;
import com.cgi.poc.dw.dao.EventNotificationDAO;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.EventFlood;
import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationUser;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.service.EmailService;
import com.cgi.poc.dw.service.TextMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author dawna.floyd
 */
public class EventFloodAPICallerServiceImpl extends APICallerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EventFloodAPICallerServiceImpl.class);

    private EventFloodDAO eventDAO;
    private TextMessageService textMessageService;
    private EmailService emailService;
    private UserDao userDao;
    private EventNotificationDAO eventNotificationDAO;

    @Inject
    public EventFloodAPICallerServiceImpl(String eventUrl, Client client, EventFloodDAO weatherEventDAO,
          SessionFactory sessionFactory, TextMessageService textMessageService,
        EmailService emailService, UserDao userDao, EventNotificationDAO eventNotificationDAO) {
        super(eventUrl, client, sessionFactory);
        eventDAO = weatherEventDAO;
        this.textMessageService = textMessageService;
        this.emailService = emailService;
        this.userDao = userDao;
        this.eventNotificationDAO = eventNotificationDAO;
    }

    public void mapAndSave(JsonNode eventJson, JsonNode geoJson) {
        ObjectMapper mapper = new ObjectMapper();
        EventFlood retEvent;

        Session session = sessionFactory.openSession();
        try {
            EventFlood event = mapper.readValue(eventJson.toString(), EventFlood.class);

            event.setGeometry(geoJson.toString());
            ManagedSessionContext.bind(session);

            Transaction transaction = session.beginTransaction();

            LOG.info("Event to save : {}", event.toString());
            // Archive users based on last login date
            retEvent = eventDAO.save(event);
            transaction.commit();

            if(retEvent.getLastModified() != null){
                LOG.info("Event for notifications");

                GeoCoordinates geo = new GeoCoordinates();
                geo.setLatitude(event.getLatitude().doubleValue());
                geo.setLongitude(event.getLongitude().doubleValue());

                List<User> users = userDao.getGeoWithinRadius(Arrays.asList(geo), 50.00);

                EventNotification eventNotification = new EventNotification();
                eventNotification.setCitizensAffected(users.size());
                eventNotification.setDescription("Emergency alert: Flood near "+event.getWaterbody()+" in your area. Please log in at <our site> for more information.");
                eventNotification.setGenerationDate(new Date());
                eventNotification.setGeometry(event.getGeometry());
                eventNotification.setUrl1(event.getUrl());
                eventNotification.setType("Flood");
                eventNotification.setUserId(userDao.getAdminUser());

                if (users.size() > 0) {

                    LOG.info("Send notifications to : {}", users.toString());
                    for (User user : users) {
                        EventNotificationUser currENUser= new EventNotificationUser();
                        currENUser.setUserId(user);
                        eventNotification.addNotifiedUser(currENUser);

                        if (user.getSmsNotification()) {
                            textMessageService.send(user.getPhone(), eventNotification.getDescription());
                        }
                        if (user.getEmailNotification()) {
                            emailService.send(null, Arrays.asList(user.getEmail()), "Emergency alert from MyCAlerts: Flood near " + event.getWaterbody(),
                                eventNotification.getDescription());
                        }
                    }
                }
                eventNotificationDAO.save(eventNotification);
            }else{
                LOG.debug("Event last modified not changed");
            }
        } catch (IOException ex) {
            LOG.error("Unable to parse the result for the flood event : error: {}", ex.getMessage());
        } finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }

    }


}
