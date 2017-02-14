package com.cgi.poc.dw;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.Validator;
import javax.ws.rs.client.Client;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgi.poc.dw.api.service.APICallerService;
import com.cgi.poc.dw.api.service.APIServiceFactory;
import com.cgi.poc.dw.api.service.impl.APICallerServiceImpl;
import com.cgi.poc.dw.auth.DBAuthenticator;
import com.cgi.poc.dw.auth.JwtAuthFilter;
import com.cgi.poc.dw.auth.UserRoleAuthorizer;
import com.cgi.poc.dw.auth.model.Keys;
import com.cgi.poc.dw.auth.service.JwtBuilderService;
import com.cgi.poc.dw.auth.service.JwtBuilderServiceImpl;
import com.cgi.poc.dw.auth.service.JwtReaderService;
import com.cgi.poc.dw.auth.service.JwtReaderServiceImpl;
import com.cgi.poc.dw.auth.service.KeyBuilderServiceImpl;
import com.cgi.poc.dw.auth.service.PasswordHash;
import com.cgi.poc.dw.auth.service.PasswordHashImpl;
import com.cgi.poc.dw.dao.model.FireEvent;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.dao.model.UserNotification;
import com.cgi.poc.dw.jobs.JobExecutionService;
import com.cgi.poc.dw.jobs.JobFactory;
import com.cgi.poc.dw.jobs.PollingDataJob;
import com.cgi.poc.dw.rest.resource.LoginResource;
import com.cgi.poc.dw.rest.resource.UserRegistrationResource;
import com.cgi.poc.dw.service.LoginService;
import com.cgi.poc.dw.service.LoginServiceImpl;
import com.cgi.poc.dw.service.UserRegistrationService;
import com.cgi.poc.dw.service.UserRegistrationServiceImpl;
import com.cgi.poc.dw.util.CustomConstraintViolationExceptionMapper;
import com.cgi.poc.dw.util.CustomSQLConstraintViolationException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Main Dropwizard Application class.
 */
public class CgiPocApplication extends Application<CgiPocConfiguration> {

	private final static Logger LOG = LoggerFactory.getLogger(CgiPocApplication.class);

	private final HibernateBundle<CgiPocConfiguration> hibernateBundle = new HibernateBundle<CgiPocConfiguration>(
			User.class, UserNotification.class, FireEvent.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(CgiPocConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	/**
	 * Application's main method.
	 * 
	 * @param args
	 *            parameter to start the application
	 * @throws Exception
	 *             all the exception in the application
	 */
	public static void main(final String[] args) throws Exception {
		new CgiPocApplication().run(args);
	}

	/**
	 * Method returns application name.
	 */
	@Override
	public String getName() {
		return "CGI-POC-DW";
	}

	/**
	 * Initializations.
	 */
	@Override
	public void initialize(final Bootstrap<CgiPocConfiguration> bootstrap) {
		/**
		 * Adding migrations bundle.
		 */
		bootstrap.addBundle(new MigrationsBundle<CgiPocConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(CgiPocConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		/**
		 * Adding Swagger bundle.
		 */
		bootstrap.addBundle(new SwaggerBundle<CgiPocConfiguration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(CgiPocConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});

		bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
				bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

		bootstrap.addBundle(hibernateBundle);
		bootstrap.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// want to ensure that dates are stored in UTC
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
		System.setProperty("user.timezone", "Etc/UTC");

	}

	@Override
	public void run(final CgiPocConfiguration configuration, final Environment environment)
			throws Exception {

		// logging requests & responses
		environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger("inbound"), Level.INFO,
				LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));

		Keys keys = new KeyBuilderServiceImpl().createKeys(configuration);

		// guice injectorctor = createInjector(configuration, environment,
		// keys);
		// resource r
		Injector injector = createInjector(configuration, environment, keys);

		registerResource(environment, injector, UserRegistrationResource.class);
		registerResource(environment, injector, LoginResource.class);
		registerResource(environment, injector, CustomConstraintViolationExceptionMapper.class);
		registerResource(environment, injector, CustomSQLConstraintViolationException.class);

		environment.jersey().property(ServerProperties.PROCESSING_RESPONSE_ERRORS_ENABLED, true);

		// CORS support
		configureCors(environment, configuration.getCorsConfiguration());
		// authentication
		registerAuthentication(environment, injector, keys);

		/**
		 * Adding Job Scheduler
		 */
		environment.lifecycle().manage(injector.getInstance(JobExecutionService.class));

		LOG.debug("Application started");
	}

	private void configureCors(Environment environment, CorsConfiguration corsConfiguration) {
		LOG.info("Configuring CORS filter");

		FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

		// if no list of allowed domains is provided, set for all domains
		if (corsConfiguration == null || Strings.isNullOrEmpty(corsConfiguration.getAllowedDomains())) {
			filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		} else {
			filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, corsConfiguration.getAllowedDomains());
		}

		filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
				getCrossOriginAllowedHeaders(corsConfiguration));
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,PATCH,DELETE,OPTIONS");

		// MUST be set to true for cross-origin Cookie auth to work
		filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
	}

