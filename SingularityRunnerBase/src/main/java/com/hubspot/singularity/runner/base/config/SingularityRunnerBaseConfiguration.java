package com.hubspot.singularity.runner.base.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

import javax.validation.constraints.NotNull;

import com.hubspot.mesos.JavaUtils;
import com.hubspot.singularity.runner.base.constraints.DirectoryExists;

public class SingularityRunnerBaseConfiguration implements SingularityRunnerConfiguration {
  public static final String LOGGING_PATTERN = "logging.pattern";
  public static final String ROOT_LOG_DIRECTORY = "root.log.directory";
  public static final String ROOT_LOG_FILENAME = "root.log.filename";

  public static final String ROOT_LOG_LEVEL = "root.log.level";
  public static final String HUBSPOT_LOG_LEVEL = "hubspot.log.level";
  public static final String OBFUSCATE_KEYS = "obfuscate.keys.comma.separated";

  public static final String LOG_METADATA_DIRECTORY = "logwatcher.metadata.directory";
  public static final String LOG_METADATA_SUFFIX = "logwatcher.metadata.suffix";

  public static final String S3_METADATA_SUFFIX = "s3uploader.metadata.suffix";
  public static final String S3_METADATA_DIRECTORY = "s3uploader.metadata.directory";

  @NotEmpty
  @DirectoryExists
  private String rootLogPath;

  @NotEmpty
  private String rootLogLevel = "INFO";

  @NotEmpty
  private String hubSpotLogLevel = "INFO";

  @NotEmpty
  private String loggingPattern = JavaUtils.LOGBACK_LOGGING_PATTERN;

  @NotNull
  private List<String> obfuscateKeys = Collections.emptyList();

  @NotEmpty
  @DirectoryExists
  private String s3UploaderMetadataDirectory;

  @NotEmpty
  private String s3UploaderMetadataSuffix = ".s3.json";

  @NotEmpty
  @DirectoryExists
  private String logWatcherMetadataDirectory;

  @NotEmpty
  private String logWatcherMetadataSuffix = ".tail.json";

  public void setRootLogPath(String rootLogPath) {
    this.rootLogPath = rootLogPath;
  }

  public String getHubSpotLogLevel() {
    return hubSpotLogLevel;
  }

  public void setHubSpotLogLevel(String hubSpotLogLevel) {
    this.hubSpotLogLevel = hubSpotLogLevel;
  }

  public String getLoggingPattern() {
    return loggingPattern;
  }

  public void setLoggingPattern(String loggingPattern) {
    this.loggingPattern = loggingPattern;
  }

  public List<String> getObfuscateKeys() {
    return obfuscateKeys;
  }

  public void setObfuscateKeys(List<String> obfuscateKeys) {
    this.obfuscateKeys = obfuscateKeys;
  }

  public Path getS3UploaderMetadataDirectory() {
    return Paths.get(s3UploaderMetadataDirectory);
  }

  public void setS3UploaderMetadataDirectory(String s3UploaderMetadataDirectory) {
    this.s3UploaderMetadataDirectory = s3UploaderMetadataDirectory;
  }

  public String getS3UploaderMetadataSuffix() {
    return s3UploaderMetadataSuffix;
  }

  public void setS3UploaderMetadataSuffix(String s3UploaderMetadataSuffix) {
    this.s3UploaderMetadataSuffix = s3UploaderMetadataSuffix;
  }

  public Path getLogWatcherMetadataDirectory() {
    return Paths.get(logWatcherMetadataDirectory);
  }

  public void setLogWatcherMetadataDirectory(String logWatcherMetadataDirectory) {
    this.logWatcherMetadataDirectory = logWatcherMetadataDirectory;
  }

  public String getLogWatcherMetadataSuffix() {
    return logWatcherMetadataSuffix;
  }

  public void setLogWatcherMetadataSuffix(String logWatcherMetadataSuffix) {
    this.logWatcherMetadataSuffix = logWatcherMetadataSuffix;
  }

  public String getRootLogLevel() {
    return rootLogLevel;
  }

  public void setRootLogLevel(String rootLogLevel) {
    this.rootLogLevel = rootLogLevel;
  }

  private boolean shouldObfuscateValue(String key) {
    for (String obfuscateKey : obfuscateKeys) {
      if (key.contains(obfuscateKey)) {
        return true;
      }
    }

    return false;
  }

  public static String obfuscateValue(String value) {
    if (value == null) {
      return value;
    }

    if (value.length() > 4) {
      return String.format("***************%s", value.substring(value.length() - 4, value.length()));
    } else {
      return "(OMITTED)";
    }
  }

  public String getRootLogPath() {
    return rootLogPath;
  }

  public Logger prepareRootLogger(LoggerContext context) {
    Logger rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    rootLogger.detachAndStopAllAppenders();

    return rootLogger;
  }

  public Logger configureRootLogger(String rootLogLevel) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    Logger rootLogger = prepareRootLogger(context);

    rootLogger.setLevel(Level.toLevel(rootLogLevel));

    Logger hubSpotLogger = context.getLogger("com.hubspot");

    hubSpotLogger.setLevel(Level.toLevel(hubSpotLogLevel));

    rootLogger.addAppender(buildFileAppender(context, rootLogPath));

    return rootLogger;
  }

  public FileAppender<ILoggingEvent> buildFileAppender(LoggerContext context, String file) {
    FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
    fileAppender.setFile(file);
    fileAppender.setContext(context);
    fileAppender.setPrudent(true);

    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(context);
    encoder.setPattern(loggingPattern);
    encoder.start();

    fileAppender.setEncoder(encoder);
    fileAppender.start();

    return fileAppender;
  }

  @Override
  public void updateFromProperties(Properties properties) {
    if (properties.containsKey(LOGGING_PATTERN)) {
      setLoggingPattern(properties.getProperty(LOGGING_PATTERN));
    }

    if (properties.containsKey(ROOT_LOG_DIRECTORY)) {
      setRootLogPath(properties.getProperty(ROOT_LOG_DIRECTORY));
    }

    if (properties.containsKey(ROOT_LOG_LEVEL)) {
      setRootLogLevel(properties.getProperty(ROOT_LOG_LEVEL));
    }

    if (properties.containsKey(HUBSPOT_LOG_LEVEL)) {
      setHubSpotLogLevel(properties.getProperty(HUBSPOT_LOG_LEVEL));
    }

    if (properties.containsKey(OBFUSCATE_KEYS)) {
      setObfuscateKeys(JavaUtils.COMMA_SPLITTER.splitToList(properties.getProperty(OBFUSCATE_KEYS)));
    }

    if (properties.containsKey(LOG_METADATA_DIRECTORY)) {
      setLogWatcherMetadataDirectory(properties.getProperty(LOG_METADATA_DIRECTORY));
    }

    if (properties.containsKey(LOG_METADATA_SUFFIX)) {
      setLogWatcherMetadataSuffix(properties.getProperty(LOG_METADATA_SUFFIX));
    }

    if (properties.containsKey(S3_METADATA_DIRECTORY)) {
      setS3UploaderMetadataDirectory(properties.getProperty(S3_METADATA_DIRECTORY));
    }

    if (properties.containsKey(S3_METADATA_SUFFIX)) {
      setS3UploaderMetadataSuffix(properties.getProperty(S3_METADATA_SUFFIX));
    }
  }
}
