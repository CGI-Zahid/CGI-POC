package com.cgi.poc.dw.rest.model.validator;

import com.cgi.poc.dw.auth.MyPasswordValidator;
import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationDtoValidator {

  private final static Logger LOG = LoggerFactory.getLogger(UserRegistrationDtoValidator.class);

  public static void validate(UserRegistrationDto userRegistrationDto,
      MyPasswordValidator myPasswordValidator) {
    if (userRegistrationDto == null) {
      throw new BadRequestException("Missing credentials.");
    } else if (myPasswordValidator == null) {
      LOG.error("MyPasswordValidator is null.");
      throw new InternalServerErrorException("Missing password validator.");
    }
    validateEmail(userRegistrationDto.getEmail());
    validatePassword(userRegistrationDto.getPassword(), myPasswordValidator);
  }

  private static void validatePassword(String password, MyPasswordValidator myPasswordValidator) {
    if (StringUtils.isBlank(password)) {
      throw new BadRequestException("Missing password.");
    }
    RuleResult result = myPasswordValidator.validate(new PasswordData(password));
    if (!result.isValid()) {
      throw new BadRequestException(myPasswordValidator.getMessages(result).get(0));
    }
  }

  private static void validateEmail(String email) {
    if (StringUtils.isBlank(email)) {
      throw new BadRequestException("Missing email.");
    } else if (!EmailValidator.getInstance().isValid(email)) {
      throw new BadRequestException("Invalid email address.");
    }
  }
}
