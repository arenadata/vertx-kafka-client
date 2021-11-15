package io.vertx.kafka.client.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.AccessControlEntryData;
import io.vertx.kafka.client.common.AccessControlEntryDataConverter;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.junit.Test;
import org.mockito.MockedStatic;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

public class AccessControlEntryDataTest {

  @Test
  public void testNoArgsConstructor() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getPrincipal());
    assertNull(accessControlEntryData.getHost());
    assertNull(accessControlEntryData.getOperation());
    assertNull(accessControlEntryData.getPermissionType());
  }

  @Test
  public void testAllArgsConstructor() {
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    assertEquals(principal, accessControlEntryData.getPrincipal());
    assertEquals(host, accessControlEntryData.getHost());
    assertEquals(aclOperation, accessControlEntryData.getOperation());
    assertEquals(aclPermissionType, accessControlEntryData.getPermissionType());
  }

  @Test
  public void testJsonConstructor() {
    JsonObject json = new JsonObject();
    try (MockedStatic<AccessControlEntryDataConverter> converterMocked = mockStatic(AccessControlEntryDataConverter.class)) {
      AccessControlEntryData accessControlEntryData = new AccessControlEntryData(json);
      converterMocked.verify(() -> AccessControlEntryDataConverter.fromJson(json, accessControlEntryData));
    }
  }

  @Test
  public void testToJson() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    try (MockedStatic<AccessControlEntryDataConverter> converterMocked = mockStatic(AccessControlEntryDataConverter.class)) {
      accessControlEntryData.toJson();
      converterMocked.verify(() -> AccessControlEntryDataConverter.toJson(eq(accessControlEntryData), any(JsonObject.class)));
    }
  }

  @Test
  public void testGetters() {
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    assertEquals(principal, accessControlEntryData.getPrincipal());
    assertEquals(host, accessControlEntryData.getHost());
    assertEquals(aclOperation, accessControlEntryData.getOperation());
    assertEquals(aclPermissionType, accessControlEntryData.getPermissionType());
  }

  @Test
  public void testSetPrincipal() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getPrincipal());
    String principal = "some_principal";
    accessControlEntryData.setPrincipal(principal);
    assertEquals(principal, accessControlEntryData.getPrincipal());
  }

  @Test
  public void testSetHost() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getHost());
    String host = "some_host";
    accessControlEntryData.setHost(host);
    assertEquals(host, accessControlEntryData.getHost());
  }

  @Test
  public void testSetOperation() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getOperation());
    AclOperation aclOperation = AclOperation.ALL;
    accessControlEntryData.setOperation(aclOperation);
    assertEquals(aclOperation, accessControlEntryData.getOperation());
  }

  @Test
  public void testSetPermissionType() {
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    assertNull(accessControlEntryData.getPermissionType());
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    accessControlEntryData.setPermissionType(aclPermissionType);
    assertEquals(aclPermissionType, accessControlEntryData.getPermissionType());
  }

  @Test
  public void testToString() {
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    assertEquals("AccessControlEntryData{principal=" + accessControlEntryData.getPrincipal() + ",host=" +
      accessControlEntryData.getHost() + ",operation=" + accessControlEntryData.getOperation() + ",permissionType=" +
      accessControlEntryData.getPermissionType() + "}", accessControlEntryData.toString());
  }

  @Test
  public void testEquals() {
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData accessControlEntryData1 = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    AccessControlEntryData accessControlEntryData2 = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    assertEquals(accessControlEntryData1, accessControlEntryData2);
    AccessControlEntryData accessControlEntryData3 = new AccessControlEntryData();
    AccessControlEntryData accessControlEntryData4 = new AccessControlEntryData();
    assertEquals(accessControlEntryData3, accessControlEntryData4);
  }

  @Test
  public void testNotEquals() {
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation1 = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData accessControlEntryData1 = new AccessControlEntryData(principal, host, aclOperation1, aclPermissionType);
    AclOperation aclOperation2 = AclOperation.ANY;
    AccessControlEntryData accessControlEntryData2 = new AccessControlEntryData(principal, host, aclOperation2, aclPermissionType);
    assertNotEquals(accessControlEntryData1, accessControlEntryData2);
  }

}
