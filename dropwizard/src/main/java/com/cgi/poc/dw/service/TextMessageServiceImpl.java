package com.cgi.poc.dw.service;

import com.cgi.poc.dw.TwilioApiConfiguration;
import com.google.inject.Inject;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextMessageServiceImpl implements TextMessageService{
  private final static Logger LOG = LoggerFactory.getLogger(TextMessageServiceImpl.class);

  private TwilioApiConfiguration twilioApiConfiguration;

  @Inject
  public TextMessageServiceImpl(TwilioApiConfiguration twilioApiConfiguration) {
    this.twilioApiConfiguration = twilioApiConfiguration;
  }

  public Boolean send(String sendingToNumber, String messageToSend){
    try {
      Twilio.init(twilioApiConfiguration.getAccountSID(), twilioApiConfiguration.getAuthToken());

      Message message = Message.creator(new PhoneNumber(("1" + sendingToNumber)),
          new PhoneNumber(twilioApiConfiguration.getPhoneNumber()),
          messageToSend).create();

      LOG.info("TextMessage success: {}", message.getStatus());

      return true;
    } catch (Exception exception){
      LOG.error("TextMessage errror sending to : {}", sendingToNumber, exception);
      return false;
    }
  }
}
