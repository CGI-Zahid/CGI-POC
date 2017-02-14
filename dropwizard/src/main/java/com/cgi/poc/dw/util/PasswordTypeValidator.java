/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.util;

import com.cgi.poc.dw.auth.MyPasswordValidator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.passay.PasswordData;
import org.passay.RuleResult;

/**
 * @author dawna.floyd
 */
public class PasswordTypeValidator implements ConstraintValidator<PasswordType, String> {

  private static final Log log = LoggerFactory.make();
  private final static MyPasswordValidator myPasswordValidator = new MyPasswordValidator();

  @Override
  public void initialize(PasswordType parameters) {
    validateParameters(parameters);
  }

  /**
   * Checks the number of entries in a map.
   *
   * @param value The map to validate.
   * @param constraintValidatorContext context in which the constraint is evaluated.
   * @return Returns <code>true</code> if the map is <code>null</code> or the number of entries in
   * <code>map</code> is between the specified <code>min</code> and <code>max</code> values
   * (inclusive), <code>false</code> otherwise.
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    // check value against the defined currency values valid for sassy.
    if (value == null) {
      return false;
    }
    RuleResult result = myPasswordValidator.validate(new PasswordData(value));

    return result.isValid();

  }

  private void validateParameters(PasswordType parameters) {
    // no parameters defined at the moment.
  }


}
