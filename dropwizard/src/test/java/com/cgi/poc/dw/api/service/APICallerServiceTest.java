package com.cgi.poc.dw.api.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.util.Random;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.client.Client;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.impl.APICallerServiceImpl;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.HibernateUtil;
import com.cgi.poc.dw.helper.IntegrationTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

@RunWith(MockitoJUnitRunner.class)
public class APICallerServiceTest extends IntegrationTest {

	@Mock
	private Appender<ILoggingEvent> mockAppender;
	@Captor
	private ArgumentCaptor<LoggingEvent> logCaptor;
	
	private Logger LOGGER = LoggerFactory.getLogger(APICallerServiceTest.class);

	private Client client;

	private SessionFactory sessionFactory;
	private FireEventDAO eventDAO;
	private Validator validator;

	@Before
	public void setup() {
		final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(mockAppender);
		final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
		configuration.setTimeToLive(Duration.seconds(120L));
		
		Random rn = new Random();
		int buildNum = rn.nextInt(1000) + 1;
		
		this.client = new JerseyClientBuilder(RULE.getEnvironment()).using(configuration).build("test" + buildNum);

		sessionFactory = HibernateUtil.getInstance().getSessionFactory();
		sessionFactory.openSession();
		sessionFactory.getCurrentSession().beginTransaction();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		eventDAO = new FireEventDAO(sessionFactory, validator);
	}

	@After
	public void teardown() {
		final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.detachAppender(mockAppender);
		
		client.close();
		sessionFactory.getCurrentSession().close();
	}

	@Test
	public void callServiceAPI_Success() {

		APICallerService apiCallerService = new APICallerServiceImpl(
				"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query", client, eventDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.INFO));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), containsString("Event to save"));

	}

	@Test
	public void callServiceAPI_ParseException() {

		APICallerService apiCallerService = new APICallerServiceImpl("http://www.google.com", client, eventDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), equalTo(
				"Unable to parse the result for the url event : http://www.google.com error:null"));

	}

	@Test
	public void callServiceAPI_IOException() {
		APICallerService apiCallerService = new APICallerServiceImpl(
				"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query", client, eventDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();
	}

	@Test
	public void callServiceAPI_DAOException() {
		try {
			APICallerService apiCallerService = new APICallerServiceImpl(
					"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query", client, null,
					sessionFactory);
			apiCallerService.callServiceAPI();
			fail("Expected ConflictException");
		} catch (RuntimeException e) {
			LOGGER.info("the runtime exception catch : {}", e.getMessage());
		}
	}

	@Test
	public void callServiceAPI_NullPointerException() {
		try {
			APICallerService apiCallerService = new APICallerServiceImpl(
					"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query", client, eventDAO,
					null);
			apiCallerService.callServiceAPI();
			fail("Expected ConflictException");
		} catch (NullPointerException e) {
			LOGGER.info("the null pointer exception catch : {}", e.getMessage());
		}
	}
}