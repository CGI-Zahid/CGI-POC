package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.dao.model.User;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class UserDao extends AbstractDAO<User> {

  @Inject
  public UserDao(SessionFactory factory) {
    super(factory);

  }

  public List<User> getAllNormalUsers() {
    Criteria criteria = this.criteria();
    //role = 'RESIDENT'
    criteria.add(Restrictions.eq("role", "RESIDENT"));
    List<User> resultList = criteria.list();
    return resultList;
  }

  public User findUserByEmail(String email) {

    Criteria criteria = this.criteria();

    //contract id, page, page size
    criteria.add(Restrictions.eq("email", email));
    User retUser = null;
    try {
      retUser = (User) criteria.uniqueResult();
    } catch (Exception e) {
      System.out.println("Error: Exception");
      System.out.println(e);

    }
    return retUser;
  }

  public User save(User usr) {
    usr = this.persist(usr);

    return usr;

  }

  public User remove(User usr) {
    usr = this.remove(usr);
    return usr;
  }

  public void flush() {
    this.currentSession().flush();
  }
}
