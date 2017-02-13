package com.cgi.poc.dw.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UserNotificationMapper implements ResultSetMapper<Long> {

  public Long map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    Long notificationMethodId = r.getLong("id");
    return notificationMethodId;
  }
}
