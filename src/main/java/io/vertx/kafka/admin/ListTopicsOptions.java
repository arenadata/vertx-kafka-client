package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class ListTopicsOptions {

  private boolean listInternal = false;

  public ListTopicsOptions() {
  }
  public ListTopicsOptions(boolean listInternal) {
    this.listInternal = listInternal;
  }

  public ListTopicsOptions(JsonObject json) {
    ListTopicsOptionsConverter.fromJson(json, this);
  }

  public boolean isListInternal() {
    return listInternal;
  }

  public ListTopicsOptions setListInternal(boolean listInternal) {
    this.listInternal = listInternal;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ListTopicsOptionsConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "ListTopicsOptions{" +
      "listInternal=" + listInternal +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ListTopicsOptions that = (ListTopicsOptions) o;

      return listInternal == that.listInternal;
  }

  @Override
  public int hashCode() {
    return (listInternal ? 1 : 0);
  }
}
