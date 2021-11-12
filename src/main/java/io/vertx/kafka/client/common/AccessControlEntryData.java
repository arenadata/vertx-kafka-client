package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;

public class AccessControlEntryData {
  private String principal;
  private String host;
  private AclOperation operation;
  private AclPermissionType permissionType;

  public AccessControlEntryData() {
  }

  public AccessControlEntryData(String principal, String host, AclOperation operation, AclPermissionType permissionType) {
    this.principal = principal;
    this.host = host;
    this.operation = operation;
    this.permissionType = permissionType;
  }

  public AccessControlEntryData(JsonObject json) {
    AccessControlEntryDataConverter.fromJson(json, this);
  }

  public String getPrincipal() {
    return principal;
  }

  public AccessControlEntryData setPrincipal(String principal) {
    this.principal = principal;
    return this;
  }

  public String getHost() {
    return host;
  }

  public AccessControlEntryData setHost(String host) {
    this.host = host;
    return this;
  }

  public AclOperation getOperation() {
    return operation;
  }

  public AccessControlEntryData setOperation(AclOperation operation) {
    this.operation = operation;
    return this;
  }

  public AclPermissionType getPermissionType() {
    return permissionType;
  }

  public AccessControlEntryData setPermissionType(AclPermissionType permissionType) {
    this.permissionType = permissionType;
    return this;
  }

  boolean isUnknown() {
    return this.operation.isUnknown() || this.permissionType.isUnknown();
  }

  @Override
  public String toString() {
    return "AccessControlEntryData{" +
      "principal=" + this.principal +
      ",host=" + this.host +
      ",operation=" + this.operation +
      ",permissionType=" + this.permissionType +
      "}";
  }
}
