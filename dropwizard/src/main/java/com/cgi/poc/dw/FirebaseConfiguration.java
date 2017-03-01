package com.cgi.poc.dw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirebaseConfiguration {
  private String fcmURL;
  private String fcmKey;

  @JsonProperty
  public String getFcmURL() {
    return fcmURL;
  }

  public void setFcmURL(String fcmURL) {
    this.fcmURL = fcmURL;
  }

  @JsonProperty
  public String getFcmKey() {
    return fcmKey;
  }

  public void setFcmKey(String fcmKey) {
    this.fcmKey = fcmKey;
  }
}
