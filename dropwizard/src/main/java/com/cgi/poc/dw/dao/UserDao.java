package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.User;
import java.util.Iterator;
import java.util.List;

public interface UserDao {

  long createUser(User user) throws Exception;

  User findUserByEmail(String email);
  
  List<User> getAllNormalUsers();

  void createUserNotification(List<Integer> id, Iterator<String> notificationTypes);
}
