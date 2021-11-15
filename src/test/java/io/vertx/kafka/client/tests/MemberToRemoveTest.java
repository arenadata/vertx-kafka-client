package io.vertx.kafka.client.tests;

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

  @Test
  public void testEquals() {
    String groupInstanceId = "some_group_instance_id";
    MemberToRemove memberToRemove1 = new MemberToRemove(groupInstanceId);
    MemberToRemove memberToRemove2 = new MemberToRemove(groupInstanceId);
    assertEquals(memberToRemove1, memberToRemove2);
    MemberToRemove memberToRemove3 = new MemberToRemove();
    MemberToRemove memberToRemove4 = new MemberToRemove();
    assertEquals(memberToRemove3, memberToRemove4);
  }

  @Test
  public void testNotEquals() {
    String groupInstanceId1 = "some_group_instance_id_1";
    String groupInstanceId2 = "some_group_instance_id_2";
    MemberToRemove memberToRemove1 = new MemberToRemove(groupInstanceId1);
    MemberToRemove memberToRemove2 = new MemberToRemove(groupInstanceId2);
    assertNotEquals(memberToRemove1, memberToRemove2);
  }

}
