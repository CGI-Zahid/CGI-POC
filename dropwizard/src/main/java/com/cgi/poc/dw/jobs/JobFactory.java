package com.cgi.poc.dw.jobs;

import com.cgi.poc.dw.api.service.APICallerService;

public interface JobFactory {
	
	PollingDataJob create(APICallerService apiCallerService);
}
