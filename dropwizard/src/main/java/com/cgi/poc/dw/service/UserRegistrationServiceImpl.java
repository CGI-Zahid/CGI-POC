package com.cgi.poc.dw.service;

import com.cgi.poc.dw.api.service.MapsApiService;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.ErrorInfoHelper;
import com.cgi.poc.dw.util.GeneralErrors;
import com.cgi.poc.dw.util.PersistValidationGroup;
import com.cgi.poc.dw.util.RestValidationGroup;
import com.google.inject.Inject;
import java.util.Arrays;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationServiceImpl extends BaseServiceImpl implements
    UserRegistrationService {

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

  private final UserDao userDao;

  private final PasswordHash passwordHash;

  private MapsApiService mapsApiService;

  private final EmailService emailService;

  private final TextMessageService textMessageService;

  //The name of the query param for the 
  private static final String ADDRESS = "address";


  @Inject
  public UserRegistrationServiceImpl(MapsApiService mapsApiService, UserDao userDao,
      PasswordHash passwordHash, Validator validator, EmailService emailService,
      TextMessageService textMessageService) {
    super(validator);
    this.userDao = userDao;
    this.passwordHash = passwordHash;
    this.mapsApiService = mapsApiService;
    this.emailService = emailService;
    this.textMessageService = textMessageService;
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
    try {
      user.setPassword(passwordHash.createHash(user.getPassword()));

      GeoCoordinates geoCoordinates = mapsApiService.getGeoCoordinatesByZipCode(user.getZipCode());
      user.setLatitude(geoCoordinates.getLatitude());
      user.setLongitude(geoCoordinates.getLongitude());

      validate(user, "save", Default.class, PersistValidationGroup.class);
      for (UserNotificationType notificationType : user.getNotificationType()) {
        notificationType.setUserId(user);
      }

      userDao.save(user);

      //Future TODO enhancement: make the subject and email body configurable
      emailService.send(null, Arrays.asList(user.getEmail()), "Registration confirmation",
          "Hello there, thank you for registering.");
      textMessageService.send(user.getPhone(), "MyCAlerts: Thank you for registering.");

    } catch (ConstraintViolationException exception) {
      throw exception;
    } catch (Exception exception) {
      LOG.error("Unable to save a user.", exception);
      ErrorInfo errRet = ErrorInfoHelper.getUnknownExceptionErrorMessage(exception);
      return Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build();
    }
    return Response.ok().entity(user).build();
  }
}
