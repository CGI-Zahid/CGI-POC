package com.cgi.poc.dw.api.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * class to call API to collect the resources event information
 * 
 * @author vincent.baylly
 *
 */
public class APICallerServiceImpl implements APICallerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(APICallerServiceImpl.class);
	
	private Client client;
	private String eventUrl;
	private FireEventDAO fireEventDAO;
	
	@Inject
	public APICallerServiceImpl(@Assisted String eventUrl, @Assisted Client client, @Assisted FireEventDAO fireEventDAO){
		this.eventUrl = eventUrl;
		this.client = client;
		this.fireEventDAO = fireEventDAO;
	}

	/**
	 * call the service API to collect event information.
	 */
	public void callServiceAPI() {

		// TODO parameter which event to call
		// parameter to call the right event collector
		WebTarget webTarget = client.target(eventUrl + "?f=json&where=1%3D1&outFields=*&outSR=4326");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		LOGGER.debug("json : " + response.getEntity());

		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) (response.getEntity())));

		JSONParser parser = new JSONParser();

		Object obj = null;
		try {
			obj = parser.parse(br);
		} catch (IOException e) {
			LOGGER.error("Unable to parse the result for the url event : " + eventUrl + " error:" + e.getMessage());
		} catch (ParseException e) {
			LOGGER.error("Unable to parse the result for the url event : " + eventUrl + " error:" + e.getMessage());
		}

		JSONObject eventJson = (JSONObject) obj;

		parsingEventsResponse(eventJson);
	}

	/**
	 * 
	 * @param eventJson
	 */
	@SuppressWarnings("unchecked")
	private void parsingEventsResponse(JSONObject eventJson) {

		JSONArray features = (JSONArray) eventJson.get("features");
		Iterator<Object> iterator = features.iterator();
		while (iterator.hasNext()) {
			
			JSONObject feature = (JSONObject) iterator.next();
			JSONObject attributes = (JSONObject) feature.get("attributes");
			
			Set<String> keys = attributes.keySet();
			
			FireEvent event = new FireEvent();
			
			for( String key : keys ) {
				reflect(event, key, attributes);
			}

			JSONObject geometry = (JSONObject) feature.get("geometry");
			event.setGeometry(geometry.toJSONString());
			
			fireEventDAO.update(event);

			LOGGER.info("Event to save : " + event.toString());

		}
	}

	/**
	 * 
	 * @param event
	 * @param fieldName
	 * @param attributes
	 */
	public void reflect(FireEvent event, String fieldName, JSONObject attributes) {

		try {

			// with reflection
			Class<?> c = Class.forName("com.cgi.poc.dw.dao.model.FireEvent");

			String attributeName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			Field field = c.getDeclaredField(fieldName);

			if (field != null) {
				// Call the setter
				String setterName = "set" + attributeName;
				Method setter = c.getDeclaredMethod(setterName, field.getType());
				setter.setAccessible(true);
				if (attributes.get(fieldName) != null) {
					if (field.getType() == String.class) {
						String fieldValue = (String) attributes.get(fieldName);
						setter.invoke(event, fieldValue);
					} else if (field.getType() == BigDecimal.class) {
						BigDecimal bigDecimal = BigDecimal.valueOf((double) attributes.get(fieldName));
						setter.invoke(event, bigDecimal);
					} else if (field.getType() == Date.class) {
						Date date = new Date((long) attributes.get(fieldName));
						setter.invoke(event, date);
					} else if (field.getType() == Integer.class) {
						Integer integer = Integer.valueOf((int) attributes.get(fieldName));
						setter.invoke(event, integer);
					} else if (field.getType() == Integer.class) {
						Long aLong = Long.valueOf((long) attributes.get(fieldName));
						setter.invoke(event, aLong);
					} else {
						Object fieldValue = attributes.get(fieldName);
						setter.invoke(event, fieldValue);
					}
				}

				// call the getter
				String getterName = "get" + attributeName;
				Method getter = c.getDeclaredMethod(getterName);
				getter.setAccessible(true);
				LOGGER.debug("field " + attributeName + " to " + getter.invoke(event));
			}

		} catch (IllegalAccessException e) {
			LOGGER.error("error occur during introspection", e.getMessage());
		} catch (InvocationTargetException e) {
			LOGGER.error("error occur during introspection", e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.error("error occur during introspection", e.getMessage());
		} catch (SecurityException e) {
			LOGGER.error("error occur during introspection", e.getMessage());
		} catch (NoSuchMethodException e) {
			LOGGER.warn("the field " + fieldName + " doesn't exist in the Entity", e.getMessage());
		} catch (NoSuchFieldException e) {
			LOGGER.warn("the field " + fieldName + " doesn't exist in the Entity", e.getMessage());
		}
	}

}
