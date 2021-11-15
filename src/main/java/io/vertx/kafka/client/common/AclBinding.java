package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AclBinding {
  private ResourcePattern resourcePattern;
  private AccessControlEntryData entryData;

  public AclBinding(JsonObject json) {
    AclBindingConverter.toJson(this, json);
  }

  public AclBinding setResourcePattern(ResourcePattern resourcePattern) {
    this.resourcePattern = resourcePattern;
    return this;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AclBinding that = (AclBinding) o;
    return (
      Objects.equals(resourcePattern, that.resourcePattern) &&
        Objects.equals(entryData, that.entryData)
    );
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (resourcePattern != null ? resourcePattern.hashCode() : 0);
    result = 31 * result + (entryData != null ? entryData.hashCode() : 0);
    return result;
  }

}
