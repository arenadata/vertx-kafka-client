package io.vertx.kafka.admin.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.ResourcePattern;
import io.vertx.kafka.client.common.ResourcePatternConverter;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResourcePatternConverterTest {

  @Test
  public void testFromJson() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    Map<String, Object> resourcePatternMap = new HashMap<>();
    resourcePatternMap.put("resourceType", resourceType.name());
    resourcePatternMap.put("name", name);
    resourcePatternMap.put("patternType", patternType.name());
    ResourcePattern resourcePattern = new ResourcePattern();
    assertNull(resourcePattern.getResourceType());
    assertNull(resourcePattern.getName());
    assertNull(resourcePattern.getPatternType());
    ResourcePatternConverter.fromJson(resourcePatternMap.entrySet(), resourcePattern);
    assertEquals(resourceType, resourcePattern.getResourceType());
    assertEquals(name, resourcePattern.getName());
    assertEquals(patternType, resourcePattern.getPatternType());
  }

  @Test
  public void testToJsonObjectParam() {
    ResourcePattern obj = new ResourcePattern();
    JsonObject json = new JsonObject();
    try (MockedStatic<ResourcePatternConverter> converterMock = mockStatic(ResourcePatternConverter.class, CALLS_REAL_METHODS)) {
      ResourcePatternConverter.toJson(obj, json);
      converterMock.verify(() -> ResourcePatternConverter.toJson(obj, json.getMap()));
    }
  }

  @Test
  public void testToJsonMapParam() {
    Map<String, Object> json = new HashMap<>();
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    ResourcePatternConverter.toJson(resourcePattern, json);
    assertEquals(resourceType.name(), json.get("resourceType"));
    assertEquals(name, json.get("name"));
    assertEquals(patternType.name(), json.get("patternType"));
  }
  @Test
  public void testToJsonMapParamNullFields() {
    Map<String, Object> json = spy(new HashMap<>());
    ResourcePattern obj = spy(new ResourcePattern());
    ResourcePatternConverter.toJson(obj, json);
    verify(json, never()).put(anyString(), anyString());
    assertTrue(json.isEmpty());
  }
}
