package io.vertx.kafka.client.common.acl;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Represents an operation which an ACL grants or denies permission to perform.
 *
 * Some operations imply other operations:
 * <ul>
 * <li><code>ALLOW ALL</code> implies <code>ALLOW</code> everything
 * <li><code>DENY ALL</code> implies <code>DENY</code> everything
 *
 * <li><code>ALLOW READ</code> implies <code>ALLOW DESCRIBE</code>
 * <li><code>ALLOW WRITE</code> implies <code>ALLOW DESCRIBE</code>
 * <li><code>ALLOW DELETE</code> implies <code>ALLOW DESCRIBE</code>
 *
 * <li><code>ALLOW ALTER</code> implies <code>ALLOW DESCRIBE</code>
 *
 * <li><code>ALLOW ALTER_CONFIGS</code> implies <code>ALLOW DESCRIBE_CONFIGS</code>
 * </ul>
 * The API for this class is still evolving and we may break compatibility in minor releases, if necessary.
 */
public enum AclOperation {

  /**
   * Represents any AclOperation which this client cannot understand, perhaps because this
   * client is too old.
   */
  UNKNOWN((byte) 0),

  /**
   * In a filter, matches any AclOperation.
   */
  ANY((byte) 1),

  /**
   * ALL operation.
   */
  ALL((byte) 2),

  /**
   * READ operation.
   */
  READ((byte) 3),

  /**
   * WRITE operation.
   */
  WRITE((byte) 4),

  /**
   * CREATE operation.
   */
  CREATE((byte) 5),

  /**
   * DELETE operation.
   */
  DELETE((byte) 6),

  /**
   * ALTER operation.
   */
  ALTER((byte) 7),

  /**
   * DESCRIBE operation.
   */
  DESCRIBE((byte) 8),

  /**
   * CLUSTER_ACTION operation.
   */
  CLUSTER_ACTION((byte) 9),

  /**
   * DESCRIBE_CONFIGS operation.
   */
  DESCRIBE_CONFIGS((byte) 10),

  /**
   * ALTER_CONFIGS operation.
   */
  ALTER_CONFIGS((byte) 11),

  /**
   * IDEMPOTENT_WRITE operation.
   */
  IDEMPOTENT_WRITE((byte) 12);

  private final static HashMap<Byte, AclOperation> CODE_TO_VALUE = new HashMap<>();

  static {
    for(AclOperation aclOperation: AclOperation.values()) {
      CODE_TO_VALUE.put(aclOperation.code, aclOperation);
    }
  }

  /**
   * Parse the given string as an ACL operation.
   *
   * @param str    The string to parse.
   *
   * @return       The AclOperation, or UNKNOWN if the string could not be matched.
   */
  public static AclOperation fromString(String str) {
    try {
      return AclOperation.valueOf(str.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }

  /**
   * Return the AclOperation with the provided code or `AclOperation.UNKNOWN` if one cannot be found.
   */
  public static AclOperation fromCode(byte code) {
    return CODE_TO_VALUE.getOrDefault(code,UNKNOWN);
  }

  public static AclOperation fromJson(JsonObject json) {
    if(json.containsKey("aclOperation")) {
      return AclOperation.fromString(json.getString("aclOperation"));
    } else {
      return UNKNOWN;
    }
  }


  private final byte code;

  AclOperation(byte code) {
    this.code = code;
  }

  public byte getCode() {
    return code;
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  public JsonObject toJson() {
    return new JsonObject().put("aclOperation", this);
  }
}
