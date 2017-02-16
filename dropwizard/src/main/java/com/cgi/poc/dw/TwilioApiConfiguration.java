package com.cgi.poc.dw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwilioApiConfiguration {

  private String accountSID;
  private String authToken;
  private String phoneNumber;

  @JsonProperty
  public String getAccountSID() {
    return accountSID;
  }

  public void setAccountSID(String accountSID) {
    this.accountSID = accountSID;
  }

  @JsonProperty
  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  @JsonProperty
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

}
