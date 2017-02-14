package com.cgi.poc.dw.test.sockets;

import com.cgi.poc.dw.helper.IntegrationTest;
import com.cgi.poc.dw.test.helper.IntTestSocketClientHelper;
import java.io.IOException;
import java.net.URI;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertEndpointIntTest extends IntegrationTest {

  private final static Logger LOG = LoggerFactory.getLogger(AlertEndpointIntTest.class);
  private static final String url = "ws://localhost:%d/alert";

  @Test
  public void canOpenConnectionWebsocket() throws IOException, DeploymentException, InterruptedException {
    try {
      String testIsOpen;
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      if(container.connectToServer(IntTestSocketClientHelper.class, new URI(String.format(url, RULE.getLocalPort()))).isOpen()){
        testIsOpen = "OPEN";
      }else{
        testIsOpen = "CLOSED";
      }
      Assert.assertEquals("OPEN", testIsOpen);
    } catch (Exception ex) {
      LOG.error("fail", ex);
      Assert.fail();
    }
  }
}
