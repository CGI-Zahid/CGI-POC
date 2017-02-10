package com.cgi.poc.dw.dao.model;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cgi.poc.dw.auth.model.EventType;

public class Event {
	
	private long id;
	private String name;
	private EventType eventType;
	private URL url;
	private LocalDateTime timestamp;
	private String description;
	private List<Coordinate> coordinates = new ArrayList<Coordinate>();
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}
	
	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}
	
	/**
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the coordinates
	 */
	public List<Coordinate> getCoordinates() {
		return coordinates;
	}
	
	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
	
}
