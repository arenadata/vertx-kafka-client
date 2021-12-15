package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public enum ElectionType {
  PREFERRED((byte) 0),
  UNCLEAN((byte) 1);

  public final byte value;

  ElectionType(byte value) {
    this.value = value;
  }

  public static ElectionType valueOf(byte value) {
    if (value == PREFERRED.value) {
      return PREFERRED;
    } else if (value == UNCLEAN.value) {
      return UNCLEAN;
    } else {
      throw new IllegalArgumentException(String.format("Value %s must be one of %s", value, Arrays.asList(values())));
    }
  }

  public static ElectionType valueOf(JsonObject json) {
    return ElectionType.valueOf(json.getString("electionType"));
  }

  public JsonObject toJson() {
    return new JsonObject().put("electionType", ElectionType.valueOf(this.value));
  }
}
