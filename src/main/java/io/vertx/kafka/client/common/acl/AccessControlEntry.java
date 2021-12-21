package io.vertx.kafka.client.common.acl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import java.util.Objects;

@DataObject()
public class AccessControlEntry {

  private String principal;
  private String host;
  private AclOperation operation;
  private AclPermissionType permissionType;

  public AccessControlEntry() {
    principal = "UNKNOWN";
    host = "UNKNOWN";
    operation = AclOperation.UNKNOWN;
    permissionType = AclPermissionType.UNKNOWN;
  }

  public AccessControlEntry(String principal, String host, AclOperation operation, AclPermissionType permissionType) {
    this.principal = Objects.requireNonNull(principal);
    this.host = Objects.requireNonNull(host);
    this.operation = Objects.requireNonNull(operation);
    this.permissionType = Objects.requireNonNull(permissionType);
    checkOperationType(operation);
    checkPermissionType(permissionType);
  }

  private void checkPermissionType(AclPermissionType permissionType) {
    if (permissionType == AclPermissionType.ANY)
      throw new IllegalArgumentException("permissionType must not be ANY");
  }

  private void checkOperationType(AclOperation operation) {
    if (operation == AclOperation.ANY)
      throw new IllegalArgumentException("operation must not be ANY");
  }


  public AccessControlEntry(JsonObject json) {
    if(json.containsKey("principal")) {
      principal = Objects.requireNonNull(json.getString("principal"));
    } else {
      principal = "UNKNOWN";
    }
    if(json.containsKey("host")) {
      host = Objects.requireNonNull(json.getString("host"));
    } else {
      host = "UNKNOWN";
    }
    if(json.containsKey("aclOperation")) {
      operation = AclOperation.fromString(json.getString("aclOperation"));
      checkOperationType(operation);
    } else {
      operation = AclOperation.UNKNOWN;
    }
    if(json.containsKey("permissionType")) {
      permissionType = AclPermissionType.fromString(json.getString("aclOperation"));
      checkPermissionType(permissionType);
    } else {
      permissionType = AclPermissionType.UNKNOWN;
    }
  }

  public String getPrincipal() {
    return principal;
  }

  public AccessControlEntry setPrincipal(String principal) {
    this.principal = Objects.requireNonNull(principal);
    return this;
  }

  public String getHost() {
    return host;
  }

  public AccessControlEntry setHost(String host) {
    this.host = Objects.requireNonNull(host);
    return this;
  }

  public AclOperation getOperation() {
    return operation;
  }

  public AccessControlEntry setOperation(AclOperation operation) {
    this.operation = Objects.requireNonNull(operation);
    checkOperationType(operation);
    return this;
  }

  public AclPermissionType getPermissionType() {
    return permissionType;
  }

  public AccessControlEntry setPermissionType(AclPermissionType permissionType) {
    this.permissionType = Objects.requireNonNull(permissionType);
    checkPermissionType(permissionType);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("principal", principal)
      .put("host", host)
      .put("aclOperation", operation)
      .put("permissionType", permissionType);
    return json;
  }

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

    AccessControlEntry that = (AccessControlEntry) o;

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
    return "AccessControlEntryData{" +
      "principal='" + principal + '\'' +
      ", host='" + host + '\'' +
      ", operation=" + operation +
      ", permissionType=" + permissionType +
      '}';
  }
}
