package io.vertx.kafka.client.common;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject
public class ResourcePattern {

  /**
   * A special literal resource name that corresponds to 'all resources of a certain type'.
   */
  public static final String WILDCARD_RESOURCE = "*";

  private ResourceType resourceType;
  private String name;
  private PatternType patternType;

  public ResourcePattern() {
    resourceType = ResourceType.UNKNOWN;
    name = "UNKNOWN";
    patternType = PatternType.UNKNOWN;
  }

  /**
   * Create a pattern using the supplied parameters.
   *
   * @param resourceType non-null, specific, resource type
   * @param name non-null resource name, which can be the {@link #WILDCARD_RESOURCE}.
   * @param patternType non-null, specific, resource pattern type, which controls how the pattern will match resource names.
   */
  public ResourcePattern(ResourceType resourceType, String name, PatternType patternType) {
    this.resourceType = Objects.requireNonNull(resourceType, "resourceType");
    this.name = Objects.requireNonNull(name, "name");
    this.patternType = Objects.requireNonNull(patternType, "patternType");
    checkResourceType(resourceType);
    checkPatternType(patternType);
  }

  public ResourcePattern(JsonObject json) {
    if(json.containsKey("resourceType")) {
      resourceType = ResourceType.fromString(json.getString("resourceType"));
      checkResourceType(resourceType);
    } else {
      resourceType = ResourceType.UNKNOWN;
    }
    if(json.containsKey("name")) {
      name = Objects.requireNonNull(json.getString("name"));
    } else {
      name = "UNKNOWN";
    }
    if(json.containsKey("patternType")) {
      patternType = PatternType.fromString(json.getString("patternType"));
      checkPatternType(patternType);
    } else {
      patternType = PatternType.UNKNOWN;
    }
  }

  private void checkPatternType(PatternType patternType) {
    if (patternType == PatternType.MATCH || patternType == PatternType.ANY) {
      throw new IllegalArgumentException("patternType must not be " + patternType);
    }
  }

  private void checkResourceType(ResourceType resourceType) {
    if (resourceType == ResourceType.ANY) {
      throw new IllegalArgumentException("resourceType must not be ANY");
    }
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public ResourcePattern setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  public String getName() {
    return name;
  }

  public ResourcePattern setName(String name) {
    this.name = name;
    return this;
  }

  public PatternType getPatternType() {
    return patternType;
  }

  public ResourcePattern setPatternType(PatternType patternType) {
    this.patternType = patternType;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("resourceType", resourceType)
      .put("name", name)
      .put("patternType", patternType);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ResourcePattern that = (ResourcePattern) o;

    if (resourceType != that.resourceType) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    return patternType == that.patternType;
  }

  @Override
  public int hashCode() {
    int result = resourceType != null ? resourceType.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (patternType != null ? patternType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ResourcePattern{" +
      "resourceType=" + resourceType +
      ", name='" + name + '\'' +
      ", patternType=" + patternType +
      '}';
  }
}
