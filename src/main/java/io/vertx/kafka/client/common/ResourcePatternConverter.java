package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;

import java.util.Map;

public class ResourcePatternConverter {

  public static void fromJson(Iterable<Map.Entry<String, Object>> json, ResourcePattern obj) {
    for (Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "resourceType":
          if (member.getValue() instanceof JsonObject) {
            obj.setResourceType(ResourceType.valueOf((String) member.getValue()));
          }
          break;
        case "patternType":
          if (member.getValue() instanceof JsonObject) {
            obj.setPatternType(PatternType.valueOf((String) member.getValue()));
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String) member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ResourcePattern obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ResourcePattern obj, Map<String, Object> json) {
    if (obj.getResourceType() != null) {
      json.put("resourceType", obj.getResourceType().name());
    }
    if (obj.getPatternType() != null) {
      json.put("patternType", obj.getPatternType().name());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }

}
