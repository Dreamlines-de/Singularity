package com.hubspot.singularity.runner.base.config;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.ConfigurationValidationException;
import io.dropwizard.configuration.FileConfigurationSourceProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.error.YAMLException;
import com.google.common.base.Throwables;

import static com.google.common.base.Preconditions.checkNotNull;

public class SingularityRunnerConfigurationFactory<T> {
  private final Class<T> klass;
  private final ObjectMapper mapper;
  private final Validator validator;
  private final YAMLFactory yamlFactory;

  /**
   * Creates a new configuration factory for the given class.
   *
   * @param klass          the configuration class
   * @param validator      the validator to use
   * @param objectMapper   the Jackson {@link ObjectMapper} to use
   */
  public SingularityRunnerConfigurationFactory(Class<T> klass,
                              Validator validator,
                              ObjectMapper objectMapper) {
    this.klass = klass;
    this.mapper = objectMapper.copy();
    this.validator = validator;
    this.yamlFactory = new YAMLFactory();
  }

  /**
   * Loads, parses, binds, and validates a configuration object.
   *
   * @param provider the provider to to use for reading configuration files
   * @param path     the path of the configuration file
   * @return a validated configuration object
   * @throws IOException            if there is an error reading the file
   * @throws ConfigurationException if there is an error parsing or validating the file
   */
  public T build(ConfigurationSourceProvider provider, String path, boolean validate) throws IOException, ConfigurationException {
    try (InputStream input = provider.open(checkNotNull(path))) {
      final JsonNode node = mapper.readTree(yamlFactory.createParser(input));
      return build(node, path, validate);
    } catch (YAMLException e) {
      /*ConfigurationParsingException.Builder builder = ConfigurationParsingException
              .builder("Malformed YAML")
              .setCause(e)
              .setDetail(e.getMessage());

      if (e instanceof MarkedYAMLException) {
        builder.setLocation(((MarkedYAMLException) e).getProblemMark());
      }

      throw builder.build(path);*/
      throw Throwables.propagate(e);
    }
  }

  /**
   * Loads, parses, binds, and validates a configuration object from a file.
   *
   * @param file the path of the configuration file
   * @return a validated configuration object
   * @throws IOException            if there is an error reading the file
   * @throws ConfigurationException if there is an error parsing or validating the file
   */
  public T build(File file, boolean validate) throws IOException, ConfigurationException {
    return build(new FileConfigurationSourceProvider(), file.toString(), validate);
  }

  /**
   * Loads, parses, binds, and validates a configuration object from an empty document.
   *
   * @return a validated configuration object
   * @throws IOException            if there is an error reading the file
   * @throws ConfigurationException if there is an error parsing or validating the file
   */
  public T build(boolean validate) throws IOException, ConfigurationException {
    return build(JsonNodeFactory.instance.objectNode(), "default configuration", validate);
  }

  private T build(JsonNode node, String path, boolean validate) throws IOException, ConfigurationException {
    try {
      final T config = mapper.readValue(new TreeTraversingParser(node), klass);
      if (validate) {
        validate(path, config);
      }
      return config;
    } catch (UnrecognizedPropertyException e) {
      Collection<Object> knownProperties = e.getKnownPropertyIds();
      List<String> properties = new ArrayList<>(knownProperties.size());
      for (Object property : knownProperties) {
        properties.add(property.toString());
      }
      /*throw ConfigurationParsingException.builder("Unrecognized field")
              .setFieldPath(e.getPath())
              .setLocation(e.getLocation())
              .addSuggestions(properties)
              .setSuggestionBase(e.getPropertyName())
              .setCause(e)
              .build(path);*/
      throw Throwables.propagate(e);
    } catch (InvalidFormatException e) {
      /*String sourceType = e.getValue().getClass().getSimpleName();
      String targetType = e.getTargetType().getSimpleName();
      throw ConfigurationParsingException.builder("Incorrect type of value")
              .setDetail("is of type: " + sourceType + ", expected: " + targetType)
              .setLocation(e.getLocation())
              .setFieldPath(e.getPath())
              .setCause(e)
              .build(path);*/
      throw Throwables.propagate(e);
    } catch (JsonMappingException e) {
      /*throw ConfigurationParsingException.builder("Failed to parse configuration")
              .setDetail(e.getMessage())
              .setFieldPath(e.getPath())
              .setLocation(e.getLocation())
              .setCause(e)
              .build(path);*/
      throw Throwables.propagate(e);
    }
  }

  public void validate(String path, T config) throws ConfigurationValidationException {
    final Set<ConstraintViolation<T>> violations = validator.validate(config);
    if (!violations.isEmpty()) {
      throw new ConfigurationValidationException(path, violations);
    }
  }
}
