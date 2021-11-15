package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourcePattern {
  private ResourceType resourceType;
  private String name;
  private PatternType patternType;

  public ResourcePattern(JsonObject json) {
    ResourcePatternConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ResourcePatternConverter.toJson(this, json);
    return json;
  }

  public ResourcePattern setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  public ResourcePattern setName(String name) {
    this.name = name;
    return this;
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
      ",name=" + this.name +
      ",patternType=" + this.patternType +
      "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResourcePattern that = (ResourcePattern) o;
    return (
      Objects.equals(resourceType, that.resourceType) &&
        Objects.equals(name, that.name) &&
        Objects.equals(patternType, that.patternType)
    );
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (patternType != null ? patternType.hashCode() : 0);
    return result;
  }

}
