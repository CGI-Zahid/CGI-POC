package com.cgi.poc.dw.rest.resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import java.util.Arrays;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
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
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Missing email."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void invalidEmail() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("invalidEmail");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Invalid email address."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void noPassword() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("helper@gmail.com");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Missing password."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void invalidPasswordTooShort() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("helper@gmail.com");
    userRegistrationDto.setPassword("a");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Password must be at least 2 characters in length."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void invalidPasswordContainsWhiteSpace() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("helper@gmail.com");
    userRegistrationDto.setPassword("abcd abcd");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Password cannot contain whitespace characters."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void invalidPasswordNoAlphabeticalCharacters() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("helper@gmail.com");
    userRegistrationDto.setPassword("123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Password must contain at least 1 alphabetical characters."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void signupSuccess() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    Assert.assertEquals(200, response.getStatus());
  }

  @Test
  public void invalidPhoneNumber() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    //less than 10 digits
    userRegistrationDto.setPhone("123456789");
    userRegistrationDto.setZipCode("98765");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Invalid phone number."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }


  @Test
  public void invalidZipCode() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("success@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    //less than 5 digits
    userRegistrationDto.setZipCode("9875");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Invalid zip code."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void signupUserAlreadyExist() {
    Client client = new JerseyClientBuilder().build();
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("duplicate@gmail.com");
    userRegistrationDto.setPassword("test123");
    userRegistrationDto.setFirstName("john");
    userRegistrationDto.setLastName("smith");
    userRegistrationDto.setNotificationType(Arrays.asList(NotificationType.SMS));
    userRegistrationDto.setRole(Role.RESIDENT);
    userRegistrationDto.setPhone("1234567890");
    userRegistrationDto.setZipCode("98765");

    client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(userRegistrationDto));
    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Unable to create user as user may already exists."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }
}
