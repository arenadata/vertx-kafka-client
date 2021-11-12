package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;

public class ResourcePattern {
  private ResourceType resourceType;
  private String name;
  private PatternType patternType;

  public ResourcePattern() {
  }

  public ResourcePattern(ResourceType resourceType, String name, PatternType patternType) {
    this.resourceType = resourceType;
    this.name = name;
    this.patternType = patternType;
  }

  public ResourcePattern(JsonObject json) {
    ResourcePatternConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ResourcePatternConverter.toJson(this, json);
    return json;
  }

  public ResourceType getResourceType() {
    return this.resourceType;
  }

  public ResourcePattern setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public ResourcePattern setName(String name) {
    this.name = name;
    return this;
  }

  public PatternType getPatternType() {
    return this.patternType;
  }

  public ResourcePattern setPatternType(PatternType patternType) {
    this.patternType = patternType;
    return this;
  }

  public boolean isUnknown() {
    return this.resourceType.isUnknown() || this.patternType.isUnknown();
  }

  public String toString() {
    return "ResourcePattern{" +
      "resourceType=" + this.resourceType +
      ", name=" + this.name +
      ", patternType=" + this.patternType +
      "}";
  }

}
