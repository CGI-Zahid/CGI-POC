package com.cgi.poc.dw.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.JobsConfiguration;

import io.dropwizard.lifecycle.Managed;

/**
 * 
 * @author vincent baylly
 *
 */
public class JobExecutionService implements Managed{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionService.class);
	
	//TODO to be add in the configuration
    private final ScheduledExecutorService service;
    
    private JobsConfiguration conf;	

	/**
	 * @param conf the job configuration
	 */
	public JobExecutionService(JobsConfiguration conf) {
		super();
		this.conf = conf;
		this.service = Executors.newScheduledThreadPool(conf.getThread());
	}

	/**
     * start the scheduler for the launch the jobs
     */
    @Override
    public void start() throws Exception {
    	LOGGER.debug("Starting jobs");
    	
    	
    	for(JobParameter jobParam : conf.getJobs()){
    		LOGGER.debug("Instanciate job : " + jobParam.toString());
        	service.scheduleAtFixedRate(new PollingDataJob(jobParam.getEventURL()), jobParam.getDelay(), jobParam.getPeriod(), TimeUnit.valueOf(jobParam.getTimeUnit()));
    	}

    }

    @Override
    public void stop() throws Exception {
    	LOGGER.debug("Shutting down");
        service.shutdown();
    }
	
}
