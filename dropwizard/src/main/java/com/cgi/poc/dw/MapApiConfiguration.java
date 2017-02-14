package com.cgi.poc.dw;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by valonsejdini on 2017-02-13.
 */
public class MapApiConfiguration {

  private String apiURL;
  /**
   * The key to access exchange rate API.
   */
  private String apiKey;

  @JsonProperty
  public String getApiURL() {
    return apiURL;
  }

  public void setApiURL(String apiURL) {
    this.apiURL = apiURL;
  }

  @JsonProperty
  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
}
