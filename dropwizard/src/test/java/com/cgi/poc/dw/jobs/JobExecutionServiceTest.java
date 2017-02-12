package com.cgi.poc.dw.jobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cgi.poc.dw.JobsConfiguration;
import com.cgi.poc.dw.helper.IntegrationTest;

import io.dropwizard.lifecycle.JettyManaged;

public class JobExecutionServiceTest extends IntegrationTest{
	

	  @Test
	  public void verifyParemeterloadUnitTest() {
		  JobsConfiguration jobsConfiguration = RULE.getConfiguration().getJobsConfiguration();
		  assertNotNull(jobsConfiguration);
		  assertEquals(jobsConfiguration.getThread(), 2);
		  assertTrue(jobsConfiguration.getJobs().size() > 0);
		  assertEquals(jobsConfiguration.getJobs().get(0).getEventURL(), "https://wildfire.cr.usgs.gov/arcgis/rest/services/geomac_dyn/MapServer/0/query");
		  assertEquals(jobsConfiguration.getJobs().get(0).getDelay(), 10);
		  assertEquals(jobsConfiguration.getJobs().get(0).getPeriod(), 10);
		  assertEquals(jobsConfiguration.getJobs().get(0).getTimeUnit(), "SECONDS");
	  }
	  
	  @Test
	  public void startedServiceUnitTest() {
		  JettyManaged jettyManaged = (JettyManaged) RULE.getEnvironment().lifecycle().getManagedObjects().get(2);
		  assertTrue(jettyManaged.getManaged() instanceof JobExecutionService);
	  }
	
}
