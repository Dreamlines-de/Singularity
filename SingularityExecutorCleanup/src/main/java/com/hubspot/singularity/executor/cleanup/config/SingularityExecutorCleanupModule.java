package com.hubspot.singularity.executor.cleanup.config;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfigurationUtils;

public class SingularityExecutorCleanupModule extends AbstractModule {
  public static final String BASE_CONFIG_PATH = "/etc/singularity.executor.cleanup";

  @Override
  protected void configure() {

  }

  public SingularityExecutorCleanupConfiguration providesConfiguration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityExecutorCleanupConfiguration.class, objectMapper, validator, BASE_CONFIG_PATH);
  }
}
