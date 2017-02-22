/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.dao.EventNotificationDAO;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationZipcode;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import com.cgi.poc.dw.service.EmailService;
import com.cgi.poc.dw.service.TextMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

public class FireEventAPICallerServiceImpl extends APICallerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(FireEventAPICallerServiceImpl.class);

    private FireEventDAO eventDAO;
    private TextMessageService textMessageService;
    private EmailService emailService;
    private FireEvent eventCompare;
    private FireEvent retEvent;
    private Date eventCompareDate;
    private Date retEventDate;
    private UserDao userDao;
    private EventNotificationDAO eventNotificationDAO;

    @Inject
    public FireEventAPICallerServiceImpl(String eventUrl, Client client, FireEventDAO fireEventDAO,
          SessionFactory sessionFactory, TextMessageService textMessageService,
          EmailService emailService, UserDao userDao, EventNotificationDAO eventNotificationDAO) {
        super(eventUrl, client, sessionFactory);
        eventDAO = fireEventDAO;
        this.textMessageService = textMessageService;
        this.emailService = emailService;
        this.userDao = userDao;
        this.eventNotificationDAO = eventNotificationDAO;
    }

    public void mapAndSave(JsonNode eventJson, JsonNode geoJson) {
        ObjectMapper mapper = new ObjectMapper();

        Session session = sessionFactory.openSession();
        try {
            FireEvent event = mapper.readValue(eventJson.toString(), FireEvent.class);

            event.setGeometry(geoJson.toString());
            ManagedSessionContext.bind(session);

            try {
                eventCompare = (eventDAO).findById(event.getUniquefireidentifier());
                eventCompareDate = eventCompare.getLastModified();
            } catch (Exception e) {
                LOG.error("Unable to calculate eventCompareDate : error: {}", e.getMessage());
            }

            Transaction transaction = session.beginTransaction();
            try {
                LOG.info("Event to save : {}", event.toString());
                // Archive users based on last login date
                retEvent = ((FireEventDAO) eventDAO).save(event);
                transaction.commit();
                retEventDate = retEvent.getLastModified();
            } catch (Exception e) {
                transaction.rollback();
                LOG.error("Unable to save event : error: {}", e.getMessage());
            }

            try {
                if(retEventDate != null && eventCompareDate.compareTo(retEventDate) != 0){
                    LOG.info("Process event for notifications");

                    GeoCoordinates geo = new GeoCoordinates();
                    geo.setLatitude(event.getLatitude().doubleValue());
                    geo.setLongitude(event.getLongitude().doubleValue());

                    List<User> users = userDao.getGeoWithinRadius(Arrays.asList(geo), 50.00);

                    LOG.info("Send notifications to : {}", users.toString());

                    for (User user : users) {
                        for (UserNotificationType userNotification : user.getNotificationType()){
                            if(userNotification.getNotificationId() == NotificationType.SMS.ordinal()){
                                textMessageService.send(user.getPhone(), "Emergency alert: "
                                    +"Fire near "+event.getIncidentname()+" in your area. Please log in at <our site> for more information.");
                            }else if(userNotification.getNotificationId() == NotificationType.EMAIL.ordinal()){
                                emailService.send(null, Arrays.asList(user.getEmail()), "Emergency alert from MyCAlerts: FireEvent",
                                    "Emergency alert: Fire near "+event.getIncidentname()+" in your area. Please log in at <our site> for more information.");
                            }
                        }
                    }

                    EventNotification eventNotification = new EventNotification();
                    eventNotification.setCitizensAffected(users.size());
                    eventNotification.setDescription("Emergency alert: Fire near "+event.getIncidentname()+" in your area. Please log in at <our site> for more information.");
                    eventNotification.setGenerationDate(new Date());
                    eventNotification.setGeometry(event.getGeometry());
                    eventNotification.setUrl1(event.getHotlink());
                    eventNotification.setType("AUTOMATED");
                    eventNotification.setUserId(userDao.getAdminUser());
                    eventNotification.setId(Long.valueOf(0));

                    EventNotificationZipcode zipcode = new EventNotificationZipcode();
                    zipcode.setZipCode("00000");
                    Set<EventNotificationZipcode> eventNotificationZipcode = new HashSet<>();
                    eventNotificationZipcode.add(zipcode);

                    eventNotification.setEventNotificationZipcodes(eventNotificationZipcode);

                    try {
                        eventNotificationDAO.save(eventNotification);
                    }catch (Exception e) {
                        LOG.error("Failing during saving event : error: {}", e.getMessage());
                    }
                }else{
                    LOG.info("Event last modified not changed");
                }
            } catch (Exception e) {
                LOG.error("Unable to process event for automated alerts : error: {}", e.getMessage());
            }
        } catch (IOException ex) {
            LOG.error("Unable to parse the result for the fire event : error: {}", ex.getMessage());
        } finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }

    }

}
