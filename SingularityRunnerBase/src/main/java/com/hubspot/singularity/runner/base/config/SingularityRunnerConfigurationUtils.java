package com.hubspot.singularity.runner.base.config;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SingularityRunnerConfigurationUtils {
  private SingularityRunnerConfigurationUtils() {

  }

  public static <T extends SingularityRunnerConfiguration> T buildConfiguration(Class<T> klass, ObjectMapper objectMapper, Validator validator, String baseConfigPath) throws Exception {
    final SingularityRunnerConfigurationFactory<T> factory = new SingularityRunnerConfigurationFactory<>(klass, validator, objectMapper);

    final String yamlPath = String.format("%s.yaml", baseConfigPath);
    final String propsPath = String.format("%s.properties", baseConfigPath);

    final File yamlFile = new File(yamlPath);
    final File propsFile = new File(propsPath);
    String configName = "default config";

    final T config;

    if (yamlFile.exists()) {
      config = factory.build(yamlFile, false);
      configName = yamlPath;
    } else {
      config = factory.build(false);
    }

    if (propsFile.exists()) {
      final Properties properties = new Properties();
      try (BufferedReader br = Files.newBufferedReader(Paths.get(propsPath), Charset.defaultCharset())) {
        properties.load(br);
      }
      config.updateFromProperties(properties);
      configName = configName + " + " + propsPath;
    }

    factory.validate(configName, config);

    return config;
  }
}
