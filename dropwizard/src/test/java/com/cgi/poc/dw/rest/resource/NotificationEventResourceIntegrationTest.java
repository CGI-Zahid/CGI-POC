package com.cgi.poc.dw.rest.resource;

import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.dao.model.EventNotificationZipcode;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.helper.IntegrationTestHelper;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NotificationEventResourceIntegrationTest extends IntegrationTest {

  private static final String url = "http://localhost:%d/notification";

  private EventNotification eventNotification;

  @BeforeClass
  public static void createAdminUser() throws SQLException {
    IntegrationTestHelper.signupAdminUser();
  }

  @Before
  public void createEventNotification() throws IOException {
    Set<EventNotificationZipcode> eventNotificationZipcodes = new LinkedHashSet<>();
    EventNotificationZipcode eventNotificationZipcode1 = new EventNotificationZipcode();
    eventNotificationZipcode1.setZipCode("92105");
    EventNotificationZipcode eventNotificationZipcode2 = new EventNotificationZipcode();
    eventNotificationZipcode2.setZipCode("92106");
    eventNotificationZipcodes.add(eventNotificationZipcode1);
    eventNotificationZipcodes.add(eventNotificationZipcode2);

    eventNotification = new EventNotification();
    eventNotification.setType("ADMIN_E");
    eventNotification.setDescription("some description");
    eventNotification.setEventNotificationZipcodes(eventNotificationZipcodes);
  }

  @AfterClass
  public static void cleanup() {
    IntegrationTestHelper.cleanDbState();
  }
  
  @Test
  public void publishNotification_Success() throws JSONException {

    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);

    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        post(Entity.json(eventNotification));

    Assert.assertEquals(200, response.getStatus());
  }

  @Test
  public void noArgument() throws JSONException {
    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);

    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        post(null);

    Assert.assertEquals(422, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("errors")));
    Assert.assertEquals("[\"The request body may not be null\"]", responseJo.optString("errors"));
  }

  @Test
  public void nullDescription() throws JSONException {
    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);
    eventNotification.setDescription(null);
    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        post(Entity.json(eventNotification));

    Assert.assertEquals(422, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("errors")));
    Assert.assertEquals("[\"description may not be null\"]", responseJo.optString("errors"));
  }

  @Test
  public void invalidDescription() throws JSONException {
    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);
    eventNotification.setDescription("abc");
    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        post(Entity.json(eventNotification));

    Assert.assertEquals(422, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("errors")));
    Assert.assertEquals("[\"description size must be between 5 and 2048\"]",
        responseJo.optString("errors"));
  }

  @Test
  public void invalidZipcode() throws JSONException {
    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);
    eventNotification.getEventNotificationZipcodes().iterator().next().setZipCode("987");
    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url, RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        post(Entity.json(eventNotification));

    Assert.assertEquals(422, response.getStatus());
    JSONObject responseJo = new JSONObject(response.readEntity(String.class));
    Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("errors")));
    Assert.assertEquals("[\"eventNotificationZipcodes[].zipCode is invalid.\"]",
        responseJo.optString("errors"));
  }

  @Test
  public void retrieveAllNoneExists() throws Exception {
    String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);
    IntegrationTestHelper.deleteAllEventNotfications();
    Client client = new JerseyClientBuilder().build();
    Response response = client.
        target(String.format(url+"/admin", RULE.getLocalPort())).
        request().
        header("Authorization", "Bearer " + authToken).
        get();

        Assert.assertEquals(200, response.getStatus());
        List<EventNotification> list = response.readEntity(new GenericType<List<EventNotification>>(){});
        String count = response.getHeaderString("x-total-count");
        int rows = Integer.decode(count);
        assertThat(rows).isEqualTo(0);
        assertThat(list.size()).isEqualTo(0);
  }
 
  @Test
  public void retrieveAll() throws Exception {
        EventNotification event = new EventNotification();
        event.setId(Long.valueOf(1234));
        User tmpUser = new User();
        tmpUser.setId(Long.valueOf(100));
        event.setType("FIRE");
        event.setUrl1("www.msn.com");
        event.setUrl2("www.cnn.com");
        event.setUserId(tmpUser);
        event.setDescription("CRUD TEST EVENT");
      event.setCitizensAffected(Integer.valueOf(1000));

      IntegrationTestHelper.addEventNotfication(event);
      String authToken = IntegrationTestHelper.getAuthToken("admin100@cgi.com", "adminpw", RULE);
      Client client = new JerseyClientBuilder().build();
      Response response = client.
              target(String.format(url+"/admin", RULE.getLocalPort())).
              request().
              header("Authorization", "Bearer " + authToken).
        get();

        Assert.assertEquals(200, response.getStatus());
        List<EventNotification> list = response.readEntity(new GenericType<List<EventNotification>>(){});
        String count = response.getHeaderString("x-total-count");
        int rows = Integer.decode(count);
        assertThat(rows).isEqualTo(1);
        assertThat(list.size()).isEqualTo(1);
        IntegrationTestHelper.deleteAllEventNotfications();
  }
}
