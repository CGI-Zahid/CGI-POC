package com.cgi.poc.dw.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.JobsConfiguration;
import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.api.service.APIServiceFactory;
import com.cgi.poc.dw.dao.FireEventDAO;
import com.google.inject.Inject;

import io.dropwizard.lifecycle.Managed;

/**
 * 
 * @author vincent baylly
 *
 */
public class JobExecutionService implements Managed{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionService.class);

    private final ScheduledExecutorService service;
    
    @Inject
    JobFactory jobFactory;
    @Inject
    APIServiceFactory aPIServiceFactory;
    @Inject
    JobsConfiguration jobsConfiguration;
    @Inject
    Client client;
    @Inject
    FireEventDAO fireEventDAO;

	/**
	 * @param conf the job configuration
	 */
    @Inject
	public JobExecutionService(JobsConfiguration jobsConfiguration) {
		super();
		this.service = Executors.newScheduledThreadPool(jobsConfiguration.getThread());
	}

	/**
     * start the scheduler for the launch the jobs
     */
    @Override
    public void start() throws Exception {
    	LOGGER.debug("Starting jobs");
    	
    	for(JobParameter jobParam : jobsConfiguration.getJobs()){
    		LOGGER.debug("Instanciate job : " + jobParam.toString());
    		
    		APICallerService apiCallerService = aPIServiceFactory.create(client, jobParam.getEventURL(), fireEventDAO);
        	service.scheduleAtFixedRate(jobFactory.create(apiCallerService), jobParam.getDelay(), jobParam.getPeriod(), TimeUnit.valueOf(jobParam.getTimeUnit()));
    	}

    }

    @Override
    public void stop() throws Exception {
    	LOGGER.debug("Shutting down");
        service.shutdown();
    }
	
}
