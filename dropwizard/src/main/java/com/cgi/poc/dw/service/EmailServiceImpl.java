package com.cgi.poc.dw.service;

import com.cgi.poc.dw.MailConfiguration;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailServiceImpl implements EmailService {

  private final static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

  private final MailConfiguration mailConfiguration;

  @Inject
  public EmailServiceImpl(MailConfiguration mailConfiguration) {
    this.mailConfiguration = mailConfiguration;
  }

  @Override
  public void send(String from, Collection<String> recipients, String subject, String text) {
    send(from, recipients, subject, text, null, null, null);
  }

  @Override
  public void send(String from, String recipient, String subject, String text,
      InputStream attachment, String fileName, String mimeType) {
    send(from, Arrays.asList(recipient), subject, text, Arrays.asList(attachment),
        Arrays.asList(fileName),
        Arrays.asList(mimeType));
  }

  @Override
  public void send(String from, Collection<String> recipients, String subject, String text,
      List<InputStream> attachments, List<String> fileNames, List<String> mimeTypes) {

    // check for null references
    if (StringUtils.isEmpty(from)) {
      from = mailConfiguration.getSystemEmail();
    }

    // check for null references
    Objects.requireNonNull(from);
    Objects.requireNonNull(recipients);

    // create an email message with html support
    HtmlEmail email = new HtmlEmail();

    // configure SMTP connection
    email.setHostName(mailConfiguration.getHostname());
    email.setSmtpPort(mailConfiguration.getPort());
    email.setAuthentication(mailConfiguration.getUsername(), mailConfiguration.getPassword());
    email.setSSLOnConnect(mailConfiguration.isSsl());

    // set its properties accordingly
    try {
      String[] toEmails = recipients.toArray(new String[]{});

      email.setFrom(from);
      email.addTo(toEmails);
      email.setSubject(subject);
      email.setHtmlMsg(text);

      if (attachments != null) {
        for (int i = 0; i < attachments.size(); i++) {
          // create a data source to wrap the attachment and its mime type
          ByteArrayDataSource dataSource = null;
          try {
            dataSource = new ByteArrayDataSource(attachments.get(i), mimeTypes.get(i));
          } catch (IOException exception) {
            LOG.error("Email attachment error for {}.", toEmails[i], exception);
            return;
          }
          // add the attachment
          email.attach(dataSource, fileNames.get(i), "attachment");
        }
      }

      // send it!
      email.send();
    } catch (EmailException exception) {
      LOG.error("Unable to send notification email to {} .", recipients.toString(), exception);
    }

  }

}
