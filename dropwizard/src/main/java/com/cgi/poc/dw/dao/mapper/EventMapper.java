package com.cgi.poc.dw.dao.mapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.cgi.poc.dw.auth.model.EventType;
import com.cgi.poc.dw.dao.model.Event;

public class EventMapper implements ResultSetMapper<Event> {

	  public Event map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		Event event = new Event();
		
	    event.setId(r.getLong("id"));
	    event.setName(r.getString("name"));
	    
    	try {
			event.setUrl(new URL(r.getString("url")));
		} catch (MalformedURLException e) {
			new SQLException("MalformedURLException : " + e.getMessage());
		}
    	
	    event.setTimestamp(r.getTimestamp("timeStamp").toLocalDateTime());
	    event.setDescription(r.getString("description"));
	    event.setEventType(EventType.valueOf(r.getString("eventType")));
	    return event;
	  }
}
