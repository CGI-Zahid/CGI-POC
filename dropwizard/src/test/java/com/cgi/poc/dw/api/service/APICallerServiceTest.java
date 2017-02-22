package com.cgi.poc.dw.api.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.service.EmailService;
import com.cgi.poc.dw.service.TextMessageService;
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

import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.HibernateUtil;
import com.cgi.poc.dw.helper.IntegrationTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.cgi.poc.dw.api.service.impl.EventFloodAPICallerServiceImpl;
import com.cgi.poc.dw.api.service.impl.EventWeatherAPICallerServiceImpl;
import com.cgi.poc.dw.api.service.impl.FireEventAPICallerServiceImpl;
import com.cgi.poc.dw.dao.EventFloodDAO;
import com.cgi.poc.dw.dao.EventWeatherDAO;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

@RunWith(MockitoJUnitRunner.class)
public class APICallerServiceTest extends IntegrationTest {

	@Mock
	private Appender<ILoggingEvent> mockAppender;
	@Captor
	private ArgumentCaptor<LoggingEvent> logCaptor;

	@Mock
	private TextMessageService textMessageService;

	@Mock
	private EmailService emailService;

	@Mock
	private UserDao userDao;

	private Logger LOGGER = LoggerFactory.getLogger(APICallerServiceTest.class);

	private Client client;

	private SessionFactory sessionFactory;
	private FireEventDAO fireEventDAO;
	private EventWeatherDAO eventWeatherDAO;
	private EventFloodDAO eventFloodDAO;
	private Validator validator;

	@Before
	public void setup() {
		final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.addAppender(mockAppender);
		final JerseyClientConfiguration configuration = new JerseyClientConfiguration();
		configuration.setTimeToLive(Duration.seconds(220L));
		
		Random rn = new Random();
		int buildNum = rn.nextInt(1000) + 1;
		
		this.client = new JerseyClientBuilder(RULE.getEnvironment()).using(configuration).build("test" + buildNum);

		sessionFactory = HibernateUtil.getInstance().getSessionFactory();
		sessionFactory.openSession();
		sessionFactory.getCurrentSession().beginTransaction();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		fireEventDAO = new FireEventDAO(sessionFactory, validator);
		eventWeatherDAO = new EventWeatherDAO(sessionFactory, validator);
		eventFloodDAO = new EventFloodDAO(sessionFactory, validator);
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

		FireEventAPICallerServiceImpl apiCallerService = new FireEventAPICallerServiceImpl(
				"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", client, fireEventDAO,
				sessionFactory, textMessageService, emailService, userDao);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.INFO));
		// Check the message being logged is correct
		if (!loggingEvent.getFormattedMessage().contains("Event")){
		   assertThat(loggingEvent.getFormattedMessage(), containsString("Event"));
		}
	}

	@Test
	public void callServiceAPI_ParseException() {

		FireEventAPICallerServiceImpl apiCallerService = new FireEventAPICallerServiceImpl("http://www.google.com", client, fireEventDAO,
				sessionFactory, textMessageService, emailService, userDao);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), containsString(
				"Unable to parse the result for the url event : http://www.google.com error"));
	}

	@Test
	public void callServiceAPI_IOException() {
		FireEventAPICallerServiceImpl apiCallerService = new FireEventAPICallerServiceImpl(
				"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", client, fireEventDAO,
				sessionFactory, textMessageService, emailService, userDao);
		apiCallerService.callServiceAPI();
	}

	@Test
	public void callServiceAPI_DAOException() {
		try {
			FireEventAPICallerServiceImpl apiCallerService = new FireEventAPICallerServiceImpl(
					"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", client, null,
					sessionFactory, textMessageService, emailService, userDao);
			apiCallerService.callServiceAPI();

			final LoggingEvent loggingEvent = logCaptor.getValue();
			assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
		} catch (RuntimeException e) {
			LOGGER.info("the runtime exception catch : {}", e.getMessage());
		}
	}

	@Test
	public void callServiceAPI_NullPointerException() {
		try {
			FireEventAPICallerServiceImpl apiCallerService = new FireEventAPICallerServiceImpl(
					"https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", client, fireEventDAO,
					null, textMessageService, emailService, userDao);
			apiCallerService.callServiceAPI();
			fail("Expected ConflictException");
		} catch (NullPointerException e) {
			LOGGER.info("the null pointer exception catch : {}", e.getMessage());
		}
	}
	@Test
	public void callWeatherServiceAPI_Success() {

		EventWeatherAPICallerServiceImpl apiCallerService = new EventWeatherAPICallerServiceImpl(
				"https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Forecasts_Guidance_Warnings/watch_warn_adv/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", 
                        client, eventWeatherDAO, sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.INFO));
		// Check the message being logged is correct
                if (!loggingEvent.getFormattedMessage().contains("Events to save : 0")){
		   assertThat(loggingEvent.getFormattedMessage(), containsString("Event to save"));
                }
	}

	@Test
	public void callWeatherServiceAPI_ParseException() {

		EventWeatherAPICallerServiceImpl apiCallerService = new EventWeatherAPICallerServiceImpl("http://www.google.com", client, eventWeatherDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), containsString(
				"Unable to parse the result for the url event : http://www.google.com error"));
	}

	@Test
	public void callWeatherServiceAPI_IOException() {
		EventWeatherAPICallerServiceImpl apiCallerService = new EventWeatherAPICallerServiceImpl(
				"https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Forecasts_Guidance_Warnings/watch_warn_adv/MapServer/0/query?f=json&where=1%3D1&outFields=*&outSR=4326", client, eventWeatherDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();
	}
        
	@Test
	public void callFloodServiceAPI_Success() {

		EventFloodAPICallerServiceImpl apiCallerService = new EventFloodAPICallerServiceImpl(
				"https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Observations/ahps_riv_gauges/MapServer/0/query?f=json&where=(status%20%3D%20%27moderate%27)%20AND%20(1%3D1)&spatialRel=esriSpatialRelIntersects&outFields=*&outSR=4326",
                        client, eventFloodDAO, sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.INFO));
		// Check the message being logged is correct
                if (!loggingEvent.getFormattedMessage().contains("Events to save : 0")){
		   assertThat(loggingEvent.getFormattedMessage(), containsString("Event to save"));
                }
	}

	@Test
	public void callFloodServiceAPI_ParseException() {

		EventFloodAPICallerServiceImpl apiCallerService = new EventFloodAPICallerServiceImpl("http://www.google.com", client, eventFloodDAO,
				sessionFactory);
		apiCallerService.callServiceAPI();

		// Now verify our logging interactions
		verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
		// Having a genricised captor means we don't need to cast
		final LoggingEvent loggingEvent = logCaptor.getValue();
		// Check log level is correct
		assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
		// Check the message being logged is correct
		assertThat(loggingEvent.getFormattedMessage(), containsString(
				"Unable to parse the result for the url event : http://www.google.com error"));
	}

	@Test
	public void callFloodServiceAPI_IOException() {
		EventFloodAPICallerServiceImpl apiCallerService = new EventFloodAPICallerServiceImpl(
				"https://idpgis.ncep.noaa.gov/arcgis/rest/services/NWS_Observations/ahps_riv_gauges/MapServer/0/query?f=json&where=(status%20%3D%20%27moderate%27)%20AND%20(1%3D1)&spatialRel=esriSpatialRelIntersects&outFields=*&outSR=4326",
                        client, eventFloodDAO, sessionFactory);
		apiCallerService.callServiceAPI();
	}



        
        
        
        
        
        
        
        
        
        
}