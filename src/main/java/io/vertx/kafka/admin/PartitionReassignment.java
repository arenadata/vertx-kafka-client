package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@DataObject(generateConverter = true)
public class PartitionReassignment {

  private List<Integer> replicas;
  private List<Integer> addingReplicas;
  private List<Integer> removingReplicas;

  public PartitionReassignment() {
    this.replicas = new ArrayList<>();
    this.addingReplicas = new ArrayList<>();
    this.removingReplicas = new ArrayList<>();
  }

  public PartitionReassignment(List<Integer> replicas, List<Integer> addingReplicas, List<Integer> removingReplicas) {
    this.replicas = replicas;
    this.addingReplicas = addingReplicas;
    this.removingReplicas = removingReplicas;
  }

  public PartitionReassignment(JsonObject json) {
    PartitionReassignmentConverter.fromJson(json, this);
    if(Objects.isNull(replicas)) {
      replicas = new ArrayList<>();
    }
    if(Objects.isNull(addingReplicas)) {
      addingReplicas = new ArrayList<>();
    }
    if(Objects.isNull(removingReplicas)) {
      removingReplicas = new ArrayList<>();
    }
  }

  /**
   * The brokers which this partition currently resides on.
   */
  public List<Integer> getReplicas() {
    return replicas;
  }

  /**
   * The brokers that we are adding this partition to as part of a reassignment.
   * A subset of replicas.
   */
  public List<Integer> getAddingReplicas() {
    return addingReplicas;
  }

  /**
   * The brokers that we are removing this partition from as part of a reassignment.
   * A subset of replicas.
   */
  public List<Integer> getRemovingReplicas() {
    return removingReplicas;
  }

  public PartitionReassignment setReplicas(List<Integer> replicas) {
    this.replicas = replicas;
    return this;
  }

  public PartitionReassignment setAddingReplicas(List<Integer> addingReplicas) {
    this.addingReplicas = addingReplicas;
    return this;
  }

  public PartitionReassignment setRemovingReplicas(List<Integer> removingReplicas) {
    this.removingReplicas = removingReplicas;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PartitionReassignmentConverter.toJson(this, json);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PartitionReassignment that = (PartitionReassignment) o;

    if (!Objects.equals(replicas, that.replicas)) return false;
    if (!Objects.equals(addingReplicas, that.addingReplicas))
      return false;
    return Objects.equals(removingReplicas, that.removingReplicas);
  }

  @Override
  public int hashCode() {
    int result = replicas != null ? replicas.hashCode() : 0;
    result = 31 * result + (addingReplicas != null ? addingReplicas.hashCode() : 0);
    result = 31 * result + (removingReplicas != null ? removingReplicas.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PartitionReassignment{" +
      "replicas=" + replicas +
      ", addingReplicas=" + addingReplicas +
      ", removingReplicas=" + removingReplicas +
      '}';
  }
}
