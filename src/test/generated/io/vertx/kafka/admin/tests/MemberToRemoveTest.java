package io.vertx.kafka.admin.tests;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.admin.MemberToRemove;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberToRemoveTest {

  @Test
  public void testConstructorNoParameters() {
    MemberToRemove memberToRemove = new MemberToRemove();
    assertNull(memberToRemove.groupInstanceId());
  }

  @Test
  public void testConstructorGroupInstanceId() {
    String groupInstanceId = "some_group_instance_id";
    MemberToRemove memberToRemove = new MemberToRemove(groupInstanceId);
    assertEquals(groupInstanceId, memberToRemove.groupInstanceId());
  }

  @Test
  public void testConstructorJsonObject() {
    String groupInstanceId = "some_group_instance_id";
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("groupInstanceId", groupInstanceId);
    MemberToRemove memberToRemove = new MemberToRemove(jsonObject);
    assertEquals(groupInstanceId, memberToRemove.groupInstanceId());
  }

  @Test
  public void testConstructorMemberToRemove() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    MemberToRemove memberToRemoveNew = new MemberToRemove(memberToRemove);
    assertEquals(memberToRemove.groupInstanceId(), memberToRemoveNew.groupInstanceId());
  }

  @Test
  public void testSetGroupInstanceId() {
    String groupInstanceId = "some_group_instance_id";
    MemberToRemove memberToRemove = new MemberToRemove();
    MemberToRemove memberToRemoveNew = memberToRemove.setGroupInstanceId(groupInstanceId);
    assertEquals(groupInstanceId, memberToRemoveNew.groupInstanceId());
  }

  @Test
  public void testToJson() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    JsonObject jsonObject = memberToRemove.toJson();
    assertEquals(1, jsonObject.getMap().size());
    assertEquals(memberToRemove.groupInstanceId(), jsonObject.getMap().get("groupInstanceId"));
  }

  @Test
  public void testToString() {
    MemberToRemove memberToRemove = new MemberToRemove("some_group_instance_id");
    String memberToRemoveString = memberToRemove.toString();
    assertEquals("MemberToRemove{groupInstanceId=" + memberToRemove.groupInstanceId() + "}", memberToRemoveString);
  }
}
