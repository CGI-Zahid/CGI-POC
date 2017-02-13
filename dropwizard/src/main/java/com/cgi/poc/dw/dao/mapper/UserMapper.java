package com.cgi.poc.dw.dao.mapper;

import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UserMapper implements ResultSetMapper<User> {

  public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    User user = new User();
    user.setEmail(r.getString("email"));
    user.setPassword(r.getString("password"));
    user.setId(r.getLong("id"));
    user.setRole(Role.valueOf(r.getString("role")));
    return user;
  }
}
