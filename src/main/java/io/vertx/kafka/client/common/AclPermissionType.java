package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Locale;

public enum AclPermissionType {

  /**
   * Represents any AclPermissionType which this client cannot understand,
   * perhaps because this client is too old.
   */
  UNKNOWN((byte) 0),

  /**
   * In a filter, matches any AclPermissionType.
   */
  ANY((byte) 1),

  /**
   * Disallows access.
   */
  DENY((byte) 2),

  /**
   * Grants access.
   */
  ALLOW((byte) 3);

  private final static HashMap<Byte, AclPermissionType> CODE_TO_VALUE = new HashMap<>();

  static {
    for(AclPermissionType permissionType: AclPermissionType.values()) {
      CODE_TO_VALUE.put(permissionType.code, permissionType);
    }
  }


  /**
   * Parse the given string as an ACL permission.
   *
   * @param str    The string to parse.
   *
   * @return       The AclPermissionType, or UNKNOWN if the string could not be matched.
   */
  public static AclPermissionType fromString(String str) {
    try {
      return AclPermissionType.valueOf(str.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }

  /**
   * Return the AclPermissionType with the provided code or `AclPermissionType.UNKNOWN` if one cannot be found.
   */
  public static AclPermissionType fromCode(byte code) {
    return CODE_TO_VALUE.getOrDefault(code, UNKNOWN);
  }

  public static AclPermissionType fromJson(JsonObject json) {
    if(json.containsKey("aclPermissionType")) {
      return AclPermissionType.fromString(json.getString("aclPermissionType"));
    }
    else {
      return UNKNOWN;
    }
  }

  private final byte code;

  AclPermissionType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public JsonObject toJson() {
    return new JsonObject().put("aclPermissionType", this);
  }
}
