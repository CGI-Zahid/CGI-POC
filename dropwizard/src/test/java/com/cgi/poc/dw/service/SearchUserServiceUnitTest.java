package com.cgi.poc.dw.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.helper.IntegrationTest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserServiceUnitTest extends IntegrationTest{
  @InjectMocks
  SearchUserServiceImpl underTest;

  @Mock
  SessionFactory sessionFactory;

  private List<GeoCoordinates> geoCoordinates;

  private Double radius = 50.00;

  @Before
  public void init() throws Exception {
    Session session = mock(Session.class);
    when(sessionFactory.openSession()).thenReturn(session);

    GeoCoordinates geo = new GeoCoordinates();
    geo.setLatitude(10.00);
    geo.setLongitude(20.00);

    geoCoordinates = Arrays.asList(geo);

    String sqlSearch = "SELECT "
        + "  *, ( "
        + "    3959 * acos ( "
        + "      cos ( radians("+geo.getLatitude().toString()+") ) "
        + "      * cos( radians( latitude ) ) "
        + "      * cos( radians( longitude ) - radians("+geo.getLongitude().toString()+") ) "
        + "      + sin ( radians("+geo.getLatitude().toString()+") ) "
        + "      * sin( radians( latitude ) ) "
        + "    ) "
        + "  ) AS distance "
        + "FROM user "
        + "HAVING distance < " + radius + " "
        + "ORDER BY distance;";
    SQLQuery query = mock(SQLQuery.class);
    when(session.createSQLQuery(eq(sqlSearch))).thenReturn(query);

    Object[] data = new Object[]{BigInteger.valueOf(1),"first_name","last_name","email","password","phone","zip","role",
        BigDecimal.valueOf(10.00),BigDecimal.valueOf(20.00)};
    List<Object[]> result = new ArrayList<>();
    result.add(data);
    when(query.list()).thenReturn(result);

    SQLQuery queryNotifications = mock(SQLQuery.class);
    String sqlNotification = "SELECT notification_id FROM user_notification WHERE user_id=1";
    when(session.createSQLQuery(eq(sqlNotification))).thenReturn(queryNotifications);

    List<Object[]> resultNotifications = new ArrayList();
    resultNotifications.add(new Object[]{new Long(1)});
    when(queryNotifications.list()).thenReturn(resultNotifications);

    doNothing().when(session).close();
  }

  @Test
  public void userIsWithinRadius(){
    List<User> users = underTest.search(geoCoordinates, radius);

    assertEquals(1, users.size());
  }

  @Test
  public void userIsOutsideRadius(){
    GeoCoordinates geo = new GeoCoordinates();
    geo.setLatitude(15.00);
    geo.setLongitude(20.00);

    geoCoordinates = Arrays.asList(geo);

    List<User> users = underTest.search(geoCoordinates, radius);

    assertEquals(0, users.size());
  }
}
