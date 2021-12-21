package io.vertx.kafka.client.common.acl;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.client.common.acl.AclBindingFilter}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.client.common.acl.AclBindingFilter} original class using Vert.x codegen.
 */
public class AclBindingFilterConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, AclBindingFilter obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "entryFilter":
          if (member.getValue() instanceof JsonObject) {
            obj.setEntryFilter(new io.vertx.kafka.client.common.acl.AccessControlEntryFilter((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "patternFilter":
          if (member.getValue() instanceof JsonObject) {
            obj.setPatternFilter(new io.vertx.kafka.client.common.resource.ResourcePatternFilter((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(AclBindingFilter obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(AclBindingFilter obj, java.util.Map<String, Object> json) {
    if (obj.getEntryFilter() != null) {
      json.put("entryFilter", obj.getEntryFilter().toJson());
    }
    if (obj.getPatternFilter() != null) {
      json.put("patternFilter", obj.getPatternFilter().toJson());
    }
  }
}
