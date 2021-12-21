package io.vertx.kafka.client.common.resource;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Locale;

public enum ResourceType {

  /**
   * Represents any ResourceType which this client cannot understand,
   * perhaps because this client is too old.
   */
  UNKNOWN((byte) 0),

  /**
   * In a filter, matches any ResourceType.
   */
  ANY((byte) 1),

  /**
   * A Kafka topic.
   */
  TOPIC((byte) 2),

  /**
   * A consumer group.
   */
  GROUP((byte) 3),

  /**
   * The cluster as a whole.
   */
  CLUSTER((byte) 4),

  /**
   * A transactional ID.
   */
  TRANSACTIONAL_ID((byte) 5),

  /**
   * A token ID.
   */
  DELEGATION_TOKEN((byte) 6);

  private final static HashMap<Byte, ResourceType> CODE_TO_VALUE = new HashMap<>();

  static {
    for (ResourceType resourceType : ResourceType.values()) {
      CODE_TO_VALUE.put(resourceType.getCode(), resourceType);
    }
  }

  /**
   * Parse the given string as an ACL resource type.
   *
   * @param str    The string to parse.
   *
   * @return       The ResourceType, or UNKNOWN if the string could not be matched.
   */
  public static ResourceType fromString(String str) {
    try {
      return ResourceType.valueOf(str.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }

  public static ResourceType fromCode(byte code) {
    return CODE_TO_VALUE.getOrDefault(code, UNKNOWN);
  }

  public static ResourceType fromJson(JsonObject json) {
    if(json.containsKey("resourceType")) {
      return ResourceType.fromString(json.getString("resourceType"));
    }
    else {
      return UNKNOWN;
    }
  }

  private final byte code;

  ResourceType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public JsonObject toJson() {
    return new JsonObject().put("resourceType", this);
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }
}
