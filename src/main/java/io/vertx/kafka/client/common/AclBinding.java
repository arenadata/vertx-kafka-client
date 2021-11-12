package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;

public class AclBinding {
  private ResourcePattern resourcePattern;
  private AccessControlEntryData entryData;

  public AclBinding() {
  }

  public AclBinding(ResourcePattern resourcePattern, AccessControlEntryData entryData) {
    this.resourcePattern = resourcePattern;
    this.entryData = entryData;
  }

  public AclBinding(JsonObject json) {
    AclBindingConverter.toJson(this, json);
  }

  public ResourcePattern getResourcePattern() {
    return resourcePattern;
  }

  public AclBinding setResourcePattern(ResourcePattern resourcePattern) {
    this.resourcePattern = resourcePattern;
    return this;
  }

  public AccessControlEntryData getEntryData() {
    return entryData;
  }

  public AclBinding setEntryData(AccessControlEntryData entryData) {
    this.entryData = entryData;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    AclBindingConverter.toJson(this, json);
    return json;
  }

  public String toString() {
    return "AclBinding{" +
      "resourcePattern=" + resourcePattern +
      ",entryData=" + entryData +
      "}";
  }

}
