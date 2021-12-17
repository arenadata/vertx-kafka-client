package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.common.resource.ResourcePattern;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum PatternType {

  /**
   * Represents any PatternType which this client cannot understand, perhaps because this client is too old.
   */
  UNKNOWN((byte) 0),

  /**
   * In a filter, matches any resource pattern type.
   */
  ANY((byte) 1),

  /**
   * In a filter, will perform pattern matching.
   *
   * e.g. Given a filter of {@code ResourcePatternFilter(TOPIC, "payments.received", MATCH)`}, the filter match
   * any {@link ResourcePattern} that matches topic 'payments.received'. This might include:
   * <ul>
   *     <li>A Literal pattern with the same type and name, e.g. {@code ResourcePattern(TOPIC, "payments.received", LITERAL)}</li>
   *     <li>A Wildcard pattern with the same type, e.g. {@code ResourcePattern(TOPIC, "*", LITERAL)}</li>
   *     <li>A Prefixed pattern with the same type and where the name is a matching prefix, e.g. {@code ResourcePattern(TOPIC, "payments.", PREFIXED)}</li>
   * </ul>
   */
  MATCH((byte) 2),

  /**
   * A literal resource name.
   *
   * A literal name defines the full name of a resource, e.g. topic with name 'foo', or group with name 'bob'.
   *
   * The special wildcard character {@code *} can be used to represent a resource with any name.
   */
  LITERAL((byte) 3),

  /**
   * A prefixed resource name.
   *
   * A prefixed name defines a prefix for a resource, e.g. topics with names that start with 'foo'.
   */
  PREFIXED((byte) 4);

  private final static Map<Byte, PatternType> CODE_TO_VALUE = new HashMap<>();

  static {
    for (PatternType patternType : PatternType.values()) {
      CODE_TO_VALUE.put(patternType.getCode(), patternType);
    }
  }

  /**
   * Return the PatternType with the provided code or {@link #UNKNOWN} if one cannot be found.
   */
  public static PatternType fromCode(byte code) {
    return CODE_TO_VALUE.getOrDefault(code, UNKNOWN);
  }
  /**
   * Return the PatternType with the provided name or {@link #UNKNOWN} if one cannot be found.
   */
  public static PatternType fromString(String str) {
    try {
      return PatternType.valueOf(str.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }

  public static PatternType fromJson(JsonObject json) {
    if(json.containsKey("patternType")) {
      return PatternType.fromString(json.getString("patternType"));
    }
    else {
      return UNKNOWN;
    }
  }

  private final byte code;

  PatternType(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public JsonObject toJson() {
    return new JsonObject().put("patternType", this);
  }
}
