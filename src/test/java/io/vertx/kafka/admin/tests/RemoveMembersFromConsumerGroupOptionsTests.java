package io.vertx.kafka.admin.tests;

import com.google.common.collect.ImmutableSet;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.admin.MemberToRemove;
import io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptions;
import io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptionsConverter;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public class RemoveMembersFromConsumerGroupOptionsTests {

  @Test
  public void testConstructorWithParamSuccess() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    List<MemberToRemove> membersToRemove = Collections.singletonList(memberToRemove);
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions(membersToRemove);
    assertNotNull(removeMembersFromConsumerGroupOptions.getMembers());
    assertEquals(1, removeMembersFromConsumerGroupOptions.getMembers().size());
    assertTrue(removeMembersFromConsumerGroupOptions.getMembers().contains(memberToRemove));
  }

  @Test
  public void testConstructorWithParamFailure() {
    try {
      new RemoveMembersFromConsumerGroupOptions(Collections.emptyList());
    } catch (IllegalArgumentException exception) {
      assertEquals("Invalid empty members has been provided", exception.getMessage());
    }
  }

  @Test
  public void testConstructorWithNoParams() {
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions();
    assertEquals(Collections.emptySet(), removeMembersFromConsumerGroupOptions.getMembers());
  }

  @Test
  public void testGetMember() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    Set<MemberToRemove> memberToRemoveSet = new HashSet(Collections.singletonList(memberToRemove));
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions();
    removeMembersFromConsumerGroupOptions.setMembers(memberToRemoveSet);
    assertEquals(memberToRemoveSet, removeMembersFromConsumerGroupOptions.getMembers());
  }

  @Test
  public void testSetMembers() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    Set<MemberToRemove> memberToRemoveSet = ImmutableSet.of(memberToRemove);
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions();
    assertEquals(Collections.emptySet(), removeMembersFromConsumerGroupOptions.getMembers());
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptionsNew =
      removeMembersFromConsumerGroupOptions.setMembers(ImmutableSet.of(memberToRemove));
    assertEquals(memberToRemoveSet, removeMembersFromConsumerGroupOptionsNew.getMembers());
  }

  @Test
  public void testRemoveAll() {
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions();
    assertTrue(removeMembersFromConsumerGroupOptions.removeAll());
  }

  @Test
  public void testToJson() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions(ImmutableSet.of(memberToRemove));
    try(MockedStatic<RemoveMembersFromConsumerGroupOptionsConverter> converterMockedStatic = mockStatic(RemoveMembersFromConsumerGroupOptionsConverter.class)) {
      removeMembersFromConsumerGroupOptions.toJson();
      converterMockedStatic.verify(() -> RemoveMembersFromConsumerGroupOptionsConverter.toJson(eq(removeMembersFromConsumerGroupOptions), any(JsonObject.class)));
    }
  }

  @Test
  public void testToString() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions = new RemoveMembersFromConsumerGroupOptions(ImmutableSet.of(memberToRemove));
    assertEquals("RemoveMembersFromConsumerGroupOptions{members=" + removeMembersFromConsumerGroupOptions.getMembers() + "}",
      removeMembersFromConsumerGroupOptions.toString());
  }

}
