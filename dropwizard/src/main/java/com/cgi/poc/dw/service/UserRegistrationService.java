package com.cgi.poc.dw.service;

import com.cgi.poc.dw.rest.model.UserRegistrationDto;
import javax.ws.rs.core.Response;

public interface UserRegistrationService {

  Response registerUser(UserRegistrationDto userDto);
}
