package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.UserNotificationDao;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.skife.jdbi.v2.exceptions.UnableToCreateSqlObjectException;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationServiceUnitTest {

  @Mock
  private UserDao userDao;
  
  @Mock
  private UserNotificationDao userNotificationDao;
  
  @Mock
  private PasswordHash passwordHash;

  @InjectMocks
  UserRegistrationServiceImpl underTest;
  
  
  @Test
  public void registerUser_shouldRegisterUserWhenGivenValidInput() throws Exception {

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    
    Long userId = new Long(1);
    Long notificationMethodId = new Long(2);
    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:" 
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(userRegistrationDto.getPassword())).thenReturn(saltedHash);
    when(userDao.createUser(any(User.class))).thenReturn(userId);
    when(userNotificationDao.findNotificationMethodIdByName("SMS")).thenReturn(notificationMethodId);
    doNothing().when(userNotificationDao).createUserNotification(anyLong(), anyList());
    Response actual = underTest.registerUser(userRegistrationDto);
    
    assertEquals(200, actual.getStatus());
  }

  @Test
  public void registerUser_shouldThrowExceptionWhenCreateUserFails() throws Exception {

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    
    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(userRegistrationDto.getPassword())).thenReturn(saltedHash);

    doThrow(new Exception("User already exists")).when(userDao).createUser(any(User.class));
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
  public void registerUser_shouldThrowExceptionWhenNotificationDaoFails() throws Exception {

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    
    doThrow(new UnableToCreateSqlObjectException("")).when(userNotificationDao).findNotificationMethodIdByName("SMS");
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

    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

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
