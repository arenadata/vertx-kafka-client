package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.*;

@DataObject(generateConverter = true)
public class NewPartitionReassignment {

  private List<Integer> targetReplicas;

  public NewPartitionReassignment() {
    targetReplicas = Collections.singletonList(0);
  }

  public NewPartitionReassignment(List<Integer> targetReplicas) {
    checkPartitionReassignment(targetReplicas);
    this.targetReplicas = targetReplicas;
  }

  public NewPartitionReassignment(JsonObject json) {
    NewPartitionReassignmentConverter.fromJson(json, this);
    checkPartitionReassignment(this.targetReplicas);
  }

  public List<Integer> getTargetReplicas() {
    return targetReplicas;
  }

  public NewPartitionReassignment setTargetReplicas(List<Integer> targetReplicas) {
    checkPartitionReassignment(targetReplicas);
    this.targetReplicas = targetReplicas;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    NewPartitionReassignmentConverter.toJson(this, json);
    return json;
  }

  private void checkPartitionReassignment(List<Integer> targetReplicas) {
    if (targetReplicas == null || targetReplicas.size() == 0)
      throw new IllegalArgumentException("Cannot create a new partition reassignment without any replicas");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewPartitionReassignment that = (NewPartitionReassignment) o;

    return Objects.equals(targetReplicas, that.targetReplicas);
  }

  @Override
  public int hashCode() {
    return targetReplicas != null ? targetReplicas.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "NewPartitionReassignment{" +
      "targetReplicas=" + targetReplicas +
      '}';
  }
}
