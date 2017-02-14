package com.cgi.poc.dw.auth;

import com.cgi.poc.dw.auth.service.JwtReaderService;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.User;
import com.google.inject.Inject;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for authenticating users using backing database.
 */
public class DBAuthenticator implements Authenticator<JwtContext, User> {

  private final static Logger LOG = LoggerFactory.getLogger(DBAuthenticator.class);

  @Inject
  UserDao userDao;
  @Inject
  JwtReaderService jwtReaderService;
 
  
  @Override
  public Optional<User> authenticate(JwtContext context) throws AuthenticationException {

    String email = "";
    try {
      email = jwtReaderService.getEmailFromJwt(context.getJwt());
    } catch (InvalidJwtException e) {
      throw new AuthenticationException(e);
    }
    if (StringUtils.isBlank(email)) {
      LOG.error("Email is blank.");
      return Optional.empty();
    } else {
      User user = userDao.findUserByEmail(email);
      return Optional.ofNullable(user);
    }
  }
}
