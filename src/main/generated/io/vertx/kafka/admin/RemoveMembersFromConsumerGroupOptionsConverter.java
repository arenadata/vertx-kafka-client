package io.vertx.kafka.admin;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RemoveMembersFromConsumerGroupOptionsConverter {
  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RemoveMembersFromConsumerGroupOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "members":
          if (member.getValue() instanceof JsonArray) {
            java.util.LinkedHashSet<io.vertx.kafka.client.common.TopicPartition> list =  new java.util.LinkedHashSet<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.kafka.client.common.TopicPartition((io.vertx.core.json.JsonObject)item));
            });
            obj.setTopicPartitions(list);
          }
          break;
      }
    }
  }

  public static void toJson(ListConsumerGroupOffsetsOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ListConsumerGroupOffsetsOptions obj, java.util.Map<String, Object> json) {
  }
}
