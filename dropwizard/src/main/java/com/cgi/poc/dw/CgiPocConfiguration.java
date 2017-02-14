package com.cgi.poc.dw;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Dropwizard Configuration class.
 */
public class CgiPocConfiguration extends Configuration {

  private MapApiConfiguration mapApiConfiguration = new MapApiConfiguration();

  public MapApiConfiguration getMapApiConfiguration() {
    return mapApiConfiguration;
  }
  @JsonProperty("mapsApi")
  public void setMapApiConfiguration(MapApiConfiguration mapApiConfiguration) {
    this.mapApiConfiguration = mapApiConfiguration;
  }

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
   * Jersey client default configuration.
   */
  @Valid
  @NotNull
  private JerseyClientConfiguration jerseyClientConfiguration
      = new JerseyClientConfiguration();

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
   * @return Jersey Client
   */
  @JsonProperty("jerseyClient")
  public JerseyClientConfiguration getJerseyClientConfiguration() {
    return jerseyClientConfiguration;
  }
}
