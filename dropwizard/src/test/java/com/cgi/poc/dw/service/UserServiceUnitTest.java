package com.cgi.poc.dw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.cgi.poc.dw.MapApiConfiguration;
import com.cgi.poc.dw.auth.model.Role;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.model.NotificationType;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotificationType;
import com.cgi.poc.dw.util.ErrorInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.hibernate.HibernateException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

  @InjectMocks
  UserServiceImpl underTest;

  @Mock
  private UserDao userDao;

  @Mock
  private PasswordHash passwordHash;

  @Spy
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Mock
  private Client client;

  @Mock
  private MapApiConfiguration mapApiConfiguration;
  
  @Mock
  private EmailService emailService;

  @Mock
  private TextMessageService textMessageService;

  private User user;
  private User user1;

  @SuppressWarnings("unchecked")
	@Before
  public void createUser() throws IOException {
    user = new User();
    user.setEmail("success@gmail.com");
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
    user.setLatitude(0.0);
    user.setLongitude(0.0);
    UserNotificationType selNot = new UserNotificationType(Long.valueOf(NotificationType.SMS.ordinal()));
    Set<UserNotificationType> notificationType = new HashSet<>();
    notificationType.add(selNot);
    user.setNotificationType(notificationType);
    user1 = new User();
		user1.setEmail("resident@cgi.com");
		user1.setPassword("!QAZ1qaz");
		user1.setFirstName("john");
		user1.setLastName("doe");
		user1.setRole(Role.RESIDENT.name());
		user1.setPhone("1234567890");
		user1.setZipCode("95814");
		user1.setCity("Sacramento");
		user1.setState("California");
		user1.setRequiredStreet("required street");
		user1.setOptionalStreet("optional street");
		user1.setLatitude(38.5824933);
		user1.setLongitude(-121.4941738);
		UserNotificationType selNot1 = new UserNotificationType(Long.valueOf(NotificationType.SMS.ordinal()));
		Set<UserNotificationType> notificationType1 = new HashSet<>();
		notificationType.add(selNot1);
		user1.setNotificationType(notificationType1);

    JsonNode jsonRespone = new ObjectMapper()
        .readTree(getClass().getResource("/google_api_geocode_response.json"));

    when(mapApiConfiguration.getApiURL()).thenReturn("http://googleMapsURL.com");

    //mocking the Jersey Client
    WebTarget mockWebTarget = mock(WebTarget.class);
    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
    Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);
    when(mockBuilder.get(String.class)).thenReturn(jsonRespone.toString());
    
    doNothing().when(emailService).send(anyString(), anyList(), anyString(), anyString());
    when(textMessageService.send(anyString(), anyString())).thenReturn(true);
  }

  @Test
  public void registerUser_RegisterUserWithValidInput() throws Exception {

    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(user.getPassword())).thenReturn(saltedHash);
    Response actual = underTest.registerUser(user);

    assertEquals(200, actual.getStatus());
    assertEquals(user, actual.getEntity());
  }


  @Test
  public void registerUser_PasswordValidationFails() throws Exception {

    user.setPassword("a"); //one character password

    try {
      underTest.registerUser(user);
      fail("Expected an exception to be thrown");
    } catch (ConstraintViolationException exception) {
      Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
      for (ConstraintViolation<?> violation : constraintViolations) {
        String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType()
            .getCanonicalName();

        if (tmp.equals("password") && annotation.equals("javax.validation.constraints.Size")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo("must be at least 2 characters in length.");
        } else if (tmp.equals("password") && annotation
            .equals("com.cgi.poc.dw.util.PasswordType")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo(
                  "must be greater that 2 character, contain no whitespace, and have at least one number and one letter.");
        } else {
          fail("not an expected constraint violation");
        }
      }
    }
  }

  @Test
  public void registerUser_EmailValidationFails() throws Exception {

    user.setPassword("aaa"); //one character password
    user.setEmail("aaa"); //one character password

    try {
      underTest.registerUser(user);
      fail("Expected an exception to be thrown");
    } catch (ConstraintViolationException exception) {
      Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
      for (ConstraintViolation<?> violation : constraintViolations) {
        String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType()
            .getCanonicalName();

        if (tmp.equals("email") && annotation.equals("javax.validation.constraints.Pattern")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo("Invalid email address.");
        } else {
          fail("not an expected constraint violation");
        }
      }
    }
  }
  @Test
  public void registerUser_EmailValidationFailsWhiteSpave() throws Exception {

    user.setPassword("aaa"); //one character password
    user.setEmail("aaa @i23.com"); //one character password

    try {
      underTest.registerUser(user);
      fail("Expected an exception to be thrown");
    } catch (ConstraintViolationException exception) {
      Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
      for (ConstraintViolation<?> violation : constraintViolations) {
        String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType()
            .getCanonicalName();

        if (tmp.equals("email") && annotation.equals("javax.validation.constraints.Pattern")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo("Invalid email address.");
        } else {
          fail("not an expected constraint violation");
        }
      }
    }
  }
   @Test
  public void registerUser_EmailValidationFailsInvalidDomain() throws Exception {

    user.setPassword("aaa"); //one character password
    user.setEmail("aaa@ggg"); //(looks for patter @XX.YYY

    try {
      underTest.registerUser(user);
      fail("Expected an exception to be thrown");
    } catch (ConstraintViolationException exception) {
      Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
      for (ConstraintViolation<?> violation : constraintViolations) {
        String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType()
            .getCanonicalName();

        if (tmp.equals("email") && annotation.equals("javax.validation.constraints.Pattern")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo("Invalid email address.");
        } else {
          fail("not an expected constraint violation");
        }
      }
    }
  }
    @Test
  public void registerUserEmailValidationFailsNoDomeain() throws Exception {

    user.setPassword("aaa"); //one character password
    user.setEmail("aaa@"); //(looks for patter @XX.YYY

    try {
      underTest.registerUser(user);
      fail("Expected an exception to be thrown");
    } catch (ConstraintViolationException exception) {
      Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
      for (ConstraintViolation<?> violation : constraintViolations) {
        String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType()
            .getCanonicalName();

        if (tmp.equals("email") && annotation.equals("javax.validation.constraints.Pattern")) {
          assertThat(violation.getMessageTemplate())
              .isEqualTo("Invalid email address.");
        } else {
          fail("not an expected constraint violation");
        }
      }
    }
  }
  @Test
  public void registerUser_UserAlreadyExistsError() throws Exception {

    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(user.getPassword())).thenReturn(saltedHash);

    when(userDao.findUserByEmail(user.getEmail())).thenReturn(user);
 
      Response registerUser = underTest.registerUser(user);
      
      assertEquals(400, registerUser.getStatus());
      ErrorInfo errorInfo = (ErrorInfo) registerUser.getEntity();
      String actualMessage = errorInfo.getErrors().get(0).getMessage();
      String actualCode = errorInfo.getErrors().get(0).getCode();

      assertEquals("ERR4", actualCode);
      assertEquals("Duplicate entry for key '<email>'", actualMessage);
 
  }

  @Test
  public void registerUser_CreateUserFails() throws Exception {

    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(user.getPassword())).thenReturn(saltedHash);

    doThrow(new HibernateException("Something went wrong.")).when(userDao).save(any(User.class));
    Response registerUser = underTest.registerUser(user);
      
      assertEquals(500, registerUser.getStatus());
      ErrorInfo errorInfo = (ErrorInfo) registerUser.getEntity();
      String actualMessage = errorInfo.getErrors().get(0).getMessage();
      String actualCode = errorInfo.getErrors().get(0).getCode();

      assertEquals("ERR1", actualCode);
      assertEquals(
          "An Unknown exception has occured. Type: <org.hibernate.HibernateException>. Message: <Something went wrong.>",
          actualMessage);
 
  }

  @Test
  public void registerUser_PasswordHashingFails() throws Exception {

    doThrow(new InvalidKeySpecException("Something went wrong.")).when(passwordHash).createHash(user.getPassword());
       Response registerUser = underTest.registerUser(user);
       
      assertEquals(500, registerUser.getStatus());
      ErrorInfo errorInfo = (ErrorInfo) registerUser.getEntity();
      String actualMessage = errorInfo.getErrors().get(0).getMessage();
      String actualCode = errorInfo.getErrors().get(0).getCode();

      assertEquals("ERR1", actualCode);
      assertEquals(
          "An Unknown exception has occured. Type: <java.security.spec.InvalidKeySpecException>. Message: <Something went wrong.>",
          actualMessage);
  }
  
  @Test
  public void registerUser_MapsAPICommunicationFails()
      throws InvalidKeySpecException, NoSuchAlgorithmException {

    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(user.getPassword())).thenReturn(saltedHash);

    //mocking the Jersey Client
    WebTarget mockWebTarget = mock(WebTarget.class);
    when(client.target(anyString())).thenReturn(mockWebTarget);
    when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
    Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
    when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);

    doThrow(new ProcessingException("Processing failed.")).when(mockBuilder).get(String.class);

    Response response = underTest.registerUser(user);

    Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = (ErrorInfo) response.getEntity();
    String actualMessage = errorInfo.getErrors().get(0).getMessage();
    String actualCode = errorInfo.getErrors().get(0).getCode();

    assertEquals("ERR1", actualCode);
    assertEquals(
        "An Unknown exception has occured. Type: <javax.ws.rs.ProcessingException>. Message: <Processing failed.>",
        actualMessage);
  }

  @Test
  public void registerUser_ReturnsExpectedGeoCoordinates() throws Exception {

    String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
        + "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
    when(passwordHash.createHash(user.getPassword())).thenReturn(saltedHash);
    Response actual = underTest.registerUser(user);

    User actualUser = (User) actual.getEntity();

    assertEquals(200, actual.getStatus());
    assertEquals(new Double(38.5824933), actualUser.getLatitude());
    assertEquals(new Double(-121.4941738), actualUser.getLongitude());
  }
  
	@Test
	public void localizerUser_Success() throws Exception {

		user.setGeoLocLatitude(38.5824933);
		user.setGeoLocLongitude(-121.4941738);

		Response actual = underTest.setLocalization(user);

		assertEquals(200, actual.getStatus());
	}

  @Test
	public void updateUser_UpdateUserWithValidInput() throws Exception {

		String saltedHash = "9e5f3dd72fbd5f309131364baf42b446f570629f4a809390be533f:"
				+ "1db93c4885d4bf980e92286d74da720dc298fdc1a29c89cf9c67ce";
		when(passwordHash.createHash(user1.getPassword())).thenReturn(saltedHash);
		when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
		Response actual = underTest.updateUser(user1);

		assertEquals(200, actual.getStatus());
		assertEquals(user1, actual.getEntity());
	}

	@Test
	public void updateUser_PasswordValidationFails() throws Exception {

		user1.setPassword("a"); // one character password

		try {
			when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
			underTest.updateUser(user1);
			fail("Expected an exception to be thrown");
		} catch (ConstraintViolationException exception) {
			Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
			for (ConstraintViolation<?> violation : constraintViolations) {
				String tmp = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
				String annotation = violation.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName();

				if (tmp.equals("password") && annotation.equals("javax.validation.constraints.Size")) {
					assertThat(violation.getMessageTemplate()).isEqualTo("must be at least 2 characters in length.");
				} else if (tmp.equals("password") && annotation.equals("com.cgi.poc.dw.util.PasswordType")) {
					assertThat(violation.getMessageTemplate()).isEqualTo(
							"must be greater that 2 character, contain no whitespace, and have at least one number and one letter.");
				} else {
					fail("not an expected constraint violation");
				}
			}
		}
	}

	@Test
	public void updateUser_UpdateUserFails() throws Exception {

		String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
				+ "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
		when(passwordHash.createHash(user1.getPassword())).thenReturn(saltedHash);

		doThrow(new HibernateException("Something went wrong.")).when(userDao).save(any(User.class));

		when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
		Response registerUser = underTest.updateUser(user1);

		assertEquals(500, registerUser.getStatus());
		ErrorInfo errorInfo = (ErrorInfo) registerUser.getEntity();
		String actualMessage = errorInfo.getErrors().get(0).getMessage();
		String actualCode = errorInfo.getErrors().get(0).getCode();

		assertEquals("ERR1", actualCode);
		assertEquals(
				"An Unknown exception has occured. Type: <org.hibernate.HibernateException>. Message: <Something went wrong.>",
				actualMessage);

	}

	@Test
	public void updateUser_PasswordHashingFails() throws Exception {

		doThrow(new InvalidKeySpecException("Something went wrong.")).when(passwordHash).createHash(user1.getPassword());
		when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
		Response updatedUser = underTest.updateUser(user1);

		assertEquals(500, updatedUser.getStatus());
		ErrorInfo errorInfo = (ErrorInfo) updatedUser.getEntity();
		String actualMessage = errorInfo.getErrors().get(0).getMessage();
		String actualCode = errorInfo.getErrors().get(0).getCode();

		assertEquals("ERR1", actualCode);
		assertEquals(
				"An Unknown exception has occured. Type: <java.security.spec.InvalidKeySpecException>. Message: <Something went wrong.>",
				actualMessage);
	}

	@Test
	public void updateUser_MapsAPICommunicationFails() throws InvalidKeySpecException, NoSuchAlgorithmException {

		String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
				+ "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
		when(passwordHash.createHash(user1.getPassword())).thenReturn(saltedHash);

		// mocking the Jersey Client
		WebTarget mockWebTarget = mock(WebTarget.class);
		when(client.target(anyString())).thenReturn(mockWebTarget);
		when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
		Invocation.Builder mockBuilder = mock(Invocation.Builder.class);
		when(mockWebTarget.request(anyString())).thenReturn(mockBuilder);

		doThrow(new ProcessingException("Processing failed.")).when(mockBuilder).get(String.class);
		when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
		Response response = underTest.registerUser(user);

    Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    ErrorInfo errorInfo = (ErrorInfo) response.getEntity();
    String actualMessage = errorInfo.getErrors().get(0).getMessage();
    String actualCode = errorInfo.getErrors().get(0).getCode();

    assertEquals("ERR1", actualCode);
    assertEquals(
        "An Unknown exception has occured. Type: <javax.ws.rs.ProcessingException>. Message: <Processing failed.>",
        actualMessage);
	}

	@Test
	public void updateUser_ReturnsExpectedGeoCoordinates() throws Exception {

		String saltedHash = "518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:"
				+ "8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af";
		when(passwordHash.createHash(user1.getPassword())).thenReturn(saltedHash);

		when(userDao.findUserByEmail(user1.getEmail())).thenReturn(user1);
		Response actual = underTest.updateUser(user1);

		User actualUser = (User) actual.getEntity();

		assertEquals(200, actual.getStatus());
		assertEquals(new Double(38.5824933), actualUser.getLatitude());
		assertEquals(new Double(-121.4941738), actualUser.getLongitude());

	}

}
