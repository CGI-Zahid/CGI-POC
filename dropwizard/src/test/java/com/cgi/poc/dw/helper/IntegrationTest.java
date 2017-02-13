package com.cgi.poc.dw.helper;

import com.cgi.poc.dw.CgiPocApplication;
import com.cgi.poc.dw.CgiPocConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;

public class IntegrationTest {

  @ClassRule
  public static final DropwizardAppRule<CgiPocConfiguration> RULE = new DropwizardAppRule<CgiPocConfiguration>(
      CgiPocApplication.class, ResourceHelpers.resourceFilePath("cgi-test-integration.yml"));
}
