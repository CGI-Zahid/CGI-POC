package com.cgi.poc.dw.service;

import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchUserServiceImpl implements SearchUserService{

  private static final Logger LOG = LoggerFactory.getLogger(SearchUserServiceImpl.class);

  private SessionFactory sessionFactory;

  @Inject
  public SearchUserServiceImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public List<User> search(List<GeoCoordinates> coords, Double radius){
    Session session = sessionFactory.openSession();

    List<User> users = new ArrayList<User>();

    for (GeoCoordinates g : coords) {
      String sqlSearch = "SELECT "
          + "  *, ( "
          + "    3959 * acos ( "
          + "      cos ( radians("+g.getLatitude().toString()+") ) "
          + "      * cos( radians( latitude ) ) "
          + "      * cos( radians( longitude ) - radians("+g.getLongitude().toString()+") ) "
          + "      + sin ( radians("+g.getLatitude().toString()+") ) "
          + "      * sin( radians( latitude ) ) "
          + "    ) "
          + "  ) AS distance "
          + "FROM user "
          + "HAVING distance < " + radius + " "
          + "ORDER BY distance;";
      Query query = session.createSQLQuery(
          sqlSearch);

      List result = query.list();

      Iterator iterator = result.iterator();

      while (iterator.hasNext()) {
        Object[] row = (Object[])iterator.next();
        User thisUser = new User(((BigInteger)row[0]).longValue(),(String)row[1],(String)row[2],(String)row[3],
            (String)row[4],(String)row[5],(String)row[6],(String)row[7],
            ((BigDecimal)row[8]).doubleValue(),((BigDecimal)row[9]).doubleValue());

        if (!users.contains(thisUser)){
          users.add(thisUser);
        }
      }
    }

    session.close();

    return users;
  }
}
