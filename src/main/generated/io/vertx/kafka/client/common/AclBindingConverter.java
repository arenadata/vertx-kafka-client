package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;

import java.util.Map;

public class AclBindingConverter {

  public static void fromJson(Iterable<Map.Entry<String, Object>> json, AclBinding obj) {
    for (Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "resourcePattern":
          if (member.getValue() instanceof JsonObject) {
            obj.setResourcePattern(new ResourcePattern((JsonObject) member.getValue()));
          }
          break;
        case "entryData":
          if (member.getValue() instanceof JsonObject) {
            obj.setEntryData(new AccessControlEntryData((JsonObject) member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(AclBinding obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(AclBinding obj, Map<String, Object> json) {
    if (obj.getResourcePattern() != null) {
      json.put("resourcePattern", obj.getResourcePattern().toJson());
    }
    if (obj.getEntryData() != null) {
      json.put("entryData", obj.getEntryData().toJson());
    }
  }
}
