/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

/**
 * @author dawna.floyd
 */
public enum GeneralErrors {

  UNKNOWN_EXCEPTION("ERR1",
      "An Unknown exception has occured. Type: <REPLACE1>. Message: <REPLACE2>"),
  CONSTRAINT_VIOLATION("ERR2", "Constraint violation <REPLACE>. Unable to Save"),
  INVALID_INPUT("ERR3", "The data provided in the API call is invalid. Message: <REPLACE>"),
  DUPLICATE_ENTRY("ERR4", "Duplicate entry for key '<REPLACE>'");

  private String errorCode;
  private String errorMessage;

  /**
   *
   */
  GeneralErrors(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public String getMessage() {
    return this.errorMessage;
  }

  public String getCode() {
    return this.errorCode;
  }

  public Error getError() {
    return new Error(this.errorCode, this.errorMessage);
  }
}
