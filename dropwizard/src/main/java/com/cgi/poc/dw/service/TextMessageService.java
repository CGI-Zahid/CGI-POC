package com.cgi.poc.dw.service;

public interface TextMessageService {
  /**
   * Sends a text message to the provided number
   *
   * @param sendingToNumber phone number destination.
   * @param messageToSend text message.
   */
  Boolean send(String sendingToNumber, String messageToSend);

}
