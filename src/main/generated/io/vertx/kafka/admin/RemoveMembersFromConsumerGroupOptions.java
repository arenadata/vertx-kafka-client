package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.admin.MemberToRemove;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RemoveMembersFromConsumerGroupOptions {
  private Set<MemberToRemove> members;

  public RemoveMembersFromConsumerGroupOptions(Collection<MemberToRemove> members) {
    if (members.isEmpty()) {
      throw new IllegalArgumentException("Invalid empty members has been provided");
    } else {
      this.members = new HashSet(members);
    }
  }

  public RemoveMembersFromConsumerGroupOptions() {
    this.members = Collections.emptySet();
  }

  public Set<MemberToRemove> members() {
    return this.members;
  }

  public boolean removeAll() {
    return this.members.isEmpty();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    RemoveMembersFromConsumerGroupOptionsConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "RemoveMembersFromConsumerGroupOptions{" +
      "members=" + members +
      '}';
  }
}
