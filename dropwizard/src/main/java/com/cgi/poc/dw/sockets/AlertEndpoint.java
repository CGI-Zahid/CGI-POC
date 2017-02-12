package com.cgi.poc.dw.sockets;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/alert")
public class AlertEndpoint {
  @OnOpen
  public void myOnOpen(final Session session) throws IOException {
  }

  @OnMessage
  public void onMessage(String text) {
  }

  @OnClose
  public void myOnClose(final Session session, CloseReason cr) {
  }
}