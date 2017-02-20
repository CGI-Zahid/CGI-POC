package com.cgi.poc.dw.api.service.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import java.util.logging.Level;

/**
 * class to call API to collect the resources event information
 *
 * @author vincent.baylly
 *
 */
public abstract class APICallerServiceImpl implements APICallerService {

    private static final Logger LOG = LoggerFactory.getLogger(APICallerServiceImpl.class);

    private Client client;
    private String eventUrl;
    protected SessionFactory sessionFactory;

    @Inject
	public APICallerServiceImpl( String eventUrl,  Client client,
                SessionFactory sessionFactory) {
        this.eventUrl = eventUrl;
        this.client = client;
        this.sessionFactory = sessionFactory;
    }

    /**
     * call the service API to collect event information.
     */
    public void callServiceAPI() {

        // TODO parameter which event to call
        // parameter to call the right event collector
        WebTarget webTarget = client.target(eventUrl );
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        LOG.debug("json : " + response.getEntity());

        // create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode eventJson=null;
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser parser = jsonFactory.createParser((InputStream) response.getEntity());
	    parser.setCodec(objectMapper);
            eventJson = parser.readValueAs(ObjectNode.class);
        } catch (JsonParseException e) {
            LOG.error("Unable to parse the result for the url event : {} error: {}", webTarget.getUri(), e.getMessage());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(APICallerServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (eventJson != null){
             parsingEventsResponse(eventJson);
        }
    }

    /**
     * parsing the json to a java object.
     *
     * @param featureJson the json string get from the Rest API
     */
    private void parsingEventsResponse(ObjectNode featureJson)   {
           ArrayNode featuresArray = (ArrayNode) featureJson.get("features");
           LOG.info("Events to save : {}", featuresArray.size());
            for (int i = 0; i < featuresArray.size(); i++) {
                JsonNode feature = featuresArray.get(i);
                JsonNode event1 = feature.get("attributes");
                JsonNode geoJson = feature.get("geometry");
                mapAndSave ( event1,geoJson );
            }
    }
    abstract void mapAndSave (JsonNode eventJson, JsonNode geoJson);
}
