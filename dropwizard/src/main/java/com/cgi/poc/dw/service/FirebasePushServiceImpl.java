package com.cgi.poc.dw.service;

import com.cgi.poc.dw.FirebaseConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirebasePushServiceImpl implements FirebasePushService{

  private final static Logger LOG = LoggerFactory.getLogger(FirebasePushServiceImpl.class);

  private Client client;
  private FirebaseConfiguration firebaseConfiguration;

  @Inject
  public FirebasePushServiceImpl( Client client,
      FirebaseConfiguration firebaseConfiguration) {
    this.client = client;
    this.firebaseConfiguration = firebaseConfiguration;
  }

  public boolean send(String token, String title, String message){
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.createObjectNode();

    JsonNode notification = mapper.createObjectNode();
    ((ObjectNode) notification).put("title", title);
    ((ObjectNode) notification).put("body", message);

    ((ObjectNode) root).set("notification", notification);
    ((ObjectNode) root).put("to", token);

    Response response = client
        .target(firebaseConfiguration.getFcmURL())
        .request(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "key=" + firebaseConfiguration.getFcmKey())
        .post(Entity.json(root));

    try {
      final ObjectNode node = new ObjectMapper().readValue(response.getEntity().toString(), ObjectNode.class);

      if (node.get("success").asInt() == 1){
        LOG.info("Push notification sent to {}", token);
        return true;
      }else{
        LOG.info("Push notification not sent to {}", token);
      }
    } catch (Exception e) {
      LOG.error("Unable to parse the result for the url event : {}", firebaseConfiguration.getFcmURL(), e);
    }

    return false;
  }
}
