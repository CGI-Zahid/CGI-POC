package com.cgi.poc.dw.api.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.auth.model.EventType;
import com.cgi.poc.dw.dao.model.Coordinate;
import com.cgi.poc.dw.dao.model.Event;

/**
 * class to call API to collect the resources event information
 * 
 * @author vincent.baylly
 *
 */
public class APICallerServiceImpl implements APICallerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(APICallerServiceImpl.class);

	private static final String CONFIG_PROPERTIES = "/config.properties";

	/**
	 * call the service API to collect event information.
	 */
	public void callServiceAPI() {

		String eventUrl = getEventURL();

		try {

			// TODO parameter which event to call
			// parameter to call the right event collector
			URL url = new URL(eventUrl + "?f=json&where=1%3D1&outFields=*&outSR=4326");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			JSONObject eventJson = null;
			LOGGER.debug("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				LOGGER.debug(output);
				eventJson = new JSONObject(output);
			}
			
			parsingEventsResponse(eventJson);

			conn.disconnect();

		} catch (MalformedURLException e) {

			LOGGER.error("Unable to use the url : " + eventUrl + " error:" + e.getMessage());

		} catch (IOException e) {
			LOGGER.error("Unable to parse the result for the url event : " + eventUrl + " error:" + e.getMessage());
		}
	}

	private void parsingEventsResponse(JSONObject eventJson) {
		
		List<Event> eventsToSave = new ArrayList<Event>();
		
		JSONArray features = (JSONArray) eventJson.get("features");
		Iterator<Object> iterator = features.iterator();
		while (iterator.hasNext()) {
			JSONObject feature = (JSONObject)iterator.next();
			JSONObject attributes = (JSONObject)feature.get("attributes");
			
			Event event = new Event();
			event.setName((String)attributes.get("incidentname"));
			
			//event.setDescription((String)attributes.get(""));
			event.setEventType(EventType.FIRE);
			
			JSONObject geometry = (JSONObject)feature.get("geometry");
			
			//TODO find a way to automate the get Geometry depending of the shape of the geometry
			Coordinate coord = new Coordinate();
		    double xLong = ((Double)geometry.get("x")).doubleValue();
		    double yLat = ((Double)attributes.get("y")).doubleValue();
		    coord.setxLong(xLong);
		    coord.setyLat(yLat);
		    
		    event.getCoordinates().add(coord);
		    
		    eventsToSave.add(event);
		    
		    LOGGER.info("Event to save : " + event.toString());
		    
		}
	}

	private String getEventURL() {
		// TODO to be add in an utils class
		Properties prop = new Properties();
		InputStream input;

		String eventUrl = "";

		try {
			input = new FileInputStream(CONFIG_PROPERTIES);

			// load a properties file
			prop.load(input);

			eventUrl = prop.getProperty("URL_FIRE_EVENTS");

		} catch (FileNotFoundException e) {
			LOGGER.error("error with the properties file : " + CONFIG_PROPERTIES + " " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("unable to load properties file " + CONFIG_PROPERTIES + " with the API Event URL "
					+ e.getMessage());
		}
		// End - TODO to be add in an utils class
		return eventUrl;
	}

}
