package io.vertx.kafka.client.common.acl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.resource.ResourcePattern;

import java.util.Objects;

@DataObject(generateConverter = true)
public class AclBinding {
  private ResourcePattern pattern;
  private AccessControlEntry entry;

  public AclBinding() {
    pattern = new ResourcePattern();
    entry = new AccessControlEntry();
  }

  public AclBinding(ResourcePattern pattern, AccessControlEntry entry) {
    this.pattern = Objects.requireNonNull(pattern, "pattern");
    this.entry = Objects.requireNonNull(entry, "entry");
  }

  public AclBinding(JsonObject json) {
    AclBindingConverter.fromJson(json, this);
  }

  public ResourcePattern getPattern() {
    return pattern;
  }

  public AclBinding setPattern(ResourcePattern pattern) {
    this.pattern = Objects.requireNonNull(pattern);
    return this;
  }

  public AccessControlEntry getEntry() {
    return entry;
  }

  public AclBinding setEntry(AccessControlEntry entry) {
    this.entry = Objects.requireNonNull(entry);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    AclBindingConverter.toJson(this, json);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AclBinding that = (AclBinding) o;

    if (!Objects.equals(pattern, that.pattern)) return false;
    return Objects.equals(entry, that.entry);
  }

  @Override
  public int hashCode() {
    int result = pattern != null ? pattern.hashCode() : 0;
    result = 31 * result + (entry != null ? entry.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AclBinding{" +
      "pattern=" + pattern +
      ", entry=" + entry +
      '}';
  }
}
