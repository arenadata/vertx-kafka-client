package io.vertx.kafka.client.common.resource;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

import static io.vertx.kafka.client.common.resource.ResourcePattern.WILDCARD_RESOURCE;

@DataObject
public class ResourcePatternFilter {

  public static final ResourcePatternFilter ANY = new ResourcePatternFilter(ResourceType.ANY, null, PatternType.ANY);


  private ResourceType resourceType;
  private String name;
  private PatternType patternType;

  public ResourcePatternFilter() {
    resourceType = ResourceType.UNKNOWN;
    patternType = PatternType.UNKNOWN;
  }

  /**
   * Create a filter using the supplied parameters.
   *
   * @param resourceType non-null resource type.
   *                     If {@link ResourceType#ANY}, the filter will ignore the resource type of the pattern.
   *                     If any other resource type, the filter will match only patterns with the same type.
   * @param name         resource name or {@code null}.
   *                     If {@code null}, the filter will ignore the name of resources.
   *                     If {@link ResourcePattern#WILDCARD_RESOURCE}, will match only wildcard patterns.
   * @param patternType  non-null resource pattern type.
   *                     If {@link PatternType#ANY}, the filter will match patterns regardless of pattern type.
   *                     If {@link PatternType#MATCH}, the filter will match patterns that would match the supplied
   *                     {@code name}, including a matching prefixed and wildcards patterns.
   *                     If any other resource pattern type, the filter will match only patterns with the same type.
   */
  public ResourcePatternFilter(ResourceType resourceType, String name, PatternType patternType) {
    this.resourceType = Objects.requireNonNull(resourceType);
    this.name = name;
    this.patternType = Objects.requireNonNull(patternType);
  }

  public ResourcePatternFilter(JsonObject json) {
    resourceType = Objects.requireNonNull(ResourceType.fromString(json.getString("resourceType")));
    name = json.getString("name");
    patternType = Objects.requireNonNull(PatternType.fromString(json.getString("patternType")));
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public ResourcePatternFilter setResourceType(ResourceType resourceType) {
    this.resourceType = Objects.requireNonNull(resourceType);
    return this;
  }

  public String getName() {
    return name;
  }

  public ResourcePatternFilter setName(String name) {
    this.name = name;
    return this;
  }

  public PatternType getPatternType() {
    return patternType;
  }

  public ResourcePatternFilter setPatternType(PatternType patternType) {
    this.patternType = Objects.requireNonNull(patternType);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("resourceType", resourceType)
      .put("patternType", patternType);
    if (Objects.nonNull(name)) {
      json.put("name", name);
    }
    return json;
  }

  /**
   * @return {@code true} if this filter has any UNKNOWN components.
   */
  public boolean isUnknown() {
    return resourceType.isUnknown() || patternType.isUnknown();
  }

  /**
   * @return {@code true} if this filter matches the given pattern.
   */
  public boolean matches(ResourcePattern pattern) {
    if (!resourceType.equals(ResourceType.ANY) && !resourceType.equals(pattern.getResourceType())) {
      return false;
    }

    if (!patternType.equals(PatternType.ANY) && !patternType.equals(PatternType.MATCH) && !patternType.equals(pattern.getPatternType())) {
      return false;
    }

    if (name == null) {
      return true;
    }

    if (patternType.equals(PatternType.ANY) || patternType.equals(pattern.getPatternType())) {
      return name.equals(pattern.getName());
    }

    switch (pattern.getPatternType()) {
      case LITERAL:
        return name.equals(pattern.getName()) || pattern.getName().equals(WILDCARD_RESOURCE);

      case PREFIXED:
        return name.startsWith(pattern.getName());

      default:
        throw new IllegalArgumentException("Unsupported PatternType: " + pattern.getPatternType());
    }
  }


  /**
   * @return {@code true} if this filter could only match one pattern.
   * In other words, if there are no ANY or UNKNOWN fields.
   */
  public boolean matchesAtMostOne() {
    return findIndefiniteField() == null;
  }

  /**
   * @return a string describing any ANY or UNKNOWN field, or null if there is no such field.
   */
  public String findIndefiniteField() {
    if (resourceType == ResourceType.ANY)
      return "Resource type is ANY.";
    if (resourceType == ResourceType.UNKNOWN)
      return "Resource type is UNKNOWN.";
    if (name == null)
      return "Resource name is NULL.";
    if (patternType == PatternType.MATCH)
      return "Resource pattern type is MATCH.";
    if (patternType == PatternType.LITERAL)
      return "Resource pattern type is UNKNOWN.";
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ResourcePatternFilter that = (ResourcePatternFilter) o;

    if (resourceType != that.resourceType) return false;
    if (!Objects.equals(name, that.name)) return false;
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
    return "ResourcePatternFilter{" +
      "resourceType=" + resourceType +
      ", name='" + name + '\'' +
      ", patternType=" + patternType +
      '}';
  }
}
