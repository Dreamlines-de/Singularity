package com.hubspot.singularity.runner.base.config;

import java.util.Properties;

public interface SingularityRunnerConfiguration {
  void updateFromProperties(Properties properties);
}
