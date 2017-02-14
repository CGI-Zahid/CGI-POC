/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author dawna floyd
 */
@XmlRootElement
public class ErrorInfo implements Serializable {

  private List<Error> errors;

  public ErrorInfo(List<Error> errors) {
    this.errors = errors;
  }

  public ErrorInfo() {
    this.errors = new ArrayList<Error>();
  }

  /**
   * @return the errors
   */
  public List<Error> getErrors() {
    return errors;
  }

  /**
   * @param errors the errors to set
   */
  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  public void addError(String code, String message) {

    getErrors().add(new Error(code, message));
  }

  public void addError(Error error) {
    getErrors().add(new Error(error));
  }

  @Override
  public String toString() {
    String retString = "";
    for (Error error : errors) {
      retString = retString
          .concat(("Code: " + error.getCode() + ", Message: " + error.getMessage() + "\n"));
    }

    return retString;

  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    if (!(other instanceof ErrorInfo)) {
      return false;
    }
    ErrorInfo error = (ErrorInfo) other;
    Set<Error> errors1 = new HashSet<>(this.getErrors());
    Set<Error> errors2 = new HashSet<>(error.getErrors());
    return errors1.equals(errors2);
  }
}
