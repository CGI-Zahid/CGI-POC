package com.cgi.poc.dw.jobs;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Use to specify the different parameter of a job
 * 
 * @author vincent baylly
 *
 */
public class JobParameter {

	private String eventURL;
	private int delay;
	private int period;
	private String timeUnit;

	
	/**
	 * @return the eventURL for the restApi to get the events
	 */
	@JsonProperty
	public String getEventURL() {
		return eventURL;
	}
	
	/**
	 * @param eventURL the eventURL to set the restApi to get the events
	 */
	@JsonProperty
	public void setEventURL(String eventURL) {
		this.eventURL = eventURL;
	}
	
	/**
	 * @return the delay between two job
	 */
	@JsonProperty
	public int getDelay() {
		return delay;
	}
	
	/**
	 * @param delay the delay to set between job
	 */
	@JsonProperty
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * @return the period. it's the delay after a job end to start an other one
	 */
	@JsonProperty
	public int getPeriod() {
		return period;
	}
	
	/**
	 * @param period the period to set delay after a job end to start an other one
	 */
	@JsonProperty
	public void setPeriod(int period) {
		this.period = period;
	}
	
	/**
	 * @return the timeUnit
	 */
	@JsonProperty
	public String getTimeUnit() {
		return timeUnit;
	}
	
	/**
	 * @param timeUnit the timeUnit to set
	 */
	@JsonProperty
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JobParameter [eventURL=" + eventURL + ", delay=" + delay + ", period=" + period + ", timeUnit="
				+ timeUnit + "]";
	}
	
}
