package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.FirebaseConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FirebasePushServiceUnitTest {

  @InjectMocks
  FirebasePushServiceImpl underTest;

  @Mock
  private FirebaseConfiguration firebaseConfiguration;

  @Mock
  private Client client;

  private WebTarget mockWebTarget;
  private Invocation.Builder mockBuilder;

  @Before
  public void setupClient() throws IOException {
    mockWebTarget = mock(WebTarget.class);
    mockBuilder = mock(Invocation.Builder.class);

    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
    when(mockBuilder.header(anyObject(), anyString())).thenReturn(mockBuilder);
  }

  @Test
  public void sendingPushNotification() throws  IOException{
    ResponseBuilder builder;
    builder = Response.status(Response.Status.OK).entity(
        "{\"multicast_id\":7733210718836532357,\"success\":1,\"failure\":0,\"canonical_ids\":0,\"results\":[{\"message_id\":\"0:1488314451590142%cc9b4facf9fd7ecd\"}]}");

    when(mockBuilder.post(Entity.json(any(JsonNode.class)))).thenReturn(builder.build());

    when(firebaseConfiguration.getFcmURL()).thenReturn("https://fcm.googleapis.com/fcm/send");
    when(firebaseConfiguration.getFcmKey()).thenReturn(
        "AAAA8XU5NgQ:APA91bEfETw0rTo7J1exWlzARff99f_SlyI1SVZywP_LGsLBOMAuVGbclayJGIfVRRb624N87-CP0MY6TmHR2jgBLU5irF7T4N40cFanF6or6pxLEGYMEisWilC6GjOL2Wx-0BXt8LRv");

    Boolean rt = underTest.send("Test", "Title", "Message");

    assertEquals("Send test push notification.", true, rt);
  }

  @Test
  public void failedSendingPushNotification() throws  IOException{
    ResponseBuilder builder;
    builder = Response.status(Response.Status.OK).entity(
        "{\"multicast_id\":7733210718836532357,\"success\":0,\"failure\":1,\"canonical_ids\":0,\"results\":[]}");

    when(mockBuilder.post(Entity.json(any(JsonNode.class)))).thenReturn(builder.build());

    when(firebaseConfiguration.getFcmURL()).thenReturn("https://fcm.googleapis.com/fcm/send");
    when(firebaseConfiguration.getFcmKey()).thenReturn(
        "AAAA8XU5NgQ:APA91bEfETw0rTo7J1exWlzARff99f_SlyI1SVZywP_LGsLBOMAuVGbclayJGIfVRRb624N87-CP0MY6TmHR2jgBLU5irF7T4N40cFanF6or6pxLEGYMEisWilC6GjOL2Wx-0BXt8LRv");

    Boolean rt = underTest.send("Test", "Title", "Message");

    assertEquals("Failed to send test push notification.", false, rt);
  }

}
