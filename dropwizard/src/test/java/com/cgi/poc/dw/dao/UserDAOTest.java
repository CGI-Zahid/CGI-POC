package com.cgi.poc.dw.dao;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.dao.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;

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

    User user = createUser();

    userDao.save(user);

    List<User> users = userDao.getGeoWithinRadius(geoCoordinates, radius);

    assertEquals(1, users.size());
    assertEquals("test@test.com", users.get(0).getEmail());
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

  @Test
  public void userIsWithinRadiusJSON() throws IOException {
    User user = createUser();
    userDao.save(user);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode geo = mapper.readTree("{\"rings\":[[[20.00,10.00],[-97.1600111522097,30.470004589592428]]]}");

    Double radius = 50.00;

    List<User> users = userDao.getGeoWithinRadius(geo, radius);

    assertEquals(1, users.size());
    assertEquals("test@test.com", users.get(0).getEmail());
  }

  @Test
  public void userIsOutsideRadiusJSON() throws IOException {
    User user = createUser();
    userDao.save(user);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode geo = mapper.readTree("{\"rings\":[[[20.00,15.00],[-97.1600111522097,30.470004589592428]]]}");

    Double radius = 50.00;

    List<User> users = userDao.getGeoWithinRadius(geo, radius);

    assertEquals(0, users.size());
  }

  @Test
  public void newNotificationSelectionsArePersisted() {
    User user = createUser();
    userDao.save(user);

    User retrievedUser = userDao.findUserByEmail(user.getEmail());
    assertThat(retrievedUser.getSmsNotification()).isEqualTo(true);

    retrievedUser.setPushNotification(true);
    userDao.save(retrievedUser);

    retrievedUser = userDao.findUserByEmail(user.getEmail());
    assertThat(retrievedUser.getSmsNotification()).isEqualTo(true);
    assertThat(retrievedUser.getPushNotification()).isEqualTo(true);


    retrievedUser.setEmailNotification(true);
    userDao.save(retrievedUser);

    retrievedUser = userDao.findUserByEmail(user.getEmail());
    assertThat(retrievedUser.getSmsNotification()).isEqualTo(true);
    assertThat(retrievedUser.getPushNotification()).isEqualTo(true);
    assertThat(retrievedUser.getEmailNotification()).isEqualTo(true);

    
    retrievedUser.setSmsNotification(false);
    userDao.save(retrievedUser);

    retrievedUser = userDao.findUserByEmail(user.getEmail());
    assertThat(retrievedUser.getSmsNotification()).isEqualTo(false);
    assertThat(retrievedUser.getPushNotification()).isEqualTo(true);
    assertThat(retrievedUser.getEmailNotification()).isEqualTo(true);

    retrievedUser.setPushNotification(false);
    retrievedUser.setEmailNotification(false);
    retrievedUser.setSmsNotification(false);
    userDao.save(retrievedUser);

    retrievedUser = userDao.findUserByEmail(user.getEmail());
    assertThat(retrievedUser.getSmsNotification()).isEqualTo(false);
    assertThat(retrievedUser.getPushNotification()).isEqualTo(false);
    assertThat(retrievedUser.getEmailNotification()).isEqualTo(false);

  }

  private User createUser() {
    User user = new User();

    user.setEmail("test@test.com");
    user.setPassword("test123");
    user.setFirstName("john");
    user.setLastName("smith");
    user.setRole(Role.RESIDENT.name());
    user.setPhone("1234567890");
    user.setZipCode("98765");
    user.setCity("Sacramento");
    user.setState("CA");
    user.setAddress1("required street");
    user.setAddress2("optional street");
    user.setLatitude(10.5);
    user.setLongitude(20.0);
    user.setSmsNotification(true);

    return user;
  }

}
