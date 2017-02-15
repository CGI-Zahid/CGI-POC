package com.cgi.poc.dw.api.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private SessionFactory sessionFactory;
	
	@Inject
	public APICallerServiceImpl(@Assisted String eventUrl, @Assisted Client client, @Assisted FireEventDAO fireEventDAO, SessionFactory sessionFactory){
		this.eventUrl = eventUrl;
		this.client = client;
		this.fireEventDAO = fireEventDAO;
		this.sessionFactory = sessionFactory;
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
			JSONObject eventJson = (JSONObject) obj;

			parsingEventsResponse(eventJson);
		} catch (IOException e) {
			LOGGER.error("Unable to parse the result for the url event : " + eventUrl + " error:" + e.getMessage());
		} catch (ParseException e) {
			LOGGER.error("Unable to parse the result for the url event : " + eventUrl + " error:" + e.getMessage());
		}
	}

	/**
	 * parsing the json to a java object.
	 * 
	 * @param featureJson the  json string get from the Rest API
	 */
	
	@SuppressWarnings("unchecked")
	private void parsingEventsResponse(JSONObject featureJson) throws IOException{

		JSONArray features = (JSONArray) featureJson.get("features");
		Iterator<Object> iterator = features.iterator();
		while (iterator.hasNext()) {
			
			JSONObject feature = (JSONObject) iterator.next();

	        JSONObject eventJson = (JSONObject)feature.get("attributes");
	        JSONObject geoJson = (JSONObject)feature.get("geometry");
	        ObjectMapper mapper = new ObjectMapper();
	        FireEvent event = mapper.readValue(eventJson.toString(), FireEvent.class);

			event.setGeometry(geoJson.toJSONString());
			
	        Session session = sessionFactory.openSession();
	        try {
	            ManagedSessionContext.bind(session);
	            Transaction transaction = session.beginTransaction();
	            try {
	            	
	                // Archive users based on last login date
	            	fireEventDAO.update(event);
	                transaction.commit();
	            }
	            catch (Exception e) {
	                transaction.rollback();
	                throw new RuntimeException(e);
	            }
	        } finally {
	            session.close();
	            ManagedSessionContext.unbind(sessionFactory);
	        }

			LOGGER.info("Event to save : " + event.toString());

		}
	}
	
}
