/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author dawna.floyd
 */
@XmlRootElement

public class Error {


  String code;
  String message;

  public Error() {
  }

  public Error(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public Error(Error error) {
    this.code = error.getCode();
    this.message = error.getMessage();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    if (!(other instanceof Error)) {
      return false;
    }
    Error error = (Error) other;

    return (this.getCode().equals(error.getCode()) && this.getMessage().equals(error.getMessage()));
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(11, 101).append(code).append(message).toHashCode();
  }
}
