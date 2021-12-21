package io.vertx.kafka.client.common.acl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.Objects;

@DataObject
public class AccessControlEntryFilter {

  private String principal;
  private String host;
  private AclOperation operation;
  private AclPermissionType permissionType;

  public static final AccessControlEntryFilter ANY = new AccessControlEntryFilter(null, null, AclOperation.ANY, AclPermissionType.ANY);

  public AccessControlEntryFilter() {
    principal = "UNKNOWN";
    host = "UNKNOWN";
    operation = AclOperation.UNKNOWN;
    permissionType = AclPermissionType.UNKNOWN;
  }

  public AccessControlEntryFilter(String principal, String host, AclOperation operation, AclPermissionType permissionType) {
    this.principal = principal;
    this.host = host;
    this.operation = Objects.requireNonNull(operation);
    this.permissionType = Objects.requireNonNull(permissionType);
  }

  public AccessControlEntryFilter(JsonObject json) {
    principal = json.getString("principal");
    host = json.getString("host");
    if(json.containsKey("aclOperation")) {
      operation = AclOperation.fromString(json.getString("aclOperation"));
    } else {
      operation = AclOperation.UNKNOWN;
    }
    if(json.containsKey("permissionType")) {
      permissionType = AclPermissionType.fromString(json.getString("aclOperation"));
    } else {
      permissionType = AclPermissionType.UNKNOWN;
    }
  }

  public String getPrincipal() {
    return principal;
  }

  public AccessControlEntryFilter setPrincipal(String principal) {
    this.principal = principal;
    return this;
  }

  public String getHost() {
    return host;
  }

  public AccessControlEntryFilter setHost(String host) {
    this.host = host;
    return this;
  }

  public AclOperation getOperation() {
    return operation;
  }

  public AccessControlEntryFilter setOperation(AclOperation operation) {
    this.operation = Objects.requireNonNull(operation);
    return this;
  }

  public AclPermissionType getPermissionType() {
    return permissionType;
  }

  public AccessControlEntryFilter setPermissionType(AclPermissionType permissionType) {
    this.permissionType = Objects.requireNonNull(permissionType);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (Objects.nonNull(principal)) {
      json.put("principal", principal);
    }
    if (Objects.nonNull(host)) {
      json.put("host", host);
    }
    json
      .put("aclOperation", operation)
      .put("permissionType", permissionType);
    return json;
  }

  public boolean isUnknown() {
    return operation.isUnknown() || permissionType.isUnknown();
  }

  /**
   * Returns true if this filter matches the given AccessControlEntry.
   */
  public boolean matches(AccessControlEntry other) {
    if ((principal != null) && (!principal.equals(other.getPrincipal())))
      return false;
    if ((host != null) && (!host.equals(other.getHost())))
      return false;
    if ((operation != AclOperation.ANY) && (!operation.equals(other.getOperation())))
      return false;
    return (permissionType == AclPermissionType.ANY) || (permissionType.equals(other.getPermissionType()));
  }

  /**
   * Returns true if this filter could only match one ACE -- in other words, if
   * there are no ANY or UNKNOWN fields.
   */
  public boolean matchesAtMostOne() {
    return findIndefiniteField() == null;
  }

  /**
   * Returns a string describing an ANY or UNKNOWN field, or null if there is
   * no such field.
   */
  public String findIndefiniteField() {
    if (principal == null)
      return "Principal is NULL";
    if (host == null)
      return "Host is NULL";
    if (operation == AclOperation.ANY)
      return "Operation is ANY";
    if (operation == AclOperation.UNKNOWN)
      return "Operation is UNKNOWN";
    if (permissionType == AclPermissionType.ANY)
      return "Permission type is ANY";
    if (permissionType == AclPermissionType.UNKNOWN)
      return "Permission type is UNKNOWN";
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AccessControlEntryFilter that = (AccessControlEntryFilter) o;

    if (!Objects.equals(principal, that.principal)) return false;
    if (!Objects.equals(host, that.host)) return false;
    if (operation != that.operation) return false;
    return permissionType == that.permissionType;
  }

  @Override
  public int hashCode() {
    int result = principal != null ? principal.hashCode() : 0;
    result = 31 * result + (host != null ? host.hashCode() : 0);
    result = 31 * result + (operation != null ? operation.hashCode() : 0);
    result = 31 * result + (permissionType != null ? permissionType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AccessControlEntryFilter{" +
      "principal='" + principal + '\'' +
      ", host='" + host + '\'' +
      ", operation=" + operation +
      ", permissionType=" + permissionType +
      '}';
  }
}
