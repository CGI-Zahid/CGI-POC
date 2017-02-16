package com.cgi.poc.dw.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.sun.el.parser.ParseException;

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
	public APICallerServiceImpl(@Assisted String eventUrl, @Assisted Client client, @Assisted FireEventDAO fireEventDAO,
			SessionFactory sessionFactory) {
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

		// create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			JSONObject eventJson = objectMapper.readValue(((InputStream) response.getEntity()), JSONObject.class);

			parsingEventsResponse(eventJson);
		} catch (IOException e) {
			LOGGER.error("Unable to parse the result for the url event : {} error: {}", eventUrl, e.getMessage());
		}
	}

	/**
	 * parsing the json to a java object.
	 * 
	 * @param featureJson
	 *            the json string get from the Rest API
	 */
	private void parsingEventsResponse(JSONObject featureJson) throws IOException {

		try {

			JSONArray featuresArray = (JSONArray) featureJson.get("features");
			JSONArray featureArrayJson = featuresArray.getJSONArray(1);

			for (int i = 0; i > featureJson.length(); i++) {

				JSONObject feature;

				feature = (JSONObject) featureArrayJson.get(i);

				JSONObject eventJson = (JSONObject) feature.get("attributes");
				JSONObject geoJson = (JSONObject) feature.get("geometry");
				ObjectMapper mapper = new ObjectMapper();
				FireEvent event = mapper.readValue(eventJson.toString(), FireEvent.class);

				event.setGeometry(geoJson.toString());

				Session session = sessionFactory.openSession();
				try {
					ManagedSessionContext.bind(session);
					Transaction transaction = session.beginTransaction();
					try {

						// Archive users based on last login date
						fireEventDAO.save(event);
						transaction.commit();
					} catch (Exception e) {
						transaction.rollback();
						throw new RuntimeException(e);
					}
				} finally {
					session.close();
					ManagedSessionContext.unbind(sessionFactory);
				}

				LOGGER.info("Event to save : {}", event.toString());

			}

		} catch (JSONException e) {
			LOGGER.error("Unable to parse the result for the url event : {} error: {}", eventUrl, e.getMessage());
		}
	}

}
