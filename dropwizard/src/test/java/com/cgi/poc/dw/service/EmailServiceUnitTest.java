package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.MailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceUnitTest {
  
  @InjectMocks
  EmailServiceImpl underTest;

  @Mock
  private MailConfiguration mailConfiguration;

  private GreenMail smtpServer;

  @Before
  public void init() {
    smtpServer = new GreenMail(new ServerSetup(3025, "127.0.0.1",
        ServerSetup.PROTOCOL_SMTP));
    smtpServer.start();
  }

  @After
  public void exit() {
    if (smtpServer != null) {
      smtpServer.stop();
    }
  }

  @Test
  public void send_SendsEmailWithoutAttachment() throws MessagingException {
    String fromEmail = "test@cgi.com";
    List<String> recepients = Arrays.asList("client1@cgi.com", "client2@cgi.com");
    String subject = "This is a test";
    String emailBody = "Hello there, this is testing email notifications.";

    when(mailConfiguration.getHostname()).thenReturn("127.0.0.1");
    when(mailConfiguration.getPort()).thenReturn(3025);
    when(mailConfiguration.getUsername()).thenReturn("myusername");
    when(mailConfiguration.getPassword()).thenReturn("mypassword");
    when(mailConfiguration.isSsl()).thenReturn(false);

    underTest.send(fromEmail, recepients, subject, emailBody);

    smtpServer.waitForIncomingEmail(2);
    MimeMessage[] receivedMails = smtpServer.getReceivedMessages();
    assertEquals( "Should have received 2 emails.", 2, receivedMails.length);
    
    for(MimeMessage mail : receivedMails) {
      assertTrue(GreenMailUtil.getHeaders(mail).contains(subject));
      assertTrue(GreenMailUtil.getBody(mail).contains(emailBody));
    }
    assertEquals(recepients.get(0), receivedMails[0].getRecipients(RecipientType.TO)[0].toString());
    assertEquals(recepients.get(1), receivedMails[1].getRecipients(RecipientType.TO)[1].toString());
  }

  @Test
  public void send_SendsEmailWithAttachmentToASingleRecepient() throws MessagingException, IOException {
    String fromEmail = "test@cgi.com";
    String recepient = "client1@cgi.com";
    String subject = "This is a test";
    String emailBody = "Hello there, this is testing email notifications.";
    InputStream inputStream = getClass().getResourceAsStream("/emailAttachment.txt");
    String fileName = "emailAttachment.txt";
    String mimeType = "application/txt";


    when(mailConfiguration.getHostname()).thenReturn("127.0.0.1");
    when(mailConfiguration.getPort()).thenReturn(3025);
    when(mailConfiguration.getUsername()).thenReturn("myusername");
    when(mailConfiguration.getPassword()).thenReturn("mypassword");
    when(mailConfiguration.isSsl()).thenReturn(false);

    underTest.send(fromEmail, recepient, subject, emailBody, inputStream, fileName, mimeType);

    smtpServer.waitForIncomingEmail(1);
    MimeMessage[] receivedMails = smtpServer.getReceivedMessages();
    assertEquals( "Should have received 1 email.", 1, receivedMails.length);

    MimeMessage mail = receivedMails[0];
    assertEquals(recepient, receivedMails[0].getRecipients(RecipientType.TO)[0].toString());
    assertTrue(GreenMailUtil.getHeaders(mail).contains(subject));
    assertTrue(GreenMailUtil.getBody(mail).contains(emailBody));
    assertTrue(GreenMailUtil.getBody(mail).contains(fileName));
    assertTrue(GreenMailUtil.getBody(mail).contains("This is a sample text for file attachment."));
  }

  @Test
  public void send_SendsEmailWithAttachmentToMultipleRecepients() throws MessagingException, IOException {
    String fromEmail = "test@cgi.com";
    List<String> recepients = Arrays.asList("client1@cgi.com", "client2@cgi.com");
    String subject = "This is a test";
    String emailBody = "Hello there, this is testing email notifications.";

    InputStream inputStream = getClass().getResourceAsStream("/emailAttachment.txt");
    String fileName = "emailAttachment.txt";
    String mimeType = "application/txt";

    List<InputStream> inputStreams = Arrays.asList(inputStream, inputStream);
    List<String> fileNames = Arrays.asList(fileName, fileName);
    List<String> mimeTypes = Arrays.asList(mimeType, mimeType);

    when(mailConfiguration.getHostname()).thenReturn("127.0.0.1");
    when(mailConfiguration.getPort()).thenReturn(3025);
    when(mailConfiguration.getUsername()).thenReturn("myusername");
    when(mailConfiguration.getPassword()).thenReturn("mypassword");
    when(mailConfiguration.isSsl()).thenReturn(false);

    underTest.send(fromEmail, recepients, subject, emailBody, inputStreams, fileNames, mimeTypes);

    smtpServer.waitForIncomingEmail(2);
    MimeMessage[] receivedMails = smtpServer.getReceivedMessages();
    assertEquals( "Should have received 2 emails.", 2, receivedMails.length);

    for(MimeMessage mail : receivedMails) {
      assertTrue(GreenMailUtil.getHeaders(mail).contains(subject));
      assertTrue(GreenMailUtil.getBody(mail).contains(emailBody));
      assertTrue(GreenMailUtil.getBody(mail).contains(fileName));
      assertTrue(GreenMailUtil.getBody(mail).contains("This is a sample text for file attachment."));
    }
    assertEquals(recepients.get(0), receivedMails[0].getRecipients(RecipientType.TO)[0].toString());
    assertEquals(recepients.get(1), receivedMails[1].getRecipients(RecipientType.TO)[1].toString());
  }

}
