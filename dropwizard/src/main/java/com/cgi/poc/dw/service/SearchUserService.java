package com.cgi.poc.dw.service;

import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.api.service.data.GeoCoordinates;
import java.util.List;

public interface SearchUserService {
  /**
   * Search for user within given radius.
   *
   * @param coords list of GPS coordinates for emergency data.
   * @param radius used as radius of circle to find user's within the circle.
   */
  List<User> search(List<GeoCoordinates> coords, Double radius);

}
