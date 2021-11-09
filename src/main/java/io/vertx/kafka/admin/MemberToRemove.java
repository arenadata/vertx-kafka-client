package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
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

  public MemberToRemove setGroupInstanceId(String groupInstanceId) {
    this.groupInstanceId = groupInstanceId;
    return this;
  }

  public JsonObject toJson() {
    return new JsonObject().put("groupInstanceId", this.groupInstanceId);
  }

  @Override
  public String toString() {
    return "MemberToRemove{" +
      "groupInstanceId=" + this.groupInstanceId +
      "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MemberToRemove) {
      MemberToRemove otherMember = (MemberToRemove)o;
      return this.groupInstanceId.equals(otherMember.groupInstanceId);
    } else {
      return false;
    }
  }
}
