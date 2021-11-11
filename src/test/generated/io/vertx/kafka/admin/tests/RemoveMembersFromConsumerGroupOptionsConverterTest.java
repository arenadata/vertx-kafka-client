package io.vertx.kafka.admin.tests;

import com.google.common.collect.ImmutableSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.admin.MemberToRemove;
import io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptions;
import io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptionsConverter;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemoveMembersFromConsumerGroupOptionsConverterTest {

  @Test
  public void testFromJson() {
    JsonObject jsonObject = new JsonObject();
    String groupInstanceId = "some_group_instance_id";
    MemberToRemove memberToRemove = new MemberToRemove(groupInstanceId);
    jsonObject.put("groupInstanceId", groupInstanceId);
    JsonArray jsonArray = new JsonArray().add(jsonObject);
    Map<String, Object> jsonMap = Collections.singletonMap("members", jsonArray);
    Iterable<Map.Entry<String, Object>> jsonEntries = jsonMap.entrySet();
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions();
    assertTrue(removeMembersFromConsumerGroupOptions.getMembers().isEmpty());
    RemoveMembersFromConsumerGroupOptionsConverter.fromJson(jsonEntries, removeMembersFromConsumerGroupOptions);
    assertFalse(removeMembersFromConsumerGroupOptions.getMembers().isEmpty());
    assertEquals(ImmutableSet.of(memberToRemove), removeMembersFromConsumerGroupOptions.getMembers());
  }

  @Test
  public void testToJsonObjectParam() {
    RemoveMembersFromConsumerGroupOptions obj = new RemoveMembersFromConsumerGroupOptions();
    JsonObject json = new JsonObject();
    try (MockedStatic<RemoveMembersFromConsumerGroupOptionsConverter> converterMocked = mockStatic(RemoveMembersFromConsumerGroupOptionsConverter.class, CALLS_REAL_METHODS)) {
      RemoveMembersFromConsumerGroupOptionsConverter.toJson(obj, json);
      converterMocked.verify(() -> RemoveMembersFromConsumerGroupOptionsConverter.toJson(obj, json.getMap()));
    }
  }

  @Test
  public void testToJsonMapParam() {
    Map<String, Object> json = new HashMap<>();
    String groupInstanceId = "some_group_instance_id";
    MemberToRemove memberToRemove = new MemberToRemove(groupInstanceId);
    RemoveMembersFromConsumerGroupOptions obj = new RemoveMembersFromConsumerGroupOptions
      (ImmutableSet.of(memberToRemove));
    RemoveMembersFromConsumerGroupOptionsConverter.toJson(obj, json);
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("groupInstanceId", groupInstanceId);
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(jsonObject);
    assertFalse(json.isEmpty());
    assertEquals(1, json.size());
    assertEquals(jsonArray, json.get("members"));
  }

  @Test
  public void testToJsonMapParamNullMembers() {
    Map<String, Object> json = spy(new HashMap<>());
    RemoveMembersFromConsumerGroupOptions obj = spy(new RemoveMembersFromConsumerGroupOptions());
    when(obj.getMembers()).thenReturn(null);
    RemoveMembersFromConsumerGroupOptionsConverter.toJson(obj, json);
    verify(json, never()).put(anyString(), anySet());
    assertTrue(json.isEmpty());
  }

}
