package com.hubspot.singularity.s3downloader.config;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;

import com.hubspot.singularity.runner.base.config.SingularityRunnerConfiguration;

public class SingularityS3DownloaderConfiguration implements SingularityRunnerConfiguration {
  public static final String NUM_DOWNLOADER_THREADS = "s3downloader.downloader.threads";

  public static final String HTTP_SERVER_TIMEOUT = "s3downloader.http.timeout";

  @Min(1)
  private long httpServerTimeout = TimeUnit.MINUTES.toMillis(30);

  @Min(1)
  private int numDownloaderThreads = 25;

  public long getHttpServerTimeout() {
    return httpServerTimeout;
  }

  public void setHttpServerTimeout(long httpServerTimeout) {
    this.httpServerTimeout = httpServerTimeout;
  }

  public int getNumDownloaderThreads() {
    return numDownloaderThreads;
  }

  public void setNumDownloaderThreads(int numDownloaderThreads) {
    this.numDownloaderThreads = numDownloaderThreads;
  }

  @Override
  public String toString() {
    return "SingularityS3DownloaderConfiguration [httpServerTimeout=" + httpServerTimeout + ", numDownloaderThreads=" + numDownloaderThreads + "]";
  }

  @Override
  public void updateFromProperties(Properties properties) {
    if (properties.containsKey(NUM_DOWNLOADER_THREADS)) {
      setNumDownloaderThreads(Integer.parseInt(properties.getProperty(NUM_DOWNLOADER_THREADS)));
    }

    if (properties.containsKey(HTTP_SERVER_TIMEOUT)) {
      setHttpServerTimeout(Long.parseLong(properties.getProperty(HTTP_SERVER_TIMEOUT)));
    }
  }
}
