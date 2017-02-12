package com.cgi.poc.dw.rest.resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotification;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.Error;
import com.cgi.poc.dw.util.GeneralErrors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class UserRegistrationResourceIntegrationTest extends IntegrationTest {

  private static final String url = "http://localhost:%d/register";

  @Test
  public void noArgument() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(null);
    Assert.assertEquals(422, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("errors")));
    Assert.assertEquals("[\"The request body may not be null\"]", responseJo.optString("errors"));

  }

  @Test
  public void noEmail() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(tstUser));
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "email  may not be null";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }

  }

  @Test
  public void invalidEmail() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("invalidEmail");
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(tstUser));

    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "email  Invalid email address.";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }


  }

  @Test
  public void noPassword() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("helper@gmail.com");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    boolean bValidErr = false;
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "password  is missing";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        if (error.getMessage().equals(expectedErrorString)){
            bValidErr = true;
        }
    }
        assertThat(bValidErr).isEqualTo(true);

  }

  @Test
  public void invalidPasswordTooShort() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("helper@gmail.com");
    tstUser.setPassword("a");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    
    assertNotNull(response);

    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    // this should fail 2 validations.. size and pwd validity
    // this test is specific to validity.. 
    boolean bValidErr = false;
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "password  must be at least 2 characters in length.";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        if (error.getMessage().equals(expectedErrorString)){
            bValidErr = true;
        }
    }
        assertThat(bValidErr).isEqualTo(true);
    
  }

  @Test
  public void invalidPasswordContainsWhiteSpace() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("helper@gmail.com");
    tstUser.setPassword("abcd abcd");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
        UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    
    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "password  must be greate that 2 character, contain no whitespace, and have at least one number and one letter.";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }

  }

  @Test
  public void invalidPasswordNoAlphabeticalCharacters() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("helper@gmail.com");
    tstUser.setPassword("123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
   // tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    
    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "password  must be greate that 2 character, contain no whitespace, and have at least one number and one letter.";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }
  }

  //@Test
  public void signupSuccess() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("success@gmail.com");
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
    //tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);
    
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
     Assert.assertEquals(200, response.getStatus());
  }

  @Test
  public void invalidPhoneNumber() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("success@gmail.com");
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
   // tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    //less than 10 digits
    tstUser.setPhone("123456789");
    tstUser.setZipCode("98765");
    UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "phone  size must be between 10 and 10";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }
 
  }


  @Test
  public void invalidZipCode() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("success@gmail.com");
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
   // tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    //less than 5 digits
    tstUser.setZipCode("9875");
        UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String partString  = "zipCode  must match \"\\d{5}\"";
        String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage().replace("REPLACE", partString);
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }
  }

  @Test
  public void signupUserAlreadyExist() {
    Client client = new JerseyClientBuilder().build();
    User tstUser = new User();
    tstUser.setEmail("duplicate@gmail.com");
    tstUser.setPassword("test123");
    tstUser.setFirstName("john");
    tstUser.setLastName("smith");
   // tstUser.setNotificationType(Arrays.asList(NotificationType.SMS));
    tstUser.setRole(Role.RESIDENT.toString());
    tstUser.setPhone("1234567890");
    tstUser.setZipCode("98765");
        UserNotification selNot = new UserNotification(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotification> notificationType = new HashSet<>();
    notificationType.add(selNot);
    tstUser.setNotificationType(notificationType);

    Response response1 = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
        assertThat(error.getCode()).isEqualTo(GeneralErrors.DUPLICATE_ENTRY.getCode());
        // The data provided in the API call is invalid. Message: <XXXXX>
        // where XXX is the message associated to the validation
        String expectedErrorString =  GeneralErrors.DUPLICATE_ENTRY.getMessage().replace("REPLACE",  "email");

        String tmp = error.getMessage();
        assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }
  }
}
