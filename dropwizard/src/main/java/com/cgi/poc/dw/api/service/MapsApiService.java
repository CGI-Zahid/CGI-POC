package com.cgi.poc.dw.api.service;

import com.cgi.poc.dw.api.service.data.GeoCoordinates;

public interface MapsApiService {

  GeoCoordinates getGeoCoordinatesByZipCode(String zipCode);
}
