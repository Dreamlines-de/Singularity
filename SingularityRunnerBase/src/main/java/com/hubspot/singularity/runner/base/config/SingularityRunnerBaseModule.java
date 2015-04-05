package com.hubspot.singularity.runner.base.config;

import java.lang.management.ManagementFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.mesos.JavaUtils;

public class SingularityRunnerBaseModule extends AbstractModule {
  public static final String BASE_CONFIG_PATH = "/etc/singularity.base";

  public static final String PROCESS_NAME = "process.name";

  @Override
  protected void configure() {
    final ObjectMapper objectMapper = JavaUtils.newObjectMapper();

    final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    bind(ObjectMapper.class).toInstance(objectMapper);
    bind(MetricRegistry.class).toInstance(new MetricRegistry());
    bind(Validator.class).toInstance(validatorFactory.getValidator());
  }

  @Provides
  @Singleton
  @Named(PROCESS_NAME)
  public String getProcessName() {
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (name != null && name.contains("@")) {
      return name.substring(0, name.indexOf("@"));
    }
    return name;
  }

  public SingularityRunnerBaseConfiguration providesConfiguration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityRunnerBaseConfiguration.class, objectMapper, validator, BASE_CONFIG_PATH);
  }
}
