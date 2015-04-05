package com.hubspot.singularity.oomkiller.config;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.hubspot.singularity.oomkiller.SingularityOOMKillerDriver;
import com.hubspot.singularity.oomkiller.SingularityOOMKillerMetrics;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfigurationUtils;
import com.hubspot.singularity.runner.base.shared.SingularityDriver;

public class SingularityOOMKillerModule extends AbstractModule {
  public static final String BASE_CONF_PATH = "/etc/singualrity.oomkiller";


  @Override
  protected void configure() {
    bind(SingularityDriver.class).to(SingularityOOMKillerDriver.class);
    bind(SingularityOOMKillerMetrics.class).in(Scopes.SINGLETON);
  }

  public SingularityOOMKillerConfiguration providesConfiguration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityOOMKillerConfiguration.class, objectMapper, validator, BASE_CONF_PATH);
  }

}
