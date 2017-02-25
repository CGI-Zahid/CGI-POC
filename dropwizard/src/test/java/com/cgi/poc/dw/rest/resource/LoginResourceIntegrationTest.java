package com.cgi.poc.dw.rest.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.UserDto;
import com.cgi.poc.dw.rest.model.LoginUserDto;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.ValidationErrors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
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
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), anyOf(is(ValidationErrors.INVALID_EMAIL), anyOf(is(ValidationErrors.MISSING_EMAIL))));
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
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), anyOf(is(ValidationErrors.INVALID_PASSWORD), anyOf(is(ValidationErrors.MISSING_PASSWORD))));
    }
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
    UserDto user = new UserDto();
    user.setEmail("success@gmail.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("CA");
    user.setAddress1("required street");
    user.setAddress2("optional street");
    user.setEmailNotification(true);
    user.setSmsNotification(false);

    Response newUserResponse = client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
        .post(Entity.json(user));
    assertEquals(200, newUserResponse.getStatus());

    // login user
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail("success@gmail.com");
    loginUserDto.setPassword("test123");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));
    assertEquals(200, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    assertTrue(!StringUtils.isBlank(responseJo.optString("authToken")));
    assertEquals(Role.RESIDENT.toString(), responseJo.optString("role"));
  }

  @Test
  public void loginFailureWrongPassword() {
    Client client = new JerseyClientBuilder().build();
    // registerUser user
    UserDto user = new UserDto();
    user.setEmail("wrong.pw@gmail.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("CA");
    user.setAddress1("required street");
  	user.setAddress2("optional street");
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    user.setSmsNotification(true);


    client.target(String.format("http://localhost:%d/profile", RULE.getLocalPort())).request()
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
