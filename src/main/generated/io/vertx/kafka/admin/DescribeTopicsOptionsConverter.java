package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.DescribeTopicsOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.DescribeTopicsOptions} original class using Vert.x codegen.
 */
public class DescribeTopicsOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DescribeTopicsOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "includeAuthorizedOperations":
          if (member.getValue() instanceof Boolean) {
            obj.setIncludeAuthorizedOperations((Boolean)member.getValue());
          }
          break;
        case "timeoutMs":
          if (member.getValue() instanceof Number) {
            obj.setTimeoutMs(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(DescribeTopicsOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DescribeTopicsOptions obj, java.util.Map<String, Object> json) {
    json.put("includeAuthorizedOperations", obj.isIncludeAuthorizedOperations());
  }
}
