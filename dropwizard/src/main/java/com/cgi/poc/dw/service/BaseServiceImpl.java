/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * @author dawna.floyd
 */
public class BaseServiceImpl {

  JsonFactory factory = null;
  ObjectMapper mapper = null;
  private Validator validator;

  public BaseServiceImpl(Validator validator) {
    factory = new JsonFactory();
    mapper = new ObjectMapper(factory);
    this.validator = validator;
  }

  public void validate(Object object, String operationName, Class<?>... groups) {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
    if (constraintViolations.size() > 0) {
      Set<ConstraintViolation<?>> propagatedViolations
          = new HashSet<ConstraintViolation<?>>(constraintViolations.size());
      Set<String> classNames = new HashSet<String>();
      for (ConstraintViolation<?> violation : constraintViolations) {
        propagatedViolations.add(violation);
        classNames.add(violation.getLeafBean().getClass().getName());
      }
      StringBuilder builder = new StringBuilder();
      builder.append("Validation failed for classes ");
      builder.append(classNames);
      builder.append(" during ");
      builder.append(operationName);
      builder.append(" time for groups ");
      builder.append(groups.toString());
      builder.append("\nList of constraint violations:[\n");
      for (ConstraintViolation<?> violation : constraintViolations) {
        builder.append("\t").append(violation.toString()).append("\n");
      }
      builder.append("]");

      throw new ConstraintViolationException(
          builder.toString(), propagatedViolations
      );
    }
  }

}
