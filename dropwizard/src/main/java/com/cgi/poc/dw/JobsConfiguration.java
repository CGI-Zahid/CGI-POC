package com.cgi.poc.dw;

import java.util.List;

import com.cgi.poc.dw.jobs.JobParameter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobsConfiguration {
	
	private int thread;
	private List<JobParameter> jobs;
	
	/**
	 * @return the thread 
	 * the number of threads to keep in the pool,
     * even if they are idle.
	 */
	@JsonProperty
	public int getThread() {
		return thread;
	}
	
	/**
	 * @param thread the thread to set
	 * the number of threads to keep in the pool,
     * even if they are idle
	 */
	@JsonProperty
	public void setThread(int thread) {
		this.thread = thread;
	}
	
	/**
	 * @return the jobs
	 */
	@JsonProperty
	public List<JobParameter> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	@JsonProperty
	public void setJobs(List<JobParameter> jobs) {
		this.jobs = jobs;
	}
	
}
