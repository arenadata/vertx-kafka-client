package io.vertx.kafka.admin;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.LinkedHashSet;
import java.util.Map;

public class RemoveMembersFromConsumerGroupOptionsConverter {
  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RemoveMembersFromConsumerGroupOptions obj) {
    for (Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "members":
          if (member.getValue() instanceof JsonArray) {
            LinkedHashSet<MemberToRemove> list = new java.util.LinkedHashSet<>();
            ((Iterable<Object>) member.getValue()).forEach(item -> {
              if (item instanceof JsonObject)
                list.add(new MemberToRemove((JsonObject) item));
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
      json.put("members", obj.getMembers());
    }
  }
}
