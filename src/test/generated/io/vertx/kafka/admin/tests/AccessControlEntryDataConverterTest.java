package io.vertx.kafka.admin.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.AccessControlEntryData;
import io.vertx.kafka.client.common.AccessControlEntryDataConverter;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccessControlEntryDataConverterTest {

  @Test
  public void testFromJson() {
    Map<String, Object> json = new HashMap<>();
    String principal = "some_principal";
    String host = "some_host";
    AclOperation operation = AclOperation.ANY;
    AclPermissionType permissionType = AclPermissionType.ANY;
    json.put("principal", principal);
    json.put("host", host);
    json.put("operation", operation.name());
    json.put("permissionType", permissionType.name());
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getPrincipal());
    assertNull(accessControlEntryData.getHost());
    assertNull(accessControlEntryData.getOperation());
    assertNull(accessControlEntryData.getPermissionType());
    AccessControlEntryDataConverter.fromJson(json.entrySet(), accessControlEntryData);
    assertEquals(principal, accessControlEntryData.getPrincipal());
    assertEquals(host, accessControlEntryData.getHost());
    assertEquals(operation, accessControlEntryData.getOperation());
    assertEquals(permissionType, accessControlEntryData.getPermissionType());
  }

  @Test
  public void testToJsonObjectParam() {
    AccessControlEntryData obj = new AccessControlEntryData();
    JsonObject json = new JsonObject();
    try (MockedStatic<AccessControlEntryDataConverter> converterMock = mockStatic(AccessControlEntryDataConverter.class, CALLS_REAL_METHODS)) {
      AccessControlEntryDataConverter.toJson(obj, json);
      converterMock.verify(() -> AccessControlEntryDataConverter.toJson(obj, json.getMap()));
    }
  }

  @Test
  public void testToJsonMapParam() {
    Map<String, Object> json = new HashMap<>();
    String principal = "some_principal";
    String host = "some_host";
    AclOperation operation = AclOperation.ANY;
    AclPermissionType permissionType = AclPermissionType.ANY;
    AccessControlEntryData obj = new AccessControlEntryData(principal, host, operation, permissionType);
    AccessControlEntryDataConverter.toJson(obj, json);
    assertEquals(principal, json.get("principal"));
    assertEquals(host, json.get("host"));
    assertEquals(operation.name(), json.get("operation"));
    assertEquals(permissionType.name(), json.get("permissionType"));
  }

  @Test
  public void testToJsonMapParamNullFields() {
    Map<String, Object> json = spy(new HashMap<>());
    AccessControlEntryData obj = spy(new AccessControlEntryData());
    AccessControlEntryDataConverter.toJson(obj, json);
    verify(json, never()).put(anyString(), anyString());
    assertTrue(json.isEmpty());
  }

}
