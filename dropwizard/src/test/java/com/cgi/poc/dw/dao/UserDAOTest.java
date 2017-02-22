package com.cgi.poc.dw.dao;

import static org.junit.Assert.assertEquals;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDAOTest extends DaoUnitTestBase{

  UserDao userDao;

  public UserDAOTest() {
    super();
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    userDao = new UserDao(getSessionFactory());
  }

  @After
  public void tearDown() {
    super.tearDown();
  }
  private List<GeoCoordinates> geoCoordinates;

  @Test
  public void userIsWithinRadius(){
    GeoCoordinates geo = new GeoCoordinates();
    geo.setLatitude(10.00);
    geo.setLongitude(20.00);

    Double radius = 50.00;

    geoCoordinates = Arrays.asList(geo);

    User user = new User();

    user.setEmail("test@test.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("California");
    user.setRequiredStreet("required street");
    user.setOptionalStreet("optional street");
    user.setLatitude(10.5);
    user.setLongitude(20.0);

    UserNotificationType selNot = new UserNotificationType(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotificationType> notificationType = new HashSet<>();
    notificationType.add(selNot);
    user.setNotificationType(notificationType);

    User rt = userDao.save(user);

    List<User> users = userDao.getGeoWithinRadius(geoCoordinates, radius);

    assertEquals(1, users.size());
  }

  @Test
  public void userIsOutsideRadius(){
    GeoCoordinates geo = new GeoCoordinates();
    geo.setLatitude(15.00);
    geo.setLongitude(20.00);

    Double radius = 50.00;

    geoCoordinates = Arrays.asList(geo);

    List<User> users = userDao.getGeoWithinRadius(geoCoordinates, radius);

    assertEquals(0, users.size());
  }
}
