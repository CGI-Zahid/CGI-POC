package com.cgi.poc.dw.jobs;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.api.service.impl.APICallerServiceImpl;

/**
 * Job to call the rest API and get the events for specific types
 * 
 * @author vincent baylly
 *
 */
public class PollingDataJob implements Runnable{
	
	private APICallerService apiCallerService = new APICallerServiceImpl();
	private String eventUrl;
	
	/**
	 * default constructon
	 */
	public PollingDataJob(){
		
	}
	
	/**
	 * set the rest api url for the job
	 * 
	 * @param eventUrl the rest api url event
	 */
	public PollingDataJob(String eventUrl){
		this.eventUrl = eventUrl;
	}
	
	/**
	 * @return the eventUrl
	 */
	public String getEventUrl() {
		return eventUrl;
	}

	/**
	 * @param eventUrl the eventUrl to set
	 */
	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}
	
	/**
	 * run the job
	 */
	@Override
	public void run() {
		apiCallerService.callServiceAPI(eventUrl);
	}

}
