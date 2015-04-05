package com.hubspot.singularity.s3uploader;

import com.hubspot.singularity.runner.base.config.SingularityRunnerBaseModule;
import com.hubspot.singularity.runner.base.shared.SingularityRunner;
import com.hubspot.singularity.s3uploader.config.SingularityS3UploaderConfiguration;
import com.hubspot.singularity.s3uploader.config.SingularityS3UploaderModule;

public class SingularityS3UploaderRunner {

  public static void main(String... args) {
    new SingularityS3UploaderRunner().run(args);
  }

  private SingularityS3UploaderRunner() {}

  public void run(String[] args) {
    new SingularityRunner().run(
            new SingularityRunnerBaseModule(),
            new SingularityS3UploaderModule());
  }

}
