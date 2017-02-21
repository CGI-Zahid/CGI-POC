package com.cgi.poc.dw.api.service.impl;

import com.cgi.poc.dw.MapApiConfiguration;
import com.cgi.poc.dw.api.service.MapsApiService;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import com.cgi.poc.dw.util.ErrorInfo;
import com.cgi.poc.dw.util.ErrorInfoHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapsApiServiceImpl implements MapsApiService {

  private final static Logger LOG = LoggerFactory.getLogger(MapsApiServiceImpl.class);

  private MapApiConfiguration mapApiConfiguration;

  private Client client;

  private static final String ADDRESS = "address";
  
  @Inject
  public MapsApiServiceImpl(MapApiConfiguration mapApiConfiguration, Client client) {
    this.mapApiConfiguration = mapApiConfiguration;
    this.client = client;
  }

  @Override
  public GeoCoordinates getGeoCoordinatesByZipCode(String zipCode) {

    GeoCoordinates geoCoordinates = new GeoCoordinates();
    try {
      String response = client
          .target(mapApiConfiguration.getApiURL())
          .queryParam(ADDRESS, zipCode)
          .request(MediaType.APPLICATION_JSON)
          .get(String.class);

      final ObjectNode node = new ObjectMapper().readValue(response, ObjectNode.class);

      if (node.path("results").size() > 0 && "OK".equals(node.path("status").asText())) {
        geoCoordinates.setLatitude(
            node.get("results").get(0).get("geometry").get("location").get("lat").asDouble());
        geoCoordinates.setLongitude(
            node.get("results").get(0).get("geometry").get("location").get("lng").asDouble());
      } else {
        geoCoordinates.setLatitude(0.0);
        geoCoordinates.setLongitude(0.0);
      }
    } catch (Exception exception) {
      LOG.error("Unable to make maps api call.", exception);
      ErrorInfo errRet = ErrorInfoHelper.getUnknownExceptionErrorMessage(exception);
      throw new WebApplicationException(
          Response.noContent().status(Status.INTERNAL_SERVER_ERROR).entity(errRet).build());
    }

    return geoCoordinates;
  }
}
