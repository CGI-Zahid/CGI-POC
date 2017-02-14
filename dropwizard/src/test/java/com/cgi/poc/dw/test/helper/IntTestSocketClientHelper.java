package com.cgi.poc.dw.test.helper;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class IntTestSocketClientHelper {

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
  }

  @OnMessage
  public void onMessage(String text) {
  }

  @OnError
  public void onError(Session session, Throwable t){
  }
}
