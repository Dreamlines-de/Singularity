package com.hubspot.singularity.s3downloader.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.hubspot.singularity.runner.base.config.SingularityRunnerConfigurationUtils;
import com.hubspot.singularity.runner.base.shared.SingularityDriver;
import com.hubspot.singularity.s3.base.ArtifactManager;
import com.hubspot.singularity.s3downloader.server.SingularityS3DownloaderServer;

public class SingularityS3DownloaderModule extends AbstractModule {
  public static final String BASE_CONFIG_PATH = "/etc/singularity.s3downloader";

  public static final String DOWNLOAD_EXECUTOR_SERVICE = "singularity.s3downloader.executor.service";

  @Override
  protected void configure() {
    bind(SingularityDriver.class).to(SingularityS3DownloaderServer.class);
    bind(ArtifactManager.class).toProvider(ArtifactManagerProvider.class);
  }

  @Provides
  @Singleton
  @Named(DOWNLOAD_EXECUTOR_SERVICE)
  public ThreadPoolExecutor getDownloadService(SingularityS3DownloaderConfiguration configuration) {
    return (ThreadPoolExecutor) Executors.newFixedThreadPool(configuration.getNumDownloaderThreads(), new ThreadFactoryBuilder().setDaemon(true).setNameFormat("S3AsyncDownloaderMainThread-%d").build());
  }

  public SingularityS3DownloaderConfiguration providesConfiguration(ObjectMapper objectMapper, Validator validator) throws Exception {
    return SingularityRunnerConfigurationUtils.buildConfiguration(SingularityS3DownloaderConfiguration.class, objectMapper, validator, BASE_CONFIG_PATH);
  }

}
