package io.vertx.kafka.client.common;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;

import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessControlEntryData {
  private String principal;
  private String host;
  private AclOperation operation;
  private AclPermissionType permissionType;

  public AccessControlEntryData(JsonObject json) {
    AccessControlEntryDataConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    AccessControlEntryDataConverter.toJson(this, json);
    return json;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessControlEntryData that = (AccessControlEntryData) o;
    return (
      Objects.equals(principal, that.principal) &&
        Objects.equals(host, that.host) &&
        Objects.equals(operation, that.operation) &&
        Objects.equals(permissionType, that.permissionType)
    );
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (principal != null ? principal.hashCode() : 0);
    result = 31 * result + (host != null ? host.hashCode() : 0);
    result = 31 * result + (operation != null ? operation.hashCode() : 0);
    result = 31 * result + (permissionType != null ? permissionType.hashCode() : 0);
    return result;
  }
}
