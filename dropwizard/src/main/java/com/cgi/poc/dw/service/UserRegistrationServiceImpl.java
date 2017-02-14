package com.cgi.poc.dw.service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.auth.MyPasswordValidator;
import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotification;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.GeneralErrors;
import com.cgi.poc.dw.util.PersistValidationGroup;
import com.cgi.poc.dw.util.RestValidationGroup;
import com.google.inject.Inject;

public class UserRegistrationServiceImpl extends BaseServiceImpl implements UserRegistrationService  {

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
  private final static MyPasswordValidator myPasswordValidator = new MyPasswordValidator();
  private final UserDao userDao;
  private final PasswordHash passwordHash;

  @Inject
  public UserRegistrationServiceImpl(UserDao userDao,   PasswordHash passwordHash,
         Validator validator) {
    super(validator);
    this.userDao = userDao;
    this.passwordHash = passwordHash;
  }

  public Response registerUser(User user) {
       validate(user, "rest", RestValidationGroup.class, Default.class);
      // check if the email already exists.
    User findUserByEmail = userDao.findUserByEmail(user.getEmail());
    if (findUserByEmail != null){
        ErrorInfo errRet = new ErrorInfo();
        String errorString = GeneralErrors.DUPLICATE_ENTRY.getMessage().replace("REPLACE",  "email");
        errRet.addError(GeneralErrors.DUPLICATE_ENTRY.getCode(), errorString );
        throw new WebApplicationException(Response.noContent().status(Response.Status.BAD_REQUEST).entity(errRet).build());
    }
    String hash = null;
    try {
      hash = passwordHash.createHash(user.getPassword());
      user.setPassword(hash);
    } catch (Exception e) {
      LOG.error("Unable to create a password hash.", e);
      throw new InternalServerErrorException("Unable to create a password hash.");
    }

    try {
      validate(user, "create", Default.class, PersistValidationGroup.class);
      for(UserNotification notificationType : user.getNotificationType()){
          notificationType.setUserId(user);
      }
      User retUser= userDao.create(user); 

    }
    catch (ConstraintViolationException exception) {
        throw exception;
    }
    return Response.ok().build();

  }

  private User createUser(User userIn, String hash) {
    User user = new User();
    user.setFirstName(user.getFirstName());
    user.setLastName(user.getLastName());
    user.setEmail(user.getEmail());
    user.setPassword(hash);
    user.setRole(Role.RESIDENT.name());
    user.setNotificationType(userIn.getNotificationType());
    user.setPhone(userIn.getPhone());
    user.setZipCode(userIn.getZipCode());
    //TODO: call external API to populate these based on ZipCode
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    return user;
  }
}