package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.ClientQuotaFilter}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.ClientQuotaFilter} original class using Vert.x codegen.
 */
public class ClientQuotaFilterConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ClientQuotaFilter obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "components":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.kafka.admin.ClientQuotaFilterComponent> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.kafka.admin.ClientQuotaFilterComponent((io.vertx.core.json.JsonObject)item));
            });
            obj.setComponents(list);
          }
          break;
        case "strict":
          if (member.getValue() instanceof Boolean) {
            obj.setStrict((Boolean)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ClientQuotaFilter obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ClientQuotaFilter obj, java.util.Map<String, Object> json) {
    if (obj.getComponents() != null) {
      JsonArray array = new JsonArray();
      obj.getComponents().forEach(item -> array.add(item.toJson()));
      json.put("components", array);
    }
    json.put("strict", obj.isStrict());
  }
}
