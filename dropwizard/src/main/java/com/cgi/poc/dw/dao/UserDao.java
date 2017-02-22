package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.dao.model.User;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
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

  public User getAdminUser() {
    Criteria criteria = this.criteria();
    criteria.add(Restrictions.eq("role", "ADMIN"));
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

  public List<User> getGeoWithinRadius(List<GeoCoordinates> geo, Double radius) {

    List<User> users = new ArrayList<User>();

    for (GeoCoordinates g : geo) {
       Query query = currentSession().getNamedQuery("getGeoWithinRadius")
          .setString("lat", g.getLatitude().toString())
          .setString("lng", g.getLongitude().toString())
          .setString("radius", radius.toString());
      List<User> user = query.list();

      if (!users.contains(user)) {
        users.addAll(user);
      }
    }
    return users;
  }
}
