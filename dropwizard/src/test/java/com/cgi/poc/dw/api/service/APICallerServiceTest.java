package com.cgi.poc.dw.api.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.impl.APICallerServiceImpl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;



@RunWith(MockitoJUnitRunner.class)
public class APICallerServiceTest {
	
	@Mock
	private Appender<ILoggingEvent> mockAppender;
	@Captor
	private ArgumentCaptor<LoggingEvent> logCaptor;
	
    @Before
    public void setup() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }
	
    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }
    
	@Test
	public void callServiceAPI_Success() {
		
		APICallerService apiCallerService = new APICallerServiceImpl();
		apiCallerService.callServiceAPI("https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query");
		
		//Now verify our logging interactions
        verify(mockAppender, atLeast(1)).doAppend(logCaptor.capture());
        //Having a genricised captor means we don't need to cast
        final LoggingEvent loggingEvent = logCaptor.getValue();
        //Check log level is correct
        assertThat(loggingEvent.getLevel(), equalTo(Level.INFO));
        //Check the message being logged is correct
        assertThat(loggingEvent.getFormattedMessage(),
                containsString("Event to save"));

	}
    
	@Test
	public void callServiceAPI_MalFormURLException() {

		APICallerService apiCallerService = new APICallerServiceImpl();
		apiCallerService.callServiceAPI("not a valid url");

		//Now verify our logging interactions
        verify(mockAppender).doAppend(logCaptor.capture());
        //Having a genricised captor means we don't need to cast
        final LoggingEvent loggingEvent = logCaptor.getValue();
        //Check log level is correct
        assertThat(loggingEvent.getLevel(), equalTo(Level.ERROR));
        //Check the message being logged is correct
        assertThat(loggingEvent.getFormattedMessage(),
        		equalTo("Unable to use the url : not a valid url error:no protocol: not a valid url?f=json&where=1%3D1&outFields=*&outSR=4326"));

	}
	
	@Test
	public void callServiceAPI_JSONException() {
		try {
			APICallerService apiCallerService = new APICallerServiceImpl();
			apiCallerService.callServiceAPI("http://www.google.com");
			fail("Expected ConflictException");
		} catch (JSONException exception) {
			System.out.println(exception.getMessage());
			assertEquals(exception.getMessage(), "A JSONObject text must begin with '{' at 1 [character 2 line 1]");
		}
	}
}
