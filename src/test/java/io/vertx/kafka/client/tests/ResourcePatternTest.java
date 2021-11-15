package io.vertx.kafka.client.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.ResourcePattern;
import io.vertx.kafka.client.common.ResourcePatternConverter;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;
import org.junit.Test;
import org.mockito.MockedStatic;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public class ResourcePatternTest {

  @Test
  public void testNoArgsConstructor() {
    ResourcePattern resourcePattern = new ResourcePattern();
    assertNull(resourcePattern.getResourceType());
    assertNull(resourcePattern.getName());
    assertNull(resourcePattern.getPatternType());
  }

  @Test
  public void testAllArgsConstructor() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    assertEquals(resourceType, resourcePattern.getResourceType());
    assertEquals(name, resourcePattern.getName());
    assertEquals(patternType, resourcePattern.getPatternType());
  }

  @Test
  public void testJsonArgConstructor() {
    JsonObject jsonObject = new JsonObject();
    try (MockedStatic<ResourcePatternConverter> converterMock = mockStatic(ResourcePatternConverter.class)) {
      ResourcePattern resourcePattern = new ResourcePattern(jsonObject);
      converterMock.verify(() -> ResourcePatternConverter.fromJson(jsonObject, resourcePattern));
    }
  }

  @Test
  public void testToJson() {
    ResourcePattern resourcePattern = new ResourcePattern();
    try (MockedStatic<ResourcePatternConverter> converterMock = mockStatic(ResourcePatternConverter.class)) {
      resourcePattern.toJson();
      converterMock.verify(() -> ResourcePatternConverter.toJson(eq(resourcePattern), any(JsonObject.class)));
    }
  }

  @Test
  public void testGetters() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    assertEquals(resourceType, resourcePattern.getResourceType());
    assertEquals(name, resourcePattern.getName());
    assertEquals(patternType, resourcePattern.getPatternType());
  }

  @Test
  public void testSetResourceType() {
    ResourceType resourceType = ResourceType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern();
    assertNull(resourcePattern.getResourceType());
    resourcePattern.setResourceType(resourceType);
    assertEquals(resourceType, resourcePattern.getResourceType());
  }

  @Test
  public void testSetName() {
    String name = "some_name";
    ResourcePattern resourcePattern = new ResourcePattern();
    assertNull(resourcePattern.getName());
    resourcePattern.setName(name);
    assertEquals(name, resourcePattern.getName());
  }

  @Test
  public void testSetPatternType() {
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern();
    assertNull(resourcePattern.getPatternType());
    resourcePattern.setPatternType(patternType);
    assertEquals(patternType, resourcePattern.getPatternType());
  }

  @Test
  public void testIsUnknown() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.UNKNOWN;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    assertTrue(resourcePattern.isUnknown());
  }

  @Test
  public void testToString() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern = new ResourcePattern(resourceType, name, patternType);
    assertEquals("ResourcePattern{resourceType=" + resourcePattern.getResourceType() + ",name=" + resourcePattern.getName() +
      ",patternType=" + resourcePattern.getPatternType() + "}", resourcePattern.toString());
  }

  @Test
  public void testEquals() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType = PatternType.ANY;
    ResourcePattern resourcePattern1 = new ResourcePattern(resourceType, name, patternType);
    ResourcePattern resourcePattern2 = new ResourcePattern(resourceType, name, patternType);
    assertEquals(resourcePattern1, resourcePattern2);
    ResourcePattern resourcePattern3 = new ResourcePattern();
    ResourcePattern resourcePattern4 = new ResourcePattern();
    assertEquals(resourcePattern3, resourcePattern4);
  }

  @Test
  public void testNotEquals() {
    ResourceType resourceType = ResourceType.ANY;
    String name = "some_name";
    PatternType patternType1 = PatternType.ANY;
    ResourcePattern resourcePattern1 = new ResourcePattern(resourceType, name, patternType1);
    PatternType patternType2 = PatternType.UNKNOWN;
    ResourcePattern resourcePattern2 = new ResourcePattern(resourceType, name, patternType2);
    assertNotEquals(resourcePattern1, resourcePattern2);
  }

}
