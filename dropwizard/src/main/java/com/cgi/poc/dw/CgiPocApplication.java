package com.cgi.poc.dw;

import com.cgi.poc.dw.auth.DBAuthenticator;
import com.cgi.poc.dw.auth.JwtAuthFilter;
import com.cgi.poc.dw.auth.UserRoleAuthorizer;
import com.cgi.poc.dw.auth.model.Keys;
import com.cgi.poc.dw.auth.service.JwtBuilderService;
import com.cgi.poc.dw.auth.service.JwtReaderService;
import com.cgi.poc.dw.auth.service.impl.JwtBuilderServiceImpl;
import com.cgi.poc.dw.auth.service.impl.JwtReaderServiceImpl;
import com.cgi.poc.dw.auth.service.impl.KeyBuilderServiceImpl;
import com.cgi.poc.dw.dao.AssetDao;
import com.cgi.poc.dw.dao.UserDao;
import com.cgi.poc.dw.dao.impl.AssetDaoImpl;
import com.cgi.poc.dw.dao.impl.UserDaoImpl;
import com.cgi.poc.dw.dao.model.User;
import com.cgi.poc.dw.rest.resource.AdminUserResource;
import com.cgi.poc.dw.rest.resource.AssetsResource;
import com.cgi.poc.dw.rest.resource.LoginResource;
import com.cgi.poc.dw.rest.resource.SignupResource;
import com.cgi.poc.dw.service.AdminUserService;
import com.cgi.poc.dw.service.AssetService;
import com.cgi.poc.dw.service.LoginService;
import com.cgi.poc.dw.service.SignupService;
import com.cgi.poc.dw.service.impl.AdminUserServiceImpl;
import com.cgi.poc.dw.service.impl.AssetServiceImpl;
import com.cgi.poc.dw.service.impl.LoginServiceImpl;
import com.cgi.poc.dw.service.impl.SignupServiceImpl;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.spinscale.dropwizard.jobs.JobsBundle;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Dropwizard Application class.
 */
public class CgiPocApplication extends Application<CgiPocConfiguration> {

  private final static Logger LOG = LoggerFactory.getLogger(CgiPocApplication.class);

  /**
   * Application's main method.
   * @params args the args
   * @throws Exception
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
    bootstrap.addBundle(
        new MigrationsBundle<CgiPocConfiguration>() {
          @Override
          public DataSourceFactory getDataSourceFactory(
              CgiPocConfiguration configuration) {
            return configuration.getDataSourceFactory();
          }
        });
    /**
     * Adding Swagger bundle.
     */
    bootstrap.addBundle(new SwaggerBundle<CgiPocConfiguration>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
          CgiPocConfiguration configuration) {
        return configuration.swaggerBundleConfiguration;
      }
    });
    
    /**
     * Adding Job Quartz Scheduler
     */
    //bootstrap.addBundle(new JobsBundle("com.cgi.poc.dw.jobs"));

    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)
        )
    );
  }

  @Override
  public void run(final CgiPocConfiguration configuration, final Environment environment)
      throws NoSuchAlgorithmException {

    // logging requests & responses
    environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger("inbound"),
        Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 8192));

    Keys keys = new KeyBuilderServiceImpl().createKeys(configuration);

    // guice injector
    Injector injector = createInjector(configuration, environment, keys);
    // resource registration
    registerResource(environment, injector, SignupResource.class);
    registerResource(environment, injector, LoginResource.class);
    registerResource(environment, injector, AdminUserResource.class);
    registerResource(environment, injector, AssetsResource.class);

    // CORS support
    configureCors(environment, configuration.getCorsConfiguration());
    // authentication
    registerAuthentication(environment, injector, keys);

    LOG.debug("Application started");
  }

  private void configureCors(Environment environment, CorsConfiguration corsConfiguration) {
    LOG.info("Configuring CORS filter");

    FilterRegistration.Dynamic filter = environment.servlets()
        .addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    // if no list of allowed domains is provided, set for all domains
    if (corsConfiguration == null || Strings.isNullOrEmpty(corsConfiguration.getAllowedDomains())) {
      filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    } else {
      filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM,
          corsConfiguration.getAllowedDomains());
    }

    filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
        getCrossOriginAllowedHeaders(corsConfiguration));
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
        "GET,PUT,POST,PATCH,DELETE,OPTIONS");

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

    final JwtConsumer consumer = new JwtConsumerBuilder()
        .setRequireExpirationTime() // the JWT must have an expiration time
        .setMaxFutureValidityInMinutes(60) // but the  expiration time can't be too crazy
        .setAllowedClockSkewInSeconds(
            30) // allow some leeway in validating time based claims to account for clock skew
        .setRequireSubject() // the JWT must have a subject claim
        .setVerificationKey(keys.getSignatureKey()) // verify the signature with the public key
        .setRelaxVerificationKeyValidation() // relaxes key length requirement
        .build(); // create the JwtConsumer instance

    env.jersey().register(RolesAllowedDynamicFeature.class);
    final JwtAuthFilter<User> tokenAuthFilter =
        new JwtAuthFilter.Builder<User>()
            .setJwtConsumer(consumer)
            .setRealm("realm")
            .setPrefix("Bearer")
            .setAuthorizer(injector.getInstance(UserRoleAuthorizer.class))
            .setAuthenticator(injector.getInstance(DBAuthenticator.class))
            .buildAuthFilter();

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
        // database
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(env, conf.getDataSourceFactory(), "mysqlDb");

        final UserDaoImpl userDaoImpl = jdbi.onDemand(UserDaoImpl.class);
        final AssetDaoImpl assetDaoImpl = jdbi.onDemand(AssetDaoImpl.class);
        bind(UserDao.class).toInstance(userDaoImpl);
        bind(AssetDao.class).toInstance(assetDaoImpl);

        // services
        bind(JwtReaderService.class).to(JwtReaderServiceImpl.class).asEagerSingleton();
        bind(JwtBuilderService.class).to(JwtBuilderServiceImpl.class).asEagerSingleton();
        bind(LoginService.class).to(LoginServiceImpl.class).asEagerSingleton();
        bind(SignupService.class).to(SignupServiceImpl.class).asEagerSingleton();
        bind(AdminUserService.class).to(AdminUserServiceImpl.class).asEagerSingleton();
        bind(AssetService.class).to(AssetServiceImpl.class).asEagerSingleton();
      }
    });
    return injector;
  }
}
