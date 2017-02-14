package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Validation;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotification;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationServiceUnitTest {

  @Mock
  private UserDao userDao;
   
  @Mock
  private PasswordHash passwordHash;

  @InjectMocks
  UserRegistrationServiceImpl underTest;
  
  @Mock
  private Validation validator;
  
  @Test
  public void registerUser_shouldRegisterUserWhenGivenValidInput() throws Exception {

    User userRegistrationDto = new User();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setRole(Role.RESIDENT.name());
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    userRegistrationDto.setNotificationType(notificationType);
    
    User newUser=null;
    Long notificationMethodId = new Long(2);
    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:" 
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(userRegistrationDto.getPassword())).thenReturn(saltedHash);
    when(userDao.create(any(User.class))).thenReturn(newUser);
    Response actual = underTest.registerUser(userRegistrationDto);
    
    assertEquals(200, actual.getStatus());
  }

  @Test
  public void registerUser_shouldThrowExceptionWhenCreateUserFails() throws Exception {

    User userRegistrationDto = new User();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setRole(Role.RESIDENT.name());
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    userRegistrationDto.setNotificationType(notificationType);
    
    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(userRegistrationDto.getPassword())).thenReturn(saltedHash);

    doThrow(new Exception("User already exists")).when(userDao).create(any(User.class));
    try {
      underTest.registerUser(userRegistrationDto);
      fail("Expected ConflictException");
    }
    catch (BadRequestException exception) {
      assertEquals(400, exception.getResponse().getStatus());
      assertEquals(exception.getMessage(),
          "Unable to create user as user may already exists.");
    }
  }


  @Test
  public void registerUser_shouldThrowExceptionWhenPasswordHashingFails() throws Exception {

    User userRegistrationDto = new User();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setRole(Role.RESIDENT.name());
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    userRegistrationDto.setNotificationType(notificationType);
    
    
    doThrow(new InvalidKeySpecException("Error")).when(passwordHash).createHash(userRegistrationDto.getPassword());
    try {
      underTest.registerUser(userRegistrationDto);
      fail("Expected ConflictException");
    }
    catch (InternalServerErrorException exception) {
      assertEquals(500, exception.getResponse().getStatus());
      assertEquals(exception.getMessage(),
          "Unable to create a password hash.");
    }
  }

}
