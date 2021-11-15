package io.vertx.kafka.admin.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.AccessControlEntryData;
import io.vertx.kafka.client.common.AclBinding;
import io.vertx.kafka.client.common.AclBindingConverter;
import io.vertx.kafka.client.common.ResourcePattern;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AclBindingConverterTest {

  @Test
  public void testFromJson() {
    Map<String, Object> json = new HashMap<>();
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    JsonObject resourcePatternJson = resourcePattern.toJson();
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData entryData = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    JsonObject entryDataJson = entryData.toJson();
    AclBinding obj = new AclBinding();
    assertNull(obj.getResourcePattern());
    assertNull(obj.getEntryData());
    json.put("resourcePattern", resourcePatternJson);
    json.put("entryData", entryDataJson);
    AclBindingConverter.fromJson(json.entrySet(), obj);
    assertEquals(resourcePattern, obj.getResourcePattern());
    assertEquals(entryData, obj.getEntryData());
  }

  @Test
  public void testToJsonObjectParam() {
    AclBinding obj = new AclBinding();
    JsonObject json = new JsonObject();
    try (MockedStatic<AclBindingConverter> converterMock = mockStatic(AclBindingConverter.class, CALLS_REAL_METHODS)) {
      AclBindingConverter.toJson(obj, json);
      converterMock.verify(() -> AclBindingConverter.toJson(obj, json.getMap()));
    }
  }

  @Test
  public void testToJsonMapParam() {
    Map<String, Object> json = new HashMap<>();
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    String principal = "some_principal";
    String host = "some_host";
    AclOperation aclOperation = AclOperation.ALL;
    AclPermissionType aclPermissionType = AclPermissionType.ANY;
    AccessControlEntryData entryData = new AccessControlEntryData(principal, host, aclOperation, aclPermissionType);
    AclBinding obj = new AclBinding(resourcePattern, entryData);
    AclBindingConverter.toJson(obj, json);
    assertEquals(resourcePattern.toJson(), json.get("resourcePattern"));
    assertEquals(entryData.toJson(), json.get("entryData"));
  }

  @Test
  public void testToJsonMapParamNullFields() {
    Map<String, Object> json = spy(new HashMap<>());
    AclBinding obj = spy(new AclBinding());
    AclBindingConverter.toJson(obj, json);
    verify(json, never()).put(anyString(), any(JsonObject.class));
  }
}
