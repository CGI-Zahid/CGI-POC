/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import com.cgi.poc.dw.service.EmailService;
import com.cgi.poc.dw.service.SearchUserService;
import com.cgi.poc.dw.service.TextMessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
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

public class FireEventAPICallerServiceImpl extends APICallerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(FireEventAPICallerServiceImpl.class);

    private FireEventDAO eventDAO;
    private SearchUserService searchUserService;
    private TextMessageService textMessageService;
    private EmailService emailService;
    private FireEvent eventCompare;
    private FireEvent retEvent;
    private Date eventCompareDate;
    private Date retEventDate;

    @Inject
    public FireEventAPICallerServiceImpl(String eventUrl, Client client, FireEventDAO fireEventDAO,
          SessionFactory sessionFactory, SearchUserService searchUserService, TextMessageService textMessageService,
          EmailService emailService) {
        super(eventUrl, client, sessionFactory);
        eventDAO = fireEventDAO;
        this.searchUserService = searchUserService;
        this.textMessageService = textMessageService;
        this.emailService = emailService;
    }

    public void mapAndSave(JsonNode eventJson, JsonNode geoJson) {
        ObjectMapper mapper = new ObjectMapper();

        Session session = sessionFactory.openSession();
        try {
            FireEvent event = mapper.readValue(eventJson.toString(), FireEvent.class);

            event.setGeometry(geoJson.toString());
            ManagedSessionContext.bind(session);

            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            } catch (Exception e) {
                transaction.rollback();
                LOG.error("Unable to save event : error: {}", e.getMessage());
            }

            try {
                retEventDate = retEvent.getLastModified();

                LOG.info(formatter.format(eventCompareDate));
                LOG.info(formatter.format(retEventDate));

                if(eventCompareDate.compareTo(retEventDate) != 0){
                    LOG.info("Process event for notifications");

                    GeoCoordinates geo = new GeoCoordinates();
                    geo.setLatitude(event.getLatitude().doubleValue());
                    geo.setLongitude(event.getLongitude().doubleValue());

                    List<User> users = searchUserService.search(Arrays.asList(geo),50.00);

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
