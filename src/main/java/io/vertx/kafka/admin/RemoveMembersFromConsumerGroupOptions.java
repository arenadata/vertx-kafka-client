package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@DataObject(generateConverter = true)
public class RemoveMembersFromConsumerGroupOptions {

  Set<MemberToRemove> members;

  public RemoveMembersFromConsumerGroupOptions() {
    this.members = Collections.emptySet();
  }

  public RemoveMembersFromConsumerGroupOptions(Set<MemberToRemove> members) {
    if (members.isEmpty()) {
      throw new IllegalArgumentException("Invalid empty members has been provided");
    } else {
      this.members = new HashSet<>(members);
    }
  }

  public RemoveMembersFromConsumerGroupOptions(JsonObject json) {
    if(!json.containsKey("members")) {
      throw new IllegalArgumentException("Invalid empty members has been provided");
    } else {
      RemoveMembersFromConsumerGroupOptionsConverter.fromJson(json, this);
    }
  }

  public Set<MemberToRemove> getMembers() {
    return members;
  }

  public RemoveMembersFromConsumerGroupOptions setMembers(Set<MemberToRemove> members) {
    if (members.isEmpty()) {
      throw new IllegalArgumentException("Invalid empty members has been provided");
    } else {
      this.members = members;
      return this;
    }
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    RemoveMembersFromConsumerGroupOptionsConverter.toJson(this, json);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RemoveMembersFromConsumerGroupOptions that = (RemoveMembersFromConsumerGroupOptions) o;

    return Objects.equals(members, that.members);
  }

  @Override
  public int hashCode() {
    return members != null ? members.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "RemoveMembersFromConsumerGroupOptions{" +
      "members=" + members +
      '}';
  }
}
