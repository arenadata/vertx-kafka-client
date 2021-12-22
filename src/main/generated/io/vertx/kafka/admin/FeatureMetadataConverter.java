package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.FeatureMetadata}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.FeatureMetadata} original class using Vert.x codegen.
 */
public class FeatureMetadataConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, FeatureMetadata obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "finalizedFeatures":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.kafka.admin.FinalizedVersionRange> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                map.put(entry.getKey(), new io.vertx.kafka.admin.FinalizedVersionRange((io.vertx.core.json.JsonObject)entry.getValue()));
            });
            obj.setFinalizedFeatures(map);
          }
          break;
        case "finalizedFeaturesEpoch":
          if (member.getValue() instanceof Number) {
            obj.setFinalizedFeaturesEpoch(((Number)member.getValue()).longValue());
          }
          break;
        case "supportedFeatures":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.kafka.admin.SupportedVersionRange> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                map.put(entry.getKey(), new io.vertx.kafka.admin.SupportedVersionRange((io.vertx.core.json.JsonObject)entry.getValue()));
            });
            obj.setSupportedFeatures(map);
          }
          break;
      }
    }
  }

  public static void toJson(FeatureMetadata obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(FeatureMetadata obj, java.util.Map<String, Object> json) {
    if (obj.getFinalizedFeatures() != null) {
      JsonObject map = new JsonObject();
      obj.getFinalizedFeatures().forEach((key, value) -> map.put(key, value.toJson()));
      json.put("finalizedFeatures", map);
    }
    json.put("finalizedFeaturesEpoch", obj.getFinalizedFeaturesEpoch());
    if (obj.getSupportedFeatures() != null) {
      JsonObject map = new JsonObject();
      obj.getSupportedFeatures().forEach((key, value) -> map.put(key, value.toJson()));
      json.put("supportedFeatures", map);
    }
  }
}
