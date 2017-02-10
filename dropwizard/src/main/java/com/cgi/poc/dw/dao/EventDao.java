package com.cgi.poc.dw.dao;

import java.util.List;

import com.cgi.poc.dw.auth.model.EventType;
import com.cgi.poc.dw.dao.model.Event;

public interface EventDao {

	  void createEvent(Event event) throws Exception;

	  List<Event> findEventByType(EventType eventType);

	  List<Event> getAllEvents();
	
}
