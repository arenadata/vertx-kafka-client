package io.vertx.kafka.client.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.AccessControlEntryData;
import io.vertx.kafka.client.common.AclBinding;
import io.vertx.kafka.client.common.AclBindingConverter;
import io.vertx.kafka.client.common.ResourcePattern;

import org.apache.kafka.common.acl.AclPermissionType;
import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import static org.junit.Assert.*;

public class AclBindingTest {

  @Test
  public void testNoArgsConstructor() {
    AclBinding aclBinding = new AclBinding();
    assertNull(aclBinding.getResourcePattern());
    assertNull(aclBinding.getEntryData());
  }

  @Test
  public void testAllArgsConstructor() {
    ResourcePattern resourcePattern = new ResourcePattern();
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    AclBinding aclBinding = new AclBinding(resourcePattern, accessControlEntryData);
    assertEquals(resourcePattern, aclBinding.getResourcePattern());
    assertEquals(accessControlEntryData, aclBinding.getEntryData());
  }

  @Test
  public void testJsonConstructor() {
    JsonObject jsonObject = new JsonObject();
    try (MockedStatic<AclBindingConverter> converterMock = mockStatic(AclBindingConverter.class)) {
      AclBinding aclBinding = new AclBinding(jsonObject);
      converterMock.verify(() -> AclBindingConverter.toJson(aclBinding, jsonObject));
    }
  }

  @Test
  public void testGetters() {
    ResourcePattern resourcePattern = new ResourcePattern();
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    AclBinding aclBinding = new AclBinding(resourcePattern, accessControlEntryData);
    assertEquals(resourcePattern, aclBinding.getResourcePattern());
    assertEquals(accessControlEntryData, aclBinding.getEntryData());
  }

  @Test
  public void testSetters() {
    AclBinding aclBinding = new AclBinding();
    assertNull(aclBinding.getResourcePattern());
    ResourcePattern resourcePattern = new ResourcePattern();
    aclBinding.setResourcePattern(resourcePattern);
    assertEquals(resourcePattern, aclBinding.getResourcePattern());
  }

  @Test
  public void testSetEntryData() {
    AclBinding aclBinding = new AclBinding();
    assertNull(aclBinding.getEntryData());
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    aclBinding.setEntryData(accessControlEntryData);
    assertEquals(accessControlEntryData, aclBinding.getEntryData());
  }

  @Test
  public void testToJson() {
    AclBinding aclBinding = new AclBinding();
    try (MockedStatic<AclBindingConverter> converterMocked = mockStatic(AclBindingConverter.class)) {
      aclBinding.toJson();
      converterMocked.verify(() -> AclBindingConverter.toJson(eq(aclBinding), any(JsonObject.class)));
    }
  }

  @Test
  public void testToString() {
    ResourcePattern resourcePattern = new ResourcePattern();
    AccessControlEntryData entryData = new AccessControlEntryData();
    AclBinding aclBinding = new AclBinding(resourcePattern, entryData);
    String aclBindingString = aclBinding.toString();
    assertEquals("AclBinding{resourcePattern=" + resourcePattern + ",entryData=" + entryData + "}", aclBindingString);
  }

  @Test
  public void testEquals() {
    ResourcePattern resourcePattern = new ResourcePattern();
    AccessControlEntryData accessControlEntryData = new AccessControlEntryData();
    AclBinding aclBinding1 = new AclBinding(resourcePattern, accessControlEntryData);
    AclBinding aclBinding2 = new AclBinding(resourcePattern, accessControlEntryData);
    assertEquals(aclBinding1, aclBinding2);
    AclBinding aclBinding3 = new AclBinding();
    AclBinding aclBinding4 = new AclBinding();
    assertEquals(aclBinding3, aclBinding4);
  }

  @Test
  public void testNotEquals() {
    ResourcePattern resourcePattern = new ResourcePattern();
    AccessControlEntryData accessControlEntryData1 = new AccessControlEntryData();
    AclBinding aclBinding1 = new AclBinding(resourcePattern, accessControlEntryData1);
    AccessControlEntryData accessControlEntryData2 = new AccessControlEntryData();
    accessControlEntryData2.setPermissionType(AclPermissionType.ANY);
    AclBinding aclBinding2 = new AclBinding(resourcePattern, accessControlEntryData2);
    assertNotEquals(aclBinding1, aclBinding2);
  }

}
