package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.NewPartitionReassignment}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.NewPartitionReassignment} original class using Vert.x codegen.
 */
public class NewPartitionReassignmentConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, NewPartitionReassignment obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "targetReplicas":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Integer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).intValue());
            });
            obj.setTargetReplicas(list);
          }
          break;
      }
    }
  }

  public static void toJson(NewPartitionReassignment obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(NewPartitionReassignment obj, java.util.Map<String, Object> json) {
    if (obj.getTargetReplicas() != null) {
      JsonArray array = new JsonArray();
      obj.getTargetReplicas().forEach(item -> array.add(item));
      json.put("targetReplicas", array);
    }
  }
}
