package com.cgi.poc.dw.rest.resource;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.UserDto;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.helper.IntegrationTestHelper;
import com.cgi.poc.dw.util.Error;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.GeneralErrors;
import com.cgi.poc.dw.util.ValidationErrors;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserResourceIntegrationTest extends IntegrationTest {

  private static final String url = "http://localhost:%d/user";

  private UserDto tstUser;

  private static GreenMail smtpServer;

  @BeforeClass
  public static void createUser() throws SQLException {
    IntegrationTestHelper.signupResidentUser();
    smtpServer = new GreenMail(new ServerSetup(3025, "127.0.0.1",
        ServerSetup.PROTOCOL_SMTP));
    smtpServer.start();
  }

  @Before
  public void initUser() {
  	tstUser = new UserDto();
  	tstUser.setEmail("resident@cgi.com");
  	tstUser.setPassword("!QAZ1qaz");
  	tstUser.setFirstName("john");
  	tstUser.setLastName("doe");
  	tstUser.setRole(Role.RESIDENT.name());
  	tstUser.setPhone("1234567890");
  	tstUser.setZipCode("95814");
  	tstUser.setCity("Sacramento");
  	tstUser.setState("CA");
  	tstUser.setAddress1("required street");
  	tstUser.setAddress2("optional street");
  	tstUser.setLatitude(38.5824933);
  	tstUser.setLongitude(-121.4941738);
    tstUser.setEmailNotification(true);
    tstUser.setSmsNotification(false);
    tstUser.setPushNotification(false);
  }

  @After
  public void exit() {
  }

  @AfterClass
  public static void cleanup() {
    IntegrationTestHelper.cleanDbState();
    if (smtpServer != null) {
      smtpServer.stop();
    }
  }

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
    tstUser.setEmail(null);

    tstUser.setEmailNotification(true);


    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(tstUser));
    assertInvalidEmail(response);
  }

  @Test
  public void invalidEmail() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("invalidEmail");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.json(tstUser));

    assertInvalidEmail(response);
  }
  
  @Test
  public void noPassword() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("nopass@gmail.com");
    tstUser.setPassword(null);

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));

    assertInvalidPassword(response);
  }
  
  @Test
  public void invalidPasswordTooShort() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("invalidpass@gmail.com");
    tstUser.setPassword("a");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));

    assertInvalidPassword(response);
  }

  @Test
  public void invalidPasswordContainsWhiteSpace() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("success123@gmail.com");
    tstUser.setPassword("abcd abcd");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));

    assertInvalidPassword(response);
  }

  @Test
  public void invalidPasswordNoAlphabeticalCharacters() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("success11@gmail.com");
    tstUser.setPassword("123");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));

    assertInvalidPassword(response);
  }
  
  @Test
  public void signupSuccess() throws MessagingException {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("random_mail12@gmail.com");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    Assert.assertEquals(200, response.getStatus());

    //verify email registration
    smtpServer.waitForIncomingEmail(7000,1);

    MimeMessage[] receivedMails = smtpServer.getReceivedMessages();
    assertEquals( "Should have received 0 email.", 0, receivedMails.length);
}

  @Test
  public void updateSuccessWithoutPasswordChange() throws MessagingException {
    Client client = new JerseyClientBuilder().build();
    tstUser.setPassword("");
    tstUser.setFirstName("Jane");
    String authToken = IntegrationTestHelper.getAuthToken("resident@cgi.com", "!QAZ1qaz", RULE);
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        put(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    Assert.assertEquals(200, response.getStatus());
  }

  @Test
  public void invalidPhoneNumber() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("invalidphonenumber1@gmail.com");
    tstUser.setPhone("44343");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), is(ValidationErrors.INVALID_PHONE));
    }

 }

  @Test
  public void invalidZipCode() throws JSONException {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("invalidzipcode@gmail.com");
    tstUser.setZipCode("983");

    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), is(ValidationErrors.INVALID_ZIPCODE));
    }
  }

  @Test
  public void signupUserAlreadyExist() {
    Client client = new JerseyClientBuilder().build();
    tstUser.setEmail("resident@cgi.com");
    Response response = client.target(String.format(url, RULE.getLocalPort())).request()
        .post(Entity.entity(tstUser, MediaType.APPLICATION_JSON_TYPE));
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (com.cgi.poc.dw.util.Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), is(ValidationErrors.DUPLICATE_USER));
    }
  }

  
  private void assertInvalidEmail(Response response) {
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), anyOf(is(ValidationErrors.INVALID_EMAIL), anyOf(is(ValidationErrors.MISSING_EMAIL))));
    }
  }

  private void assertInvalidPassword(Response response) {
    assertNotNull(response);
    assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = response.readEntity(ErrorInfo.class);
    for (Error error : errorInfo.getErrors()) {
      assertEquals(error.getCode(), Integer.toString(Status.BAD_REQUEST.getStatusCode()));
      assertThat(error.getMessage(), anyOf(is(ValidationErrors.INVALID_PASSWORD), anyOf(is(ValidationErrors.MISSING_PASSWORD))));
    }
  }

}