	private String getCrossOriginAllowedHeaders(CorsConfiguration corsConfiguration) {
		List<String> allowedList = corsConfiguration.getAllowedHeaders();
		if (allowedList == null || allowedList.isEmpty()) {
			allowedList = new ArrayList<>();
			allowedList.add("Authorization");
		}
		return String.join(",", allowedList);
	}

	private void registerResource(Environment env, Injector injector, Class<?> theClass) {
		env.jersey().register(injector.getInstance(theClass));
	}

	private void registerAuthentication(Environment env, Injector injector, Keys keys) {

		final JwtConsumer consumer = new JwtConsumerBuilder().setRequireExpirationTime() // the
																							// JWT
																							// must
																							// have
																							// an
																							// expiration
																							// time
				.setMaxFutureValidityInMinutes(60) // but the expiration time
													// can't be too crazy
				.setAllowedClockSkewInSeconds(30) // allow some leeway in
													// validating time based
													// claims to account for
													// clock skew
				.setRequireSubject() // the JWT must have a subject claim
				.setVerificationKey(keys.getSignatureKey()) // verify the
															// signature with
															// the public key
				.setRelaxVerificationKeyValidation() // relaxes key length
														// requirement
				.build(); // create the JwtConsumer instance

		env.jersey().register(RolesAllowedDynamicFeature.class);
		final JwtAuthFilter<User> tokenAuthFilter = new JwtAuthFilter.Builder<User>().setJwtConsumer(consumer)
				.setRealm("realm").setPrefix("Bearer").setAuthorizer(injector.getInstance(UserRoleAuthorizer.class))
				.setAuthenticator(injector.getInstance(DBAuthenticator.class)).buildAuthFilter();

		env.jersey().register(new AuthDynamicFeature(tokenAuthFilter));
		env.jersey().register(new AuthValueFactoryProvider.Binder<User>(User.class));
	}

	private Injector createInjector(CgiPocConfiguration conf, Environment env, Keys keys)
			throws NoSuchAlgorithmException {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				// keys
				bind(Keys.class).toInstance(keys);
				
				//scheduler
				final Client client = new JerseyClientBuilder(env).build("EventsRESTClient");
				bind(JobsConfiguration.class).toInstance(conf.getJobsConfiguration());
				install(new FactoryModuleBuilder().implement(APICallerService.class, APICallerServiceImpl.class).build(APIServiceFactory.class));
				install(new FactoryModuleBuilder().implement(Runnable.class, PollingDataJob.class).build(JobFactory.class));
				bind(Client.class).toInstance(client);
				
				// services
				bind(Validator.class).toInstance(env.getValidator());
				bind(JwtReaderService.class).to(JwtReaderServiceImpl.class).asEagerSingleton();
				bind(JwtBuilderService.class).to(JwtBuilderServiceImpl.class).asEagerSingleton();
				bind(PasswordHash.class).to(PasswordHashImpl.class).asEagerSingleton();
				bind(LoginService.class).to(LoginServiceImpl.class).asEagerSingleton();
				bind(UserRegistrationService.class).to(UserRegistrationServiceImpl.class).asEagerSingleton();
				bindConstant().annotatedWith(Names.named("apiUrl")).to(conf.getApiURL());
			}

			@Singleton
			@Provides
			public SessionFactory provideSessionFactory() {

				SessionFactory sf = hibernateBundle.getSessionFactory();
				if (sf == null) {
					try {
						hibernateBundle.run(conf, env);
						return hibernateBundle.getSessionFactory();
					} catch (Exception e) {
						// logger.error("Unable to run hibernatebundle");
					}
				} else {
					return sf;
				}
				return null;
			}

		});
		return injector;
	}
}