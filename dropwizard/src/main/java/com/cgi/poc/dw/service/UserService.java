package com.cgi.poc.dw.service;

import com.cgi.poc.dw.dao.model.User;
import javax.ws.rs.core.Response;

public interface UserService {

  Response registerUser(User userDto);
  
  Response setLocalization(User user);

  Response updateUser(User userDto);
}
