package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.PartitionReassignment}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.PartitionReassignment} original class using Vert.x codegen.
 */
public class PartitionReassignmentConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PartitionReassignment obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "addingReplicas":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Integer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).intValue());
            });
            obj.setAddingReplicas(list);
          }
          break;
        case "removingReplicas":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Integer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).intValue());
            });
            obj.setRemovingReplicas(list);
          }
          break;
        case "replicas":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Integer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).intValue());
            });
            obj.setReplicas(list);
          }
          break;
      }
    }
  }

  public static void toJson(PartitionReassignment obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PartitionReassignment obj, java.util.Map<String, Object> json) {
    if (obj.getAddingReplicas() != null) {
      JsonArray array = new JsonArray();
      obj.getAddingReplicas().forEach(item -> array.add(item));
      json.put("addingReplicas", array);
    }
    if (obj.getRemovingReplicas() != null) {
      JsonArray array = new JsonArray();
      obj.getRemovingReplicas().forEach(item -> array.add(item));
      json.put("removingReplicas", array);
    }
    if (obj.getReplicas() != null) {
      JsonArray array = new JsonArray();
      obj.getReplicas().forEach(item -> array.add(item));
      json.put("replicas", array);
    }
  }
}
