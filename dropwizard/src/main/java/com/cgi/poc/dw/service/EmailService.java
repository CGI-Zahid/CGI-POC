package com.cgi.poc.dw.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public interface EmailService {

  /**
   * Sends an email message with no attachments.
   *
   * @param from email address from which the message will be sent.
   * @param recipients the recipients of the message.
   * @param subject subject header field.
   * @param text content of the message.
   */
  void send(String from, Collection<String> recipients, String subject, String text);

  /**
   * Sends an email message to one recipient with one attachment.
   *
   * @param from email address from which the message will be sent.
   * @param recipient the recipients of the message.
   * @param subject subject header field.
   * @param text content of the message.
   * @param attachment attachment to be included with the message.
   * @param fileName file name of the attachment.
   * @param mimeType mime type of the attachment.
   */
  void send(String from, String recipient, String subject, String text, InputStream attachment,
      String fileName, String mimeType);

  /**
   * Sends an email message with attachments.
   *
   * @param from email address from which the message will be sent.
   * @param recipients array of strings containing the recipients of the message.
   * @param subject subject header field.
   * @param text content of the message.
   * @param attachments attachments to be included with the message.
   * @param fileNames file names for each attachment.
   * @param mimeTypes mime types for each attachment.
   */
  void send(String from, Collection<String> recipients, String subject, String text,
      List<InputStream> attachments, List<String> fileNames, List<String> mimeTypes);
}
