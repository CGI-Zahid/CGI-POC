package com.cgi.poc.dw.service;

import com.cgi.poc.dw.dao.EventNotificationDAO;
import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationZipcode;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.ErrorInfoHelper;
import com.google.inject.Inject;
import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventNotificationServiceImpl extends BaseServiceImpl implements EventNotificationService {

  private final static Logger LOG = LoggerFactory.getLogger(EventNotificationServiceImpl.class);

  private EventNotificationDAO eventNotificationDAO;

  @Inject
  public EventNotificationServiceImpl(EventNotificationDAO eventNotificationDAO, Validator validator) {
    super(validator);
    this.eventNotificationDAO = eventNotificationDAO;
  }
  @Override
    public Response retrieveAllNotifications(User user) {
        List<EventNotification> resultList = eventNotificationDAO.retrieveAll();
        Response.ResponseBuilder respBuilder = Response.noContent().status(Response.Status.OK);
        respBuilder.header("X-Total-Count", Integer.valueOf(resultList.size()).toString());
        return respBuilder.entity(resultList).build();

    }

  @Override
  public Response publishNotification(User user, EventNotification eventNotification) {

    try {
      eventNotification.setUserId(user);
      for (EventNotificationZipcode eventNotificationZipcode : eventNotification
          .getEventNotificationZipcodes()) {
        eventNotificationZipcode.setEventNotificationId(eventNotification);
      }
      validate(eventNotification, "eventNotification validation", Default.class);
      eventNotification = eventNotificationDAO.save(eventNotification);
    } catch (ConstraintViolationException exception) {
      throw exception;
    } catch (Exception exception) {
      LOG.error("Unable to publish a notification for user {}.", user.getEmail(), exception);
      ErrorInfo errRet = ErrorInfoHelper.getUnknownExceptionErrorMessage(exception);
      return Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build();
    }
    return Response.ok().entity(eventNotification).build();
  }
}
