package com.hubspot.singularity;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class SingularityDeployWebhook extends SingularityJsonObject {

  public enum DeployEventType {
    STARTING, FINISHED;
  }

  private final SingularityDeployMarker deployMarker;
  private final Optional<SingularityDeploy> deploy;
  private final DeployEventType eventType;
  private final Optional<SingularityDeployResult> deployResult;

  public static SingularityDeployWebhook fromBytes(byte[] bytes, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(bytes, SingularityDeployWebhook.class);
    } catch (IOException e) {
      throw new SingularityJsonException(e);
    }
  }

  @JsonCreator
  public SingularityDeployWebhook(@JsonProperty("deployMarker") SingularityDeployMarker deployMarker, @JsonProperty("deploy") Optional<SingularityDeploy> deploy, @JsonProperty("eventType") DeployEventType eventType, @JsonProperty("deployResult") Optional<SingularityDeployResult> deployResult) {
    this.deployMarker = deployMarker;
    this.deploy = deploy;
    this.eventType = eventType;
    this.deployResult = deployResult;
  }

  public SingularityDeployMarker getDeployMarker() {
    return deployMarker;
  }

  public Optional<SingularityDeploy> getDeploy() {
    return deploy;
  }

  public DeployEventType getEventType() {
    return eventType;
  }

  public Optional<SingularityDeployResult> getDeployResult() {
    return deployResult;
  }

}
