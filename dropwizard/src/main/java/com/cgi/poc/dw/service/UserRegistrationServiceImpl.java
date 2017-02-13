package com.cgi.poc.dw.service;

import com.cgi.poc.dw.auth.MyPasswordValidator;
import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.UserNotificationDao;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import com.cgi.poc.dw.rest.model.validator.UserRegistrationDtoValidator;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationServiceImpl implements UserRegistrationService {

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
  private final static MyPasswordValidator myPasswordValidator = new MyPasswordValidator();
  private final UserDao userDao;
  private final UserNotificationDao userNotificationDao;
  private final PasswordHash passwordHash;

  @Inject
  public UserRegistrationServiceImpl(UserDao userDao, UserNotificationDao userNotificationDao, PasswordHash passwordHash) {
    this.userDao = userDao;
    this.userNotificationDao = userNotificationDao;
    this.passwordHash = passwordHash;
  }

  public Response registerUser(UserRegistrationDto userRegistrationDto) {
    UserRegistrationDtoValidator.validate(userRegistrationDto, myPasswordValidator);
    String hash = null;
    try {
      hash = passwordHash.createHash(userRegistrationDto.getPassword());
    } catch (Exception e) {
      LOG.error("Unable to create a password hash.", e);
      throw new InternalServerErrorException("Unable to create a password hash.");
    }

    User user = createUser(userRegistrationDto, hash);

    try {
      long id = userDao.createUser(user);
      List<Long> notificationTypes = new ArrayList<>();
      for(NotificationType notificationType : userRegistrationDto.getNotificationType()){
        long notificationMethodId = userNotificationDao.findNotificationMethodIdByName(notificationType.name());
        notificationTypes.add(notificationMethodId);
      }
      userNotificationDao.createUserNotification(id, notificationTypes);
    } catch (Exception exception) {
      String msg = "Unable to create user as user may already exists.";
      LOG.warn(msg, exception);
      throw new BadRequestException(msg);
    }
    return Response.ok().build();
  }

  private User createUser(UserRegistrationDto userRegistrationDto, String hash) {
    User user = new User();
    user.setFirstName(userRegistrationDto.getFirstName());
    user.setLastName(userRegistrationDto.getLastName());
    user.setEmail(userRegistrationDto.getEmail());
    user.setPassword(hash);
    user.setRole(Role.RESIDENT);
    user.setNotificationType(userRegistrationDto.getNotificationType());
    user.setPhone(userRegistrationDto.getPhone());
    user.setZipCode(userRegistrationDto.getZipCode());
    //TODO: call external API to populate these based on ZipCode
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    return user;
  }
}
