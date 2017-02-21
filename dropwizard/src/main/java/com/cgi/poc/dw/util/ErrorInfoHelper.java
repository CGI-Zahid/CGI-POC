package com.cgi.poc.dw.util;

public class ErrorInfoHelper {

  public static ErrorInfo getUnknownExceptionErrorMessage(Exception exception) {
    GeneralErrors generalErrors = GeneralErrors.UNKNOWN_EXCEPTION;
    ErrorInfo errRet = new ErrorInfo();
    String message = generalErrors.getMessage();
    String exMsg = "";
    if (exception.getMessage() != null){
      exMsg  = exception.getMessage();
    }
    String errorString = message.replace("REPLACE1", exception.getClass().getCanonicalName())
        .replace("REPLACE2", exMsg);
    errRet.addError(generalErrors.getCode(), errorString);
    return errRet;
  }
  
}
