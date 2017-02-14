package com.cgi.poc.dw.service;

import com.cgi.poc.dw.dao.model.User;
import javax.ws.rs.core.Response;

public interface LoginService {

  Response login(User userDto);
}
