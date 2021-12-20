package io.vertx.kafka.client.common.acl;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.client.common.acl.AclBinding}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.client.common.acl.AclBinding} original class using Vert.x codegen.
 */
public class AclBindingConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, AclBinding obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "entry":
          if (member.getValue() instanceof JsonObject) {
            obj.setEntry(new io.vertx.kafka.client.common.acl.AccessControlEntry((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "pattern":
          if (member.getValue() instanceof JsonObject) {
            obj.setPattern(new io.vertx.kafka.client.common.resource.ResourcePattern((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(AclBinding obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(AclBinding obj, java.util.Map<String, Object> json) {
    if (obj.getEntry() != null) {
      json.put("entry", obj.getEntry().toJson());
    }
    if (obj.getPattern() != null) {
      json.put("pattern", obj.getPattern().toJson());
    }
  }
}
