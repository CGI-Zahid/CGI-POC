/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.dao.EventNotificationDAO;
import com.cgi.poc.dw.dao.EventWeatherDAO;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationUser;
import com.cgi.poc.dw.dao.model.EventWeather;
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
public class EventWeatherAPICallerServiceImpl extends APICallerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EventWeatherAPICallerServiceImpl.class);

    private EventWeatherDAO eventDAO;
    private TextMessageService textMessageService;
    private EmailService emailService;
    private UserDao userDao;
    private EventNotificationDAO eventNotificationDAO;

    @Inject
    public EventWeatherAPICallerServiceImpl(String eventUrl, Client client, EventWeatherDAO weatherEventDAO,
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
        EventWeather retEvent;

        Session session = sessionFactory.openSession();
        try {
            EventWeather event = mapper.readValue(eventJson.toString(), EventWeather.class);

            event.setGeometry(geoJson.toString());
            ManagedSessionContext.bind(session);

            Transaction transaction = session.beginTransaction();
            EventWeather eventFromDB = eventDAO.selectForUpdate(event);
            boolean bNewEvent = false;
            try {
                 event.setLastModified(eventFromDB.getLastModified());
            } catch (Exception ex) {
                LOG.info("Event is new");
                // row doesn't exist it's new... nothing wrong..
                // just ignore the exectoion
                bNewEvent = true;
            }
            LOG.info("Event to save : {}", event.toString());
            // Archive users based on last login date
            retEvent = eventDAO.update(event);
            transaction.commit();

            if(bNewEvent || !retEvent.getLastModified().equals(eventFromDB.getLastModified()) ){
                LOG.info("Event for notifications");

                List<User> users = userDao.getGeoWithinRadius(geoJson, 50.00);

                EventNotification eventNotification = new EventNotification();
                eventNotification.setCitizensAffected(users.size());
                eventNotification.setDescription("Emergency alert: "+event.getProdType()+" in your area. Please log in at https://mycalerts.com/ for more information.");
                eventNotification.setGenerationDate(new Date());
                eventNotification.setGeometry(event.getGeometry());
                eventNotification.setUrl1(event.getUrl());
                eventNotification.setType("Weather");
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
                            emailService.send(null, Arrays.asList(user.getEmail()), "Emergency alert from MyCAlerts: " + event.getProdType(),
                                eventNotification.getDescription());
                        }
                    }
                }
                eventNotificationDAO.save(eventNotification);
            }
        } catch (IOException ex) {
            LOG.error("Unable to parse the result for the weather event : error: {}", ex.getMessage());
        } finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }

    }


}
