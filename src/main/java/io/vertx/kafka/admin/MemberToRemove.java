package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject
public class MemberToRemove {
  private String groupInstanceId;

  public MemberToRemove() {
  }

  public MemberToRemove(String groupInstanceId) {
    this.groupInstanceId = groupInstanceId;
  }

  public MemberToRemove(JsonObject json) {
    this.groupInstanceId = json.getString("groupInstanceId");
  }

  public String getGroupInstanceId() {
    return groupInstanceId;
  }

  public MemberToRemove setGroupInstanceId(String groupInstanceId) {
    this.groupInstanceId = groupInstanceId;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("groupInstanceId", this.groupInstanceId);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MemberToRemove that = (MemberToRemove) o;

    return Objects.equals(groupInstanceId, that.groupInstanceId);
  }

  @Override
  public int hashCode() {
    return groupInstanceId != null ? groupInstanceId.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MemberToRemove{" +
      "groupInstanceId='" + groupInstanceId + '\'' +
      '}';
  }
}
