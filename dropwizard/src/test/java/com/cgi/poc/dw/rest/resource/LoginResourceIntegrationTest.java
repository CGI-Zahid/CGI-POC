package com.cgi.poc.dw.rest.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import com.cgi.poc.dw.rest.model.LoginUserDto;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.GeneralErrors;
import java.util.HashSet;
import java.util.Set;
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

public class LoginResourceIntegrationTest extends IntegrationTest {

  private static final String url = "http://localhost:%d/login";

  @Test
  public void noArgument() {
    Client client = new JerseyClientBuilder().build();
    Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(null);

    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Missing credentials."));
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
  }

  @Test
  public void noEmail() {
    Client client = new JerseyClientBuilder().build();
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setPassword("test123");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));

    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
      // The data provided in the API call is invalid. Message: <XXXXX>
      // where XXX is the message associated to the validation
      String partString = "email  may not be null";
      String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage()
          .replace("REPLACE", partString);
      assertThat(error.getMessage()).isEqualTo(expectedErrorString);
    }
  }


  @Test
  public void noPassword() {
    Client client = new JerseyClientBuilder().build();
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail("helper@gmail.com");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));

    assertNotNull(response);
    Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    boolean bValidErr = false;
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertThat(error.getCode()).isEqualTo(GeneralErrors.INVALID_INPUT.getCode());
      // The data provided in the API call is invalid. Message: <XXXXX>
      // where XXX is the message associated to the validation
      String partString = "password  is missing";
      String expectedErrorString = GeneralErrors.INVALID_INPUT.getMessage()
          .replace("REPLACE", partString);
      if (error.getMessage().equals(expectedErrorString)) {
        bValidErr = true;
      }
    }
    assertThat(bValidErr).isEqualTo(true);
  }

  @Test
  public void userNotFound() {
    Client client = new JerseyClientBuilder().build();
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail("user.not.found@gmail.com");
    loginUserDto.setPassword("test123");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));

    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Invalid username or password."));
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
        response.getStatus());
  }

  @Test
  public void registerUserLoginUserSuccess() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    // registerUser user
    User user = new User();
    user.setEmail("success@gmail.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("California");
    user.setRequiredStreet("required street");
  	user.setOptionalStreet("optional street");
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    UserNotificationType selNot = new UserNotificationType(Long.valueOf(NotificationType.EMAIL.ordinal()));
    Set<UserNotificationType> notificationType = new HashSet<>();
    notificationType.add(selNot);
    user.setNotificationType(notificationType);

    client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
        .post(Entity.json(user));
    // login user
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail("success@gmail.com");
    loginUserDto.setPassword("test123");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));
    Assert.assertEquals(200, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("authToken")));
    Assert.assertEquals(Role.RESIDENT.toString(), responseJo.optString("role"));
  }
  
  @Test
  public void loginFailureWrongPassword() {
    Client client = new JerseyClientBuilder().build();
    // registerUser user
    User user = new User();
    user.setEmail("wrong.pw@gmail.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("California");
    user.setRequiredStreet("required street");
  	user.setOptionalStreet("optional street");
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    UserNotificationType selNot = new UserNotificationType(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotificationType> notificationType = new HashSet<>();
    notificationType.add(selNot);
    user.setNotificationType(notificationType);

    client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
        .post(Entity.json(user));
    // login user
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail("wrong.pw@gmail.com");
    loginUserDto.setPassword("wrong");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));

    assertNotNull(response);
    assertThat(response.readEntity(String.class),
        containsString("Invalid username or password."));
    assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
        response.getStatus());
  }
}
