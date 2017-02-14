package com.cgi.poc.dw.service;

import com.cgi.poc.dw.MapApiConfiguration;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotification;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.GeneralErrors;
import com.cgi.poc.dw.util.PersistValidationGroup;
import com.cgi.poc.dw.util.RestValidationGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationServiceImpl extends BaseServiceImpl implements
    UserRegistrationService {

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

  private final UserDao userDao;

  private final PasswordHash passwordHash;

  private Client client;

  private MapApiConfiguration mapApiConfiguration;

  //The name of the query param for the 
  private static final String ADDRESS = "address";


  @Inject
  public UserRegistrationServiceImpl(MapApiConfiguration mapApiConfiguration, UserDao userDao,
      PasswordHash passwordHash,
      Validator validator, Client client) {
    super(validator);
    this.userDao = userDao;
    this.passwordHash = passwordHash;
    this.client = client;
    this.mapApiConfiguration = mapApiConfiguration;

  }

  public Response registerUser(User user) {
    //Defaulting the user to RESIDENT
    user.setRole("RESIDENT");
    
    validate(user, "rest", RestValidationGroup.class, Default.class);
    // check if the email already exists.
    User findUserByEmail = userDao.findUserByEmail(user.getEmail());
    if (findUserByEmail != null) {
      ErrorInfo errRet = new ErrorInfo();
      String errorString = GeneralErrors.DUPLICATE_ENTRY.getMessage().replace("REPLACE", "email");
      errRet.addError(GeneralErrors.DUPLICATE_ENTRY.getCode(), errorString);
      return Response.noContent().status(Response.Status.BAD_REQUEST).entity(errRet).build();
    }
    String hash = null;
    try {
      hash = passwordHash.createHash(user.getPassword());
      user.setPassword(hash);
    } catch (Exception exception) {
      LOG.error("Unable to create a password hash.", exception);
      ErrorInfo errRet = getInternalErrorInfo(exception, GeneralErrors.UNKNOWN_EXCEPTION);
      return Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build();
    }

    setUserGeoCoordinates(user);

    try {
      validate(user, "save", Default.class, PersistValidationGroup.class);
      for (UserNotification notificationType : user.getNotificationType()) {
        notificationType.setUserId(user);
      }
      
      userDao.save(user);
      
    } catch (ConstraintViolationException exception) {
      throw exception;
    } catch (Exception exception) {
      LOG.error("Unable to save a user.", exception);
      ErrorInfo errRet = getInternalErrorInfo(exception, GeneralErrors.UNKNOWN_EXCEPTION);
      return Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build();
    }
    return Response.ok().entity(user).build();
  }

  //invoke Google Maps API to retrieve latitude and longitude by zipCode
  private void setUserGeoCoordinates(User user) {
    try {
      String response = client
          .target(mapApiConfiguration.getApiURL())
          .queryParam(ADDRESS, user.getZipCode())
          .request(MediaType.APPLICATION_JSON)
          .get(String.class);

      final ObjectNode node = new ObjectMapper().readValue(response, ObjectNode.class);

      if (node.path("results").size() > 0 && "OK".equals(node.path("status").asText())) {
        user.setLatitude(node.get("results").get(0).get("geometry").get("location").get("lat").asDouble());
        user.setLongitude(node.get("results").get(0).get("geometry").get("location").get("lng").asDouble());
      } else {
        user.setLatitude(0.0);
        user.setLongitude(0.0);
      }
    } catch (Exception exception) {
      LOG.error("Unable to make maps api call.", exception);
      ErrorInfo errRet = getInternalErrorInfo(exception, GeneralErrors.UNKNOWN_EXCEPTION);
      throw new WebApplicationException(
          Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build());
    }
  }

  private ErrorInfo getInternalErrorInfo(Exception exception, GeneralErrors generalErrors) {
    ErrorInfo errRet = new ErrorInfo();
    String message = generalErrors.getMessage();
    String errorString = message.replace("REPLACE1", exception.getClass().getCanonicalName())
        .replace("REPLACE2", exception.getMessage());
    errRet.addError(generalErrors.getCode(), errorString);
    return errRet;
  }
}
