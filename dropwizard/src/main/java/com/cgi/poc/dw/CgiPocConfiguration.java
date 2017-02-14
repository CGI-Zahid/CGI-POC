package com.cgi.poc.dw;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Dropwizard Configuration class.
 */
public class CgiPocConfiguration extends Configuration {

	/**
	 * Assign swagger bundle configuration.
	 */
	@JsonProperty("swagger")
	public SwaggerBundleConfiguration swaggerBundleConfiguration;

	/* CORS */
	private CorsConfiguration corsConfiguration = new CorsConfiguration();

	/* JOB PARAMETER */
	private JobsConfiguration jobsConfiguration = new JobsConfiguration();

	/**
	 * A factory to read database configuration from the configuration file.
	 */
	@Valid
	@NotNull
	private DataSourceFactory dataSourceFactory = new DataSourceFactory();

	@NotEmpty
	private String jwtSignatureSecret;

	/**
	 * The URL to access exchange rate API.
	 */
	@NotEmpty
	private String apiURL;
	/**
	 * The key to access exchange rate API.
	 */
	@NotEmpty
	private String apiKey;

	/* CORS */
	public CorsConfiguration getCorsConfiguration() {
		return corsConfiguration;
	}

	@JsonProperty("cors")
	public void setCorsConfiguration(CorsConfiguration corsConfiguration) {
		this.corsConfiguration = corsConfiguration;
	}

	/**
	 * @return the jobsConfiguration
	 */
	@JsonProperty("scheduler")
	public JobsConfiguration getJobsConfiguration() {
		return jobsConfiguration;
	}

	/**
	 * @param jobsConfiguration
	 *            the jobsConfiguration to set
	 */
	public void setJobsConfiguration(JobsConfiguration jobsConfiguration) {
		this.jobsConfiguration = jobsConfiguration;
	}

	/**
	 * Obtain database connection parameters from the configuration file.
	 *
	 * @return Data source factory.
	 */
	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty
	public String getJwtSignatureSecret() {
		return jwtSignatureSecret;
	}

	@JsonProperty
	public void setJwtSignatureSecret(String jwtSignatureSecret) {
		this.jwtSignatureSecret = jwtSignatureSecret;
	}

	/**
	 * Jersey client default configuration.
	 */
	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();

	/**
	 * @return Jersey Client
	 */
	@JsonProperty("jerseyClient")
	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return jerseyClientConfiguration;
	}

	/**
	 * A getter for the URL of currency rates the API.
	 *
	 * @return the URL of currency rates the API.
	 */
	@JsonProperty
	public String getApiURL() {
		return apiURL;
	}

	/**
	 * A getter for the API key of currency rates the API.
	 *
	 * @return the API key of currency rates the API.
	 */
	@JsonProperty
	public String getApiKey() {
		return apiKey;
	}

}
