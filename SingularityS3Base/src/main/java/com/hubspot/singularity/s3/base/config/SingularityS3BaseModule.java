package com.hubspot.singularity.s3.base.config;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfigurationUtils;

public class SingularityS3BaseModule extends AbstractModule {
  public static final String BASE_CONFIG_PATH = "/etc/singularity.s3.base";

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  public SingularityS3Configuration providesS3Configuration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityS3Configuration.class, objectMapper, validator, BASE_CONFIG_PATH);
  }
}
