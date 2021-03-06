package com.cgi.poc.dw.auth;

import com.cgi.poc.dw.auth.data.Role;
import com.cgi.poc.dw.dao.model.User;
import io.dropwizard.auth.Authorizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoleAuthorizer implements Authorizer<User> {

  private final static Logger LOG = LoggerFactory.getLogger(UserRoleAuthorizer.class);

  @Override
  public boolean authorize(User user, String role) {
    if (user == null) {
      LOG.error("Unable to authorize since user is null.");
      return false;
    } else if (user.getRole() == null) {
      LOG.error("Unable to authorize since user role is null.");
      return false;
    } else if (StringUtils.isBlank(role)) {
      LOG.error("Unable to authorize since role is blank.");
      return false;
    }
    Role roleToEnum = null;
    try {
      roleToEnum = Role.valueOf(role);
    } catch (IllegalArgumentException iae) {
      LOG.error("Unable to authorize since role " + role + " is not a valid role.");
      return false;
    }
    return user.getRole().equals(role);
  }
}
