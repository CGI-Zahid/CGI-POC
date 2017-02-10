package com.cgi.poc.dw.dao.model;

public class Coordinate {
	
	private long id;
	private double xLong;
	private double yLat;
	
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
	 * @return the xLong
	 */
	public double getxLong() {
		return xLong;
	}
	
	/**
	 * @param xLong the xLong to set
	 */
	public void setxLong(double xLong) {
		this.xLong = xLong;
	}
	
	/**
	 * @return the yLat
	 */
	public double getyLat() {
		return yLat;
	}
	
	/**
	 * @param yLat the yLat to set
	 */
	public void setyLat(double yLat) {
		this.yLat = yLat;
	}
	
}
