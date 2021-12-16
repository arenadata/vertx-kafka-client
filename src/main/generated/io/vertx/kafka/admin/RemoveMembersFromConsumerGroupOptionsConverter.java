package io.vertx.kafka.admin;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptions} original class using Vert.x codegen.
 */
public class RemoveMembersFromConsumerGroupOptionsConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RemoveMembersFromConsumerGroupOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "members":
          if (member.getValue() instanceof JsonArray) {
            java.util.LinkedHashSet<io.vertx.kafka.admin.MemberToRemove> list =  new java.util.LinkedHashSet<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.kafka.admin.MemberToRemove((io.vertx.core.json.JsonObject)item));
            });
            obj.setMembers(list);
          }
          break;
      }
    }
  }

  public static void toJson(RemoveMembersFromConsumerGroupOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(RemoveMembersFromConsumerGroupOptions obj, java.util.Map<String, Object> json) {
    if (obj.getMembers() != null) {
      JsonArray array = new JsonArray();
      obj.getMembers().forEach(item -> array.add(item.toJson()));
      json.put("members", array);
    }
  }
}
