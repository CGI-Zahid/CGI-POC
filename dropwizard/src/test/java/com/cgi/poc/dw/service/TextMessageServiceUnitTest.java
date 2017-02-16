package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.TwilioApiConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextMessageServiceUnitTest {

  @InjectMocks
  TextMessageServiceImpl underTest;

  @Mock
  private TwilioApiConfiguration twilioApiConfiguration;

  @Test
  public void sendToNullPhoneNumberWithMessage() {
    when(twilioApiConfiguration.getAccountSID()).thenReturn("ACf7fd4c1c6ce58597ea87ef2b6c179cdb");
    when(twilioApiConfiguration.getAuthToken()).thenReturn("ff0440de6f1a7586bfaa9d87f558e1a4");
    when(twilioApiConfiguration.getPhoneNumber()).thenReturn("+15005550006");

    Boolean rt = underTest.send(null, "Test");

    assertEquals("Trying null phone number text message should return false.",false,rt);
  }

  @Test
  public void sendToPhoneNumberWithNullMessage() {
    when(twilioApiConfiguration.getAccountSID()).thenReturn("ACf7fd4c1c6ce58597ea87ef2b6c179cdb");
    when(twilioApiConfiguration.getAuthToken()).thenReturn("ff0440de6f1a7586bfaa9d87f558e1a4");
    when(twilioApiConfiguration.getPhoneNumber()).thenReturn("+15005550006");

    Boolean rt = underTest.send("19162490778", null);

    assertEquals("Trying null text message should return false.",false,rt);
  }

  @Test
  public void sendToPhoneWithMessage() {
    when(twilioApiConfiguration.getAccountSID()).thenReturn("ACf7fd4c1c6ce58597ea87ef2b6c179cdb");
    when(twilioApiConfiguration.getAuthToken()).thenReturn("ff0440de6f1a7586bfaa9d87f558e1a4");
    when(twilioApiConfiguration.getPhoneNumber()).thenReturn("+15005550006");

    Boolean rt = underTest.send("9162490778", "Test");

    assertEquals("Trying send text message should return true.",true,rt);
  }
}
