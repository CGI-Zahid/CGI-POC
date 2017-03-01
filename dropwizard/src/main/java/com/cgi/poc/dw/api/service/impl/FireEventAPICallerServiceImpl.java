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
import com.cgi.poc.dw.dao.model.EventNotificationUser;
import com.cgi.poc.dw.dao.model.FireEvent;
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
 * @author dawna.floyd
 */

public class FireEventAPICallerServiceImpl extends APICallerServiceImpl {

  private static final Logger LOG = LoggerFactory.getLogger(FireEventAPICallerServiceImpl.class);

  private FireEventDAO eventDAO;
  private TextMessageService textMessageService;
  private EmailService emailService;
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
    FireEvent retEvent;

    Session session = sessionFactory.openSession();
    try {
      FireEvent event = mapper.readValue(eventJson.toString(), FireEvent.class);

      event.setGeometry(geoJson.toString());
      ManagedSessionContext.bind(session);

      Transaction transaction = session.beginTransaction();
      FireEvent eventFromDB = eventDAO.selectForUpdate(event);
      boolean isNewEvent = false;
      try {
        event.setLastModified(eventFromDB.getLastModified());
        //this is to protect from the Hibernate ObjectNotFoundException when the eventFromDB is a proxy object
      } catch (Exception ex) {
        LOG.info("Event is new");
        // row doesn't exist it's new... nothing wrong..
        // just ignore the exception
        isNewEvent = true;
      }
      LOG.info("Event to save : {}", event.toString());
      // Archive users based on last login date
      retEvent = eventDAO.save(event);
      transaction.commit();

      if (isNewEvent || isChangedEvent(retEvent, eventFromDB)) {
        LOG.info("Event for notifications");

        GeoCoordinates geo = new GeoCoordinates();
        geo.setLatitude(event.getLatitude().doubleValue());
        geo.setLongitude(event.getLongitude().doubleValue());

        List<User> users = userDao.getGeoWithinRadius(Arrays.asList(geo), 15.00);

                    EventNotification eventNotification = new EventNotification();
                    eventNotification.setCitizensAffected(users.size());
                    eventNotification.setDescription("Emergency alert: Fire near "+event.getIncidentname()+" in your area. Please log in at https://mycalerts.com/ for more information.");
                    eventNotification.setGenerationDate(new Date());
                    eventNotification.setGeometry(event.getGeometry());
                    eventNotification.setUrl1(event.getHotlink());
                    eventNotification.setType("Fire");
                    eventNotification.setUserId(userDao.getAdminUser());

        if (users.size() > 0) {

          LOG.info("Send notifications to : {}", users.toString());
          for (User user : users) {
            EventNotificationUser currENUser = new EventNotificationUser();
            currENUser.setUserId(user);
            eventNotification.addNotifiedUser(currENUser);

            if (user.getSmsNotification()) {
              textMessageService.send(user.getPhone(), eventNotification.getDescription());
            }
            if (user.getEmailNotification()) {
              emailService.send(null, Arrays.asList(user.getEmail()),
                  "Emergency alert from MyCAlerts: Fire near " + event.getIncidentname(),
                  eventNotification.getDescription());
            }
          }
        }
        eventNotificationDAO.save(eventNotification);
      }
    } catch (IOException ex) {
      LOG.error("Unable to parse the result for the fire event : error: {}", ex.getMessage());
    } finally {
      session.close();
      ManagedSessionContext.unbind(sessionFactory);
    }

  }

  private boolean isChangedEvent(FireEvent retEvent, FireEvent eventFromDB) {
    return !retEvent.getLastModified().equals(eventFromDB.getLastModified());
  }

}
