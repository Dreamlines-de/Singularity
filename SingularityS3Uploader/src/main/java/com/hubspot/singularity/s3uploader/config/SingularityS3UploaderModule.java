package com.hubspot.singularity.s3uploader.config;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfigurationUtils;
import com.hubspot.singularity.runner.base.shared.SingularityDriver;
import com.hubspot.singularity.s3uploader.SingularityS3UploaderDriver;

public class SingularityS3UploaderModule extends AbstractModule {
  public static final String BASE_CONFIG_PATH = "/etc/singularity.s3uploader";

  @Override
  protected void configure() {
    bind(SingularityDriver.class).to(SingularityS3UploaderDriver.class);
  }

  @Provides
  @Singleton
  public SingularityS3UploaderConfiguration providesConfiguration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityS3UploaderConfiguration.class, objectMapper, validator, BASE_CONFIG_PATH);
  }

}
