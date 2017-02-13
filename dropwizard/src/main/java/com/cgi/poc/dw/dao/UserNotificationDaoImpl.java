package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.mapper.UserNotificationMapper;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserNotificationMapper.class)
public interface UserNotificationDaoImpl extends UserNotificationDao {

  @Override
  @SqlQuery("SELECT id FROM notification_method WHERE method = :methodName")
  long findNotificationMethodIdByName(@Bind("methodName") String name);
  
  @Override
  @SqlBatch("insert into user_notification (user_id, notification_id) values (:user_id, :notification)")
  void createUserNotification(@Bind("user_id") long id, @Bind("notification") List<Long> notificationTypes);
}
