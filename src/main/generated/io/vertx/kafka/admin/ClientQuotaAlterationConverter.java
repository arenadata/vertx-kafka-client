package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.ClientQuotaAlteration}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.ClientQuotaAlteration} original class using Vert.x codegen.
 */
public class ClientQuotaAlterationConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ClientQuotaAlteration obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "entity":
          if (member.getValue() instanceof JsonObject) {
            obj.setEntity(new io.vertx.kafka.admin.ClientQuotaEntity((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "ops":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.kafka.admin.QuotaAlterationOperation> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.kafka.admin.QuotaAlterationOperation((io.vertx.core.json.JsonObject)item));
            });
            obj.setOps(list);
          }
          break;
      }
    }
  }

  public static void toJson(ClientQuotaAlteration obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ClientQuotaAlteration obj, java.util.Map<String, Object> json) {
    if (obj.getEntity() != null) {
      json.put("entity", obj.getEntity().toJson());
    }
    if (obj.getOps() != null) {
      JsonArray array = new JsonArray();
      obj.getOps().forEach(item -> array.add(item.toJson()));
      json.put("ops", array);
    }
  }
}
