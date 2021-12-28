package io.vertx.kafka.client.common;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject
public class TopicPartitionReplica {

  private String topic;
  private int partition;
  private int brokerId;

  public TopicPartitionReplica() {
    topic = "topic_partition_replica";
  }

  public TopicPartitionReplica(String topic, int partition, int brokerId) {
    this.topic = Objects.requireNonNull(topic);
    this.partition = partition;
    this.brokerId = brokerId;
  }

  public TopicPartitionReplica(JsonObject json) {
    if(json.containsKey("topic")) {
      topic = Objects.requireNonNull(json.getString("topic"));
    } else {
      topic = "topic_partition_replica";
    }
    partition = json.getInteger("partition");
    brokerId = json.getInteger("brokerId");
  }

  public int getBrokerId() {
    return brokerId;
  }

  public TopicPartitionReplica setBrokerId(int brokerId) {
    this.brokerId = brokerId;
    return this;
  }

  public int getPartition() {
    return partition;
  }

  public TopicPartitionReplica setPartition(int partition) {
    this.partition = partition;
    return this;
  }

  public String getTopic() {
    return topic;
  }

  public TopicPartitionReplica setTopic(String topic) {
    this.topic = Objects.requireNonNull(topic);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("topic", topic)
      .put("partition", partition)
      .put("brokerId", brokerId);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TopicPartitionReplica that = (TopicPartitionReplica) o;

    if (brokerId != that.brokerId) return false;
    if (partition != that.partition) return false;
    return Objects.equals(topic, that.topic);
  }

  @Override
  public int hashCode() {
    int result = brokerId;
    result = 31 * result + partition;
    result = 31 * result + (topic != null ? topic.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TopicPartitionReplica{" +
      "topic='" + topic + '\'' +
      ", partition=" + partition +
      ", brokerId=" + brokerId +
      '}';
  }
}
