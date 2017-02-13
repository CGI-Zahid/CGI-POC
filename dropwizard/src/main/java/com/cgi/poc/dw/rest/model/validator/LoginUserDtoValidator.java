package com.cgi.poc.dw.rest.model.validator;

import com.cgi.poc.dw.rest.model.LoginUserDto;
import javax.ws.rs.BadRequestException;
import org.apache.commons.lang3.StringUtils;

public class LoginUserDtoValidator {

  public static void validate(LoginUserDto loginUserDto) {
    if (loginUserDto == null) {
      throw new BadRequestException("Missing credentials.");
    }
    if (StringUtils.isBlank(loginUserDto.getEmail())) {
      throw new BadRequestException("Missing email.");
    } else if (StringUtils.isBlank(loginUserDto.getPassword())) {
      throw new BadRequestException("Missing password.");
    }
  }
}
