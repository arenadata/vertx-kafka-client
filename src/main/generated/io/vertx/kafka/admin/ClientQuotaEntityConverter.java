package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.ClientQuotaEntity}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.ClientQuotaEntity} original class using Vert.x codegen.
 */
public class ClientQuotaEntityConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ClientQuotaEntity obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "entries":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.String> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                map.put(entry.getKey(), (String)entry.getValue());
            });
            obj.setEntries(map);
          }
          break;
      }
    }
  }

  public static void toJson(ClientQuotaEntity obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ClientQuotaEntity obj, java.util.Map<String, Object> json) {
    if (obj.getEntries() != null) {
      JsonObject map = new JsonObject();
      obj.getEntries().forEach((key, value) -> map.put(key, value));
      json.put("entries", map);
    }
  }
}
