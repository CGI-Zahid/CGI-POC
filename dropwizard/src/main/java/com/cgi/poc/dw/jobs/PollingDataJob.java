package com.cgi.poc.dw.jobs;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.api.service.impl.APICallerServiceImpl;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;

@Every("${eventpollingtime}")
public class PollingDataJob extends Job{
	
	APICallerService apiCallerService = new APICallerServiceImpl();
	
	@Override
	public void doJob() {
		//TODO add the event url in parameter to call the right event
		apiCallerService.callServiceAPI();
	}

}
