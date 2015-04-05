package com.hubspot.singularity.s3downloader;

import com.hubspot.singularity.runner.base.config.SingularityRunnerBaseModule;
import com.hubspot.singularity.runner.base.shared.SingularityRunner;
import com.hubspot.singularity.s3downloader.config.SingularityS3DownloaderConfiguration;
import com.hubspot.singularity.s3downloader.config.SingularityS3DownloaderModule;

public class SingularityS3DownloaderRunner {

  public static void main(String... args) {
    new SingularityS3DownloaderRunner().run(args);
  }

  private SingularityS3DownloaderRunner() {}

  public void run(String[] args) {
    new SingularityRunner().run(
            new SingularityRunnerBaseModule(),
            new SingularityS3DownloaderModule());
  }

}
