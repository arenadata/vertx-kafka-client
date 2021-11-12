package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;

import java.util.Map;

public class AccessControlEntryDataConverter {

  public static void fromJson(Iterable<Map.Entry<String, Object>> json, AccessControlEntryData obj) {
    for (Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "principal":
          if (member.getValue() instanceof String) {
            obj.setPrincipal((String)member.getValue());
          }
          break;
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "operation":
          if (member.getValue() instanceof String) {
            obj.setOperation(AclOperation.valueOf((String)member.getValue()));
          }
          break;
        case "permissionType":
          if (member.getValue() instanceof String) {
            obj.setPermissionType(AclPermissionType.valueOf((String)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(AccessControlEntryData obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(AccessControlEntryData obj, Map<String, Object> json) {
    if (obj.getPrincipal() != null) {
      json.put("principal", obj.getPrincipal());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getOperation() != null) {
      json.put("operation", obj.getOperation().name());
    }
    if (obj.getPermissionType() != null) {
      json.put("permissionType", obj.getPermissionType().name());
    }
  }
}
