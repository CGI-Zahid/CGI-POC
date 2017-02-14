package com.cgi.poc.dw.service;

import com.cgi.poc.dw.rest.model.LoginUserDto;
import javax.ws.rs.core.Response;

public interface LoginService {

  Response login(LoginUserDto userDto);
}
