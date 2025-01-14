/*
 * Copyright 2019 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.kafka.client.tests;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.kafka.admin.*;
import io.vertx.kafka.client.common.*;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.ElectionNotNeededException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public class AdminClientTest extends KafkaClusterTestBase {
  private Vertx vertx;
  private Properties config;

  private static Set<String> topics = new HashSet<>();

  static {
    topics.add("first-topic");
    topics.add("second-topic");
//    topics.add("offsets-topic");
  }

  @Before
  public void beforeTest() {
    this.vertx = Vertx.vertx();
    this.config = new Properties();
    this.config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
  }

  @After
  public void afterTest(TestContext ctx) {
    this.vertx.close(ctx.asyncAssertSuccess());
  }

  @BeforeClass
  public static void setUp() throws IOException {
    KafkaClusterTestBase.setUp();
    kafkaCluster.createTopics(topics);
  }

  @Test
  public void testListTopics(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {
      adminClient.listTopics(ctx.asyncAssertSuccess(res ->{
        ctx.assertTrue(res.containsAll(topics), "Was expecting topics " + topics + " to be in " + res);

        adminClient.close();
        async.complete();
      }));
    });
  }

  @Test
  public void testDescribeTopics(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {

      adminClient.describeTopics(Collections.singletonList("first-topic"), ctx.asyncAssertSuccess(map -> {
        TopicDescription topicDescription = map.get("first-topic");
        ctx.assertNotNull(topicDescription);
        ctx.assertEquals("first-topic", topicDescription.getName());
        ctx.assertEquals(false, topicDescription.isInternal());
        ctx.assertEquals(1, topicDescription.getPartitions().size());

        TopicPartitionInfo topicPartitionInfo = topicDescription.getPartitions().get(0);
        ctx.assertEquals(0, topicPartitionInfo.getPartition());
        ctx.assertTrue(topicPartitionInfo.getLeader().getId() == 1 || topicPartitionInfo.getLeader().getId() == 2);
        ctx.assertEquals(1, topicPartitionInfo.getReplicas().size());
        ctx.assertTrue(topicPartitionInfo.getReplicas().get(0).getId() == 1 || topicPartitionInfo.getReplicas().get(0).getId() == 2);
        ctx.assertEquals(1, topicPartitionInfo.getIsr().size());
        ctx.assertTrue(topicPartitionInfo.getIsr().get(0).getId() == 1 || topicPartitionInfo.getIsr().get(0).getId() == 2);

        adminClient.close();
        async.complete();
      }));
    });
  }

  @Test
  public void testCreateTopic(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    adminClient.createTopics(Collections.singletonList(new NewTopic("testCreateTopic", 1, (short)1)), ctx.asyncAssertSuccess(v -> {
      adminClient.describeTopics(Collections.singletonList("testCreateTopic"), ctx.asyncAssertSuccess(topics -> {
        TopicDescription topicDescription = topics.get("testCreateTopic");

        ctx.assertEquals("testCreateTopic", topicDescription.getName());
        ctx.assertEquals(1, topicDescription.getPartitions().size());
        ctx.assertEquals(1, topicDescription.getPartitions().get(0).getReplicas().size());

        adminClient.deleteTopics(Collections.singletonList("testCreateTopic"), ctx.asyncAssertSuccess(v1 -> {
          adminClient.close();
        }));
      }));
    }));
  }

  @Test
  public void testCreateTopicWithConfigs(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    NewTopic newTopic = new NewTopic("testCreateTopicWithConfigs", 1, (short)1);
    newTopic.setConfig(Collections.singletonMap("segment.bytes", "1000"));

    adminClient.createTopics(Collections.singletonList(newTopic), ctx.asyncAssertSuccess(v -> {

      adminClient.describeTopics(Collections.singletonList("testCreateTopicWithConfigs"), ctx.asyncAssertSuccess(topics -> {

        TopicDescription topicDescription = topics.get("testCreateTopicWithConfigs");

        ctx.assertEquals("testCreateTopicWithConfigs", topicDescription.getName());
        ctx.assertEquals(1, topicDescription.getPartitions().size());
        ctx.assertEquals(1, topicDescription.getPartitions().get(0).getReplicas().size());

        ConfigResource configResource =
          new ConfigResource(org.apache.kafka.common.config.ConfigResource.Type.TOPIC, "testCreateTopicWithConfigs");

        adminClient.describeConfigs(Collections.singletonList(configResource), ctx.asyncAssertSuccess(descs -> {

          Optional<ConfigEntry> configEntry = descs.get(configResource).getEntries().stream()
            .filter(e -> e.getName().equals("segment.bytes"))
            .findFirst();
          ctx.assertTrue(configEntry.isPresent());
          ctx.assertEquals("1000", configEntry.get().getValue());

          adminClient.deleteTopics(Collections.singletonList("testCreateTopicWithConfigs"), ctx.asyncAssertSuccess(v1 -> {
            adminClient.close();
          }));
        }));
      }));
    }));
  }


  @Test
  public void testDeleteTopic(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    kafkaCluster.createTopic("topicToDelete", 1, 1);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {

      adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {

        ctx.assertTrue(topics.contains("topicToDelete"));

        adminClient.deleteTopics(Collections.singletonList("topicToDelete"), ctx.asyncAssertSuccess(v -> {
          adminClient.close();
          async.complete();
        }));
      }));
    });
  }

  @Test
  public void testDescribeConfigs(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    adminClient.describeConfigs(Collections.singletonList(
      new ConfigResource(org.apache.kafka.common.config.ConfigResource.Type.TOPIC, "first-topic")), ctx.asyncAssertSuccess(desc -> {
      ctx.assertFalse(desc.isEmpty());
      adminClient.close();
    }));
  }

  @Test
  public void testAlterConfigs(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    ConfigResource resource = new ConfigResource(org.apache.kafka.common.config.ConfigResource.Type.TOPIC, "first-topic");
    // create a entry for updating the retention.ms value on the topic
    ConfigEntry retentionEntry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, "51000");
    Map<ConfigResource, Config> updateConfig = new HashMap<>();
    updateConfig.put(resource, new Config(Collections.singletonList(retentionEntry)));

    adminClient.alterConfigs(updateConfig, ctx.asyncAssertSuccess(v -> {

      adminClient.describeConfigs(Collections.singletonList(resource), ctx.asyncAssertSuccess(describeConfig -> {

        ConfigEntry describeRetentionEntry =
          describeConfig.get(resource)
            .getEntries()
            .stream()
            .filter(entry -> entry.getName().equals(TopicConfig.RETENTION_MS_CONFIG))
            .collect(Collectors.toList())
            .get(0);

        ctx.assertEquals("51000", describeRetentionEntry.getValue());
        adminClient.close();
      }));
    }));
  }

  @Test
  public void testListConsumerGroups(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    //kafkaCluster.useTo().consumeStrings(() -> true, null, Collections.singletonList("first-topic"), c -> { });

    kafkaCluster.useTo().consume("groupId", "clientId", OffsetResetStrategy.EARLIEST,
      new StringDeserializer(), new StringDeserializer(), () -> true, null, null,
      Collections.singleton("first-topic"), c -> { });

    // timer because, Kafka cluster takes time to start consumer
    vertx.setTimer(1000, t -> {

      adminClient.listConsumerGroups(ctx.asyncAssertSuccess(groups -> {

        ctx.assertTrue(groups.size() > 0);
        ctx.assertTrue(groups.stream().map(ConsumerGroupListing::getGroupId).anyMatch(g -> g.equals("groupId")));
        adminClient.close();
        async.complete();
      }));

    });
  }

  @Test
  public void testDescribeConsumerGroups(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    //kafkaCluster.useTo().consumeStrings(() -> true, null, Collections.singletonList("first-topic"), c -> { });

    kafkaCluster.useTo().consume("groupId", "clientId", OffsetResetStrategy.EARLIEST,
      new StringDeserializer(), new StringDeserializer(), () -> true, null, null,
      Collections.singleton("first-topic"), c -> { });

    // timer because, Kafka cluster takes time to start consumer
    vertx.setTimer(1000, t -> {

      adminClient.describeConsumerGroups(Collections.singletonList("groupId"), ctx.asyncAssertSuccess(groups -> {

        ConsumerGroupDescription consumerGroupDescription = groups.get("groupId");
        ctx.assertNotNull(consumerGroupDescription);
        ctx.assertEquals("groupId", consumerGroupDescription.getGroupId());
        ctx.assertEquals(1, consumerGroupDescription.getMembers().size());

        MemberDescription memberDescription = consumerGroupDescription.getMembers().get(0);
        ctx.assertEquals("clientId", memberDescription.getClientId());
        ctx.assertEquals(1, memberDescription.getAssignment().getTopicPartitions().size());

        Iterator<TopicPartition> iterator = memberDescription.getAssignment().getTopicPartitions().iterator();
        ctx.assertTrue(iterator.hasNext());
        ctx.assertEquals("first-topic", iterator.next().getTopic());

        adminClient.close();
        async.complete();
      }));

    });
  }

  @Test
  public void testDeleteConsumerGroups(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    final Async consumeAsync = ctx.async();
    final AtomicBoolean groupIsEmpty = new AtomicBoolean();
    groupIsEmpty.set(true);
    kafkaCluster.useTo().consume("groupId-1", "clientId-1", OffsetResetStrategy.EARLIEST,
      new StringDeserializer(), new StringDeserializer(), groupIsEmpty::get, null, consumeAsync::complete,
      Collections.singleton("first-topic"), c -> { groupIsEmpty.set(false); });

    kafkaCluster.useTo().consume("groupId-2", "clientId-2", OffsetResetStrategy.EARLIEST,
      new StringDeserializer(), new StringDeserializer(), () -> true, null, null,
      Collections.singleton("first-topic"), c -> { });

    kafkaCluster.useTo().produceIntegers("first-topic", 6, 1, null);

    consumeAsync.awaitSuccess(10000);

      adminClient.deleteConsumerGroups(Collections.singletonList("groupId-1"), ctx.asyncAssertSuccess(v -> {

      adminClient.listConsumerGroups(ctx.asyncAssertSuccess(groups -> {

        ctx.assertTrue(groups.stream().map(ConsumerGroupListing::getGroupId).noneMatch(g -> g.equals("groupId-1")));
        ctx.assertTrue(groups.stream().map(ConsumerGroupListing::getGroupId).anyMatch(g -> g.equals("groupId-2")));

        adminClient.close();
        async.complete();
      }));

    }));

  }

  @Test
  public void testConsumersOnTopics() {

    List<ConsumerGroupDescription> cGroups = new ArrayList<>();

    List<MemberDescription> members1 = new ArrayList<>();

    MemberDescription m1 = new MemberDescription();
    MemberAssignment ma1 = new MemberAssignment();
    Set<TopicPartition> setTp1 = new HashSet<>();
    setTp1.add(new TopicPartition("my-topic", 0));
    setTp1.add(new TopicPartition("my-topic", 1));
    setTp1.add(new TopicPartition("your-topic", 0));
    ma1.setTopicPartitions(setTp1);
    m1.setAssignment(ma1);

    MemberDescription m2 = new MemberDescription();
    MemberAssignment ma2 = new MemberAssignment();
    Set<TopicPartition> setTp2 = new HashSet<>();
    setTp2.add(new TopicPartition("my-topic", 2));
    setTp2.add(new TopicPartition("my-topic", 3));
    setTp2.add(new TopicPartition("his-topic", 0));
    ma2.setTopicPartitions(setTp2);
    m2.setAssignment(ma2);

    members1.add(m1);
    members1.add(m2);

    List<MemberDescription> members2 = new ArrayList<>();

    MemberDescription m3 = new MemberDescription();
    MemberAssignment ma3 = new MemberAssignment();
    Set<TopicPartition> setTp3 = new HashSet<>();
    setTp3.add(new TopicPartition("my-topic", 0));
    setTp3.add(new TopicPartition("my-topic", 1));
    setTp3.add(new TopicPartition("my-topic", 2));
    setTp3.add(new TopicPartition("my-topic", 3));
    setTp3.add(new TopicPartition("his-topic", 0));
    setTp3.add(new TopicPartition("your-topic", 0));
    ma3.setTopicPartitions(setTp3);
    m3.setAssignment(ma3);

    members2.add(m3);

    cGroups.add(new ConsumerGroupDescription().setGroupId("groupid-1").setMembers(members1));
    cGroups.add(new ConsumerGroupDescription().setGroupId("groupid-2").setMembers(members2));

    Map<String, Integer> consumers = new HashMap<>();

    for (ConsumerGroupDescription cgd : cGroups) {

      for (MemberDescription m : cgd.getMembers()) {

        List<String> topics = m.getAssignment().getTopicPartitions().stream()
          .filter(distinctByKey(tp -> tp.getTopic()))
          .map(tp -> tp.getTopic())
          .collect(Collectors.toList());

        for (String topic : topics) {

          consumers.merge(topic, 1, Integer::sum);
        }
      }

    }

    assertEquals(3, (int)consumers.get("my-topic"));
    assertEquals(2, (int)consumers.get("your-topic"));
    assertEquals(2, (int)consumers.get("his-topic"));
  }
  @Test
  public void testDescribeCluster(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to start consumer
    vertx.setTimer(1000, t -> {

      adminClient.describeCluster(ctx.asyncAssertSuccess(cluster -> {
        ctx.assertNotNull(cluster.getClusterId());
        Node controller = cluster.getController();
        ctx.assertNotNull(controller);
        ctx.assertEquals(1, controller.getId());
        ctx.assertEquals("localhost", controller.getHost());
        ctx.assertEquals(false, controller.hasRack());
        ctx.assertEquals("1", controller.getIdString());
        ctx.assertEquals(false, controller.isEmpty());
        ctx.assertEquals(9092, controller.getPort());
        ctx.assertEquals(null, controller.rack());
        Collection<Node> nodes = cluster.getNodes();
        ctx.assertNotNull(nodes);
        ctx.assertEquals(2, nodes.size());
        ctx.assertEquals(1, nodes.iterator().next().getId());
        adminClient.close();
        async.complete();
      }));

    });
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  @Test
  public void testListConsumerGroupOffsets(TestContext ctx) throws InterruptedException {

    final String topicName = "offsets-topic";
    kafkaCluster.createTopic(topicName, 2, 1);

    Async producerAsync = ctx.async();
    kafkaCluster.useTo().produceIntegers(topicName, 6, 1, producerAsync::complete);
    producerAsync.awaitSuccess(10000);

    final String groupId = "group-id";
    final String clientId = "client-id";
    final AtomicInteger counter = new AtomicInteger();
    final OffsetCommitCallback offsetCommitCallback = new OffsetCommitCallback() {
      @Override
      public void onComplete(Map<org.apache.kafka.common.TopicPartition, OffsetAndMetadata> map, Exception e) {
      }
    };
    final Async consumerAsync = ctx.async();

    kafkaCluster.useTo().consume(groupId, clientId, OffsetResetStrategy.EARLIEST, new StringDeserializer(), new IntegerDeserializer(),
      () -> counter.get() < 6, offsetCommitCallback, consumerAsync::complete, Collections.singletonList(topicName),
      record -> { counter.incrementAndGet(); });
    consumerAsync.awaitSuccess(10000);

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final Async async = ctx.async();

    adminClient.listConsumerGroupOffsets(groupId, ctx.asyncAssertSuccess(consumerGroupOffsets -> {

      TopicPartition topicPartition0 = new TopicPartition().setTopic(topicName).setPartition(0);
      TopicPartition topicPartition1 = new TopicPartition().setTopic(topicName).setPartition(0);
      ctx.assertEquals(2, consumerGroupOffsets.size());
      ctx.assertTrue(consumerGroupOffsets.containsKey(topicPartition0));
      ctx.assertEquals(3L, consumerGroupOffsets.get(topicPartition0).getOffset());
      ctx.assertTrue(consumerGroupOffsets.containsKey(topicPartition1));
      ctx.assertEquals(3L, consumerGroupOffsets.get(topicPartition1).getOffset());

      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testDeleteConsumerGroupOffsets(TestContext ctx) throws InterruptedException {

    final String topicName = "delete-offsets";
    kafkaCluster.createTopic(topicName, 2, 1);

    Async producerAsync = ctx.async();
    kafkaCluster.useTo().produceIntegers(topicName, 6, 1, producerAsync::complete);
    producerAsync.awaitSuccess(10000);

    final String groupId = "group-id";
    final String clientId = "client-id";
    final AtomicInteger counter = new AtomicInteger();
    final OffsetCommitCallback offsetCommitCallback = new OffsetCommitCallback() {
      @Override
      public void onComplete(Map<org.apache.kafka.common.TopicPartition, OffsetAndMetadata> map, Exception e) {
      }
    };
    final Async consumerAsync = ctx.async();

    kafkaCluster.useTo().consume(groupId, clientId, OffsetResetStrategy.EARLIEST, new StringDeserializer(), new IntegerDeserializer(),
      () -> counter.get() < 6, offsetCommitCallback, consumerAsync::complete, Collections.singletonList(topicName),
      record -> { counter.incrementAndGet(); });
    consumerAsync.awaitSuccess(10000);

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final Async async = ctx.async();

    final TopicPartition topicPartition0 = new TopicPartition().setTopic(topicName).setPartition(0);
    adminClient.deleteConsumerGroupOffsets(groupId, Collections.singleton(topicPartition0), ctx.asyncAssertSuccess(v -> {

      adminClient.listConsumerGroupOffsets(groupId, ctx.asyncAssertSuccess(consumerGroupOffsets -> {

        final TopicPartition topicPartition1 = new TopicPartition().setTopic(topicName).setPartition(1);
        ctx.assertTrue(consumerGroupOffsets.containsKey(topicPartition1));
        ctx.assertEquals(3L, consumerGroupOffsets.get(topicPartition1).getOffset());

        adminClient.close();
        async.complete();
      }));

    }));
  }

  @Test
  public void testAlterConsumerGroupOffsets(TestContext ctx) throws InterruptedException {

    final String topicName = "alter-offsets";
    kafkaCluster.createTopic(topicName, 2, 1);

    Async producerAsync = ctx.async();
    kafkaCluster.useTo().produceIntegers(topicName, 6, 1, producerAsync::complete);
    producerAsync.awaitSuccess(10000);

    final String groupId = "group-id";
    final String clientId = "client-id";
    final AtomicInteger counter = new AtomicInteger();
    final OffsetCommitCallback offsetCommitCallback = new OffsetCommitCallback() {
      @Override
      public void onComplete(Map<org.apache.kafka.common.TopicPartition, OffsetAndMetadata> map, Exception e) {
      }
    };
    final Async consumerAsync = ctx.async();

    kafkaCluster.useTo().consume(groupId, clientId, OffsetResetStrategy.EARLIEST, new StringDeserializer(), new IntegerDeserializer(),
            () -> counter.get() < 6, offsetCommitCallback, consumerAsync::complete, Collections.singletonList(topicName),
            record -> { counter.incrementAndGet(); });
    consumerAsync.awaitSuccess(10000);

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final Async async = ctx.async();

    final TopicPartition topicPartition0 = new TopicPartition().setTopic(topicName).setPartition(0);
    long newOffset = 42L;
    String newMetadata = "vertx-rulezz";

    io.vertx.kafka.client.consumer.OffsetAndMetadata oam = new io.vertx.kafka.client.consumer.OffsetAndMetadata(newOffset, newMetadata);
    adminClient.alterConsumerGroupOffsets(groupId, Collections.singletonMap(topicPartition0, oam), ctx.asyncAssertSuccess(v -> {
      adminClient.listConsumerGroupOffsets(groupId, ctx.asyncAssertSuccess(consumerGroupOffsets -> {

        final TopicPartition topicPartition1 = new TopicPartition().setTopic(topicName).setPartition(0);
        ctx.assertTrue(consumerGroupOffsets.containsKey(topicPartition1));
        ctx.assertEquals(newOffset, consumerGroupOffsets.get(topicPartition1).getOffset());
        ctx.assertEquals(newMetadata, consumerGroupOffsets.get(topicPartition1).getMetadata());

        adminClient.close();
        async.complete();
      }));
    }));
  }

  @Test
  public void testListOffsets(TestContext ctx) {
    final String topicName = "list-offsets-topic";
    kafkaCluster.createTopic(topicName, 1, 1);

    Async producerAsync = ctx.async();
    kafkaCluster.useTo().produceIntegers(topicName, 6, 1, producerAsync::complete);
    producerAsync.awaitSuccess(10000);

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final TopicPartition topicPartition0 = new TopicPartition().setTopic(topicName).setPartition(0);

    // BeginOffsets
    Map<TopicPartition, OffsetSpec> topicPartitionBeginOffsets = Collections.singletonMap(topicPartition0, OffsetSpec.EARLIEST);
    final Async async1 = ctx.async();
    adminClient.listOffsets(topicPartitionBeginOffsets, ctx.asyncAssertSuccess(listOffsets -> {
      ListOffsetsResultInfo offsets = listOffsets.get(topicPartition0);
      ctx.assertNotNull(offsets);
      ctx.assertEquals(0L, offsets.getOffset());
      async1.complete();
    }));

    // EndOffsets
    Map<TopicPartition, OffsetSpec> topicPartitionEndOffsets = Collections.singletonMap(topicPartition0, OffsetSpec.LATEST);
    final Async async2 = ctx.async();
    adminClient.listOffsets(topicPartitionEndOffsets, ctx.asyncAssertSuccess(listOffsets -> {
      ListOffsetsResultInfo offsets = listOffsets.get(topicPartition0);
      ctx.assertNotNull(offsets);
      ctx.assertEquals(6L, offsets.getOffset());
      adminClient.close();
      async2.complete();
    }));
  }

  @Test
  public void testListOffsetsNoTopic(TestContext ctx) {
    final String topicName = "list-offsets-notopic";

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final TopicPartition topicPartition0 = new TopicPartition().setTopic(topicName).setPartition(0);

    // BeginOffsets of a non existent topic-partition
    Map<TopicPartition, OffsetSpec> topicPartitionBeginOffsets = Collections.singletonMap(topicPartition0, OffsetSpec.EARLIEST);
    adminClient.listOffsets(topicPartitionBeginOffsets, ctx.asyncAssertFailure());
  }

  @Test
  public void testCreateNewPartitionInTopic(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    kafkaCluster.createTopic("testCreateNewPartitionInTopic", 1, 1);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {

      adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {

        ctx.assertTrue(topics.contains("testCreateNewPartitionInTopic"));

        adminClient.createPartitions(Collections.singletonMap("testCreateNewPartitionInTopic", new NewPartitions(3, null)), ctx.asyncAssertSuccess(v -> {
          adminClient.describeTopics(Collections.singletonList("testCreateNewPartitionInTopic"), ctx.asyncAssertSuccess(s -> {
            ctx.assertTrue(s.get("testCreateNewPartitionInTopic").getPartitions().size() == 3);
            adminClient.close();
            async.complete();
          }));
        }));
      }));
    });
  }

  @Test
  public void testDecreasePartitionInTopic(TestContext ctx) {

    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    kafkaCluster.createTopic("testDecreasePartitionInTopic", 3, 1);

    Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {

      adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {

        ctx.assertTrue(topics.contains("testDecreasePartitionInTopic"));

        adminClient.createPartitions(Collections.singletonMap("testDecreasePartitionInTopic", new NewPartitions(1, null)), ctx.asyncAssertFailure(v -> {
          ctx.assertTrue(v.getMessage().equals("Topic currently has 3 partitions, which is higher than the requested 1."));
          adminClient.close();
          async.complete();
        }));
      }));
    });
  }



  @Test
  public void testCreatePartitionInTopicWithAssignment(TestContext ctx) throws IOException {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);

    kafkaCluster.createTopic("testCreatePartitionInTopicWithAssignment", 1, 1);

   Async async = ctx.async();

    // timer because, Kafka cluster takes time to create topics
    vertx.setTimer(1000, t -> {

      adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {

        ctx.assertTrue(topics.contains("testCreatePartitionInTopicWithAssignment"));

        List sublist1 = new ArrayList<Integer>();
        sublist1.add(2);

        List sublist2 = new ArrayList<Integer>();
        sublist2.add(1);

        List assigmnments = new ArrayList<List<Integer>>();
        assigmnments.add(sublist1);
        assigmnments.add(sublist2);

        adminClient.createPartitions(Collections.singletonMap("testCreatePartitionInTopicWithAssignment", new NewPartitions(3, assigmnments)), ctx.asyncAssertSuccess(v -> {
          adminClient.describeTopics(Collections.singletonList("testCreatePartitionInTopicWithAssignment"), ctx.asyncAssertSuccess(s -> {
            ctx.assertTrue(s.get("testCreatePartitionInTopicWithAssignment").getPartitions().size() == 3);
            ctx.assertTrue(s.get("testCreatePartitionInTopicWithAssignment").getPartitions().get(1).getReplicas().get(0).getId() == 2);
            ctx.assertTrue(s.get("testCreatePartitionInTopicWithAssignment").getPartitions().get(2).getReplicas().get(0).getId() == 1);
            adminClient.close();
            async.complete();
          }));
        }));
      }));
    });
  }

  @Test
  public void testAsyncClose(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {
      adminClient.close(ctx.asyncAssertSuccess());
    }));
  }

  @Test
  public void testDeleteRecords(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    TopicPartition partition = new TopicPartition("testTopicForDelete", 0);
    AtomicInteger messagesCnt = new AtomicInteger();
    adminClient.createTopics(Collections.singletonList(new NewTopic("testTopicForDelete", 1, (short) 1)),
      ctx.asyncAssertSuccess(v -> kafkaCluster.useTo().produceIntegers("testTopicForDelete", 7, 1, () -> {
        adminClient.deleteRecords(Collections.singletonMap(partition, 5L),
          ctx.asyncAssertSuccess(r -> adminClient.listOffsets(Collections.singletonMap(partition, OffsetSpec.EARLIEST), ctx.asyncAssertSuccess(ar -> {
            ctx.assertEquals(ar.get(partition).getOffset(), 5L);
            adminClient.deleteTopics(Collections.singletonList("testTopicForDelete"), ctx.asyncAssertSuccess(v1 -> {
              async.complete();
              adminClient.close();
            }));
          }))));
      })));
  }

  @Test
  public void testAlterAndDescribeClientQuotas(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();

    ClientQuotaFilter clientQuotaFilter = ClientQuotaFilter.all();
    adminClient.describeClientQuotas(clientQuotaFilter, ctx.asyncAssertSuccess(quotas -> {
      ctx.assertEquals(quotas.size(), 0);
    }));

    adminClient.alterClientQuotas(Collections.singletonList(
      new ClientQuotaAlteration()
        .setEntity(new ClientQuotaEntity(Collections.singletonMap("user", "test")))
        .setOps(Collections.singletonList(new QuotaAlterationOperation("producer_byte_rate",Double.parseDouble("1024"))))), ctx.asyncAssertSuccess(v -> {
          adminClient.describeClientQuotas(ClientQuotaFilter.all(), ctx.asyncAssertSuccess(quotas -> {
            ctx.assertEquals(quotas.size(), 1);
            ctx.assertEquals(quotas,
              Collections.singletonMap(new ClientQuotaEntity(Collections.singletonMap("user","test")),
                Collections.singletonMap("producer_byte_rate",Double.parseDouble("1024"))));
          }));
    }));

    ClientQuotaFilter clientQuotaFilter2 = ClientQuotaFilter.contains(Collections.singletonList(new ClientQuotaFilterComponent("user","test")));
    adminClient.describeClientQuotas(clientQuotaFilter2, ctx.asyncAssertSuccess(quotas -> {
      ctx.assertEquals(quotas.size(), 1);
      ctx.assertEquals(quotas,
        Collections.singletonMap(new ClientQuotaEntity(Collections.singletonMap("user","test")),
          Collections.singletonMap("producer_byte_rate",Double.parseDouble("1024"))));
      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testDescribeLogDirs(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();

    vertx.setTimer(1000, t -> {
      adminClient.describeLogDirs(Stream.of(1, 2).collect(Collectors.toList()), ctx.asyncAssertSuccess(map -> {
        ctx.assertEquals(map.size(), 2);


        List<LogDirInfo> infosBroker1 = map.get(1);
        ctx.assertNotNull(infosBroker1);
        ctx.assertEquals(infosBroker1.size(), 1);
        ctx.assertTrue(infosBroker1.get(0).getPath().endsWith("/data/cluster/kafka/broker1"));

        List<LogDirInfo> infosBroker2 = map.get(2);
        ctx.assertNotNull(infosBroker2);
        ctx.assertEquals(infosBroker2.size(), 1);
        ctx.assertTrue(infosBroker2.get(0).getPath().endsWith("/data/cluster/kafka/broker2"));

        ctx.assertEquals(
          (int) infosBroker1.get(0).getReplicaInfos().stream()
            .filter(f -> topics.contains(f.getTopicPartition().topic()))
            .count()
            +
            (int) infosBroker2.get(0).getReplicaInfos().stream()
              .filter(f -> topics.contains(f.getTopicPartition().topic()))
              .count(), 2
        );

        adminClient.close();
        async.complete();
      }));
    });
  }

  @Test
  public void testDescribeLogDirsSeveralPartitions(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.createTopics(Collections.singletonList(new NewTopic("testDescribeTopic", 4, (short) 1)),
      ctx.asyncAssertSuccess(v -> {
        adminClient.describeLogDirs(Stream.of(1, 2).collect(Collectors.toList()), ctx.asyncAssertSuccess(map -> {
          List<LogDirInfo> infosBroker1 = map.get(1);
          List<LogDirInfo> infosBroker2 = map.get(2);

          ctx.assertNotNull(infosBroker1);
          ctx.assertEquals(infosBroker1.size(), 1);
          ctx.assertNotNull(infosBroker2);
          ctx.assertEquals(infosBroker2.size(), 1);

          ctx.assertEquals(
            (int) infosBroker1.get(0).getReplicaInfos().stream()
              .filter(f -> f.getTopicPartition().topic().equals("testDescribeTopic"))
              .count()
              +
              (int) infosBroker2.get(0).getReplicaInfos().stream()
                .filter(f -> f.getTopicPartition().topic().equals("testDescribeTopic"))
                .count()
            , 4);

          adminClient.deleteTopics(Collections.singletonList("testDescribeTopic"), ctx.asyncAssertSuccess(v1 -> {
            async.complete();
            adminClient.close();
          }));
        }));
      }));
  }

  @Test
  public void testDescribeLogDirsWithMessages(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.createTopics(Collections.singletonList(new NewTopic("testDescribeTopicWithMs", 1, (short) 1)),
      ctx.asyncAssertSuccess(v -> {
        kafkaCluster.useTo().produceIntegers("testDescribeTopicWithMs", 6, 1, () -> {
          adminClient.describeLogDirs(Stream.of(1, 2).collect(Collectors.toList()), ctx.asyncAssertSuccess(map -> {
            List<LogDirInfo> infosBroker1 = map.get(1);
            List<LogDirInfo> infosBroker2 = map.get(2);

            ctx.assertNotNull(infosBroker1);
            ctx.assertEquals(infosBroker1.size(), 1);
            ctx.assertNotNull(infosBroker2);
            ctx.assertEquals(infosBroker2.size(), 1);

            ctx.assertTrue(
              infosBroker1.get(0).getReplicaInfos().stream()
                .filter(f -> f.getTopicPartition().topic().equals("testDescribeTopicWithMs"))
                .map(i -> i.getReplicaInfo().getSize())
                .reduce(0L, Long::sum)
                +
                infosBroker2.get(0).getReplicaInfos().stream()
                  .filter(f -> f.getTopicPartition().topic().equals("testDescribeTopicWithMs"))
                  .map(i -> i.getReplicaInfo().getSize())
                  .reduce(0L, Long::sum)
                > 0L);

            adminClient.deleteTopics(Collections.singletonList("testDescribeTopicWithMs"), ctx.asyncAssertSuccess(v1 -> {
              async.complete();
              adminClient.close();
            }));
          }));
        });
      }));
  }

  @Test
  public void testElectLeaders(TestContext ctx) {
    final String topicName = "elect-leaders";
    kafkaCluster.createTopic(topicName, 1, 1);

    final KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    final Async async = ctx.async();

    vertx.setTimer(1000, t ->
      adminClient.listTopics(ctx.asyncAssertSuccess(topics -> {
        ctx.assertTrue(topics.contains("elect-leaders"));
        adminClient.electLeaders(ElectionType.PREFERRED,
          Collections.singleton(new TopicPartition(topicName, 0)),
          ctx.asyncAssertFailure(v -> {
            ctx.assertTrue(v instanceof ElectionNotNeededException);
            ctx.assertTrue(v.getMessage().equals("Leader election not needed for topic partition."));
            adminClient.close();
            async.complete();
          }));
      })));
  }

  @Test
  public void testRemoveMembersFromConsumerGroup(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();

    Properties props = new Properties();
    props.setProperty("bootstrap.servers", kafkaCluster.brokerList());
    props.setProperty("group.id", "remove-group");
    props.setProperty("enable.auto.commit", Boolean.FALSE.toString());
    props.setProperty("auto.offset.reset", OffsetResetStrategy.EARLIEST.toString().toLowerCase());
    props.setProperty("client.id", "remove-group-client");
    props.setProperty("group.instance.id","consumer-to-delete");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props, new StringDeserializer(), new StringDeserializer());
    consumer.subscribe(Collections.singletonList("remove-consumer-topic"));
    consumer.poll(Duration.ofMillis(1000));
    vertx.setTimer(1000, t -> {
      adminClient.describeConsumerGroups(Collections.singletonList("remove-group"), ctx.asyncAssertSuccess(groups -> {
        ctx.assertEquals(groups.get("remove-group").getMembers().size(), 1);
        adminClient.removeMembersFromConsumerGroup("remove-group",
          new RemoveMembersFromConsumerGroupOptions(Collections.singleton(new MemberToRemove("consumer-to-delete")))
          , members -> adminClient.describeConsumerGroups(Collections.singletonList("remove-group"), ctx.asyncAssertSuccess(groups2 -> {
            ctx.assertEquals(groups2.get("remove-group").getMembers().size(), 0);
            adminClient.close();
            async.complete();
          })));
      }));
    });
  }

  @Test
  public void testDescribeFeatures(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.describeFeatures(ctx.asyncAssertSuccess(features -> {
      ctx.assertEquals(features, new FeatureMetadata(new HashMap<>(), 0, new HashMap<>()));
      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testUpdateFeatures(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    HashMap<String, FeatureUpdate> featureUpdateHashMap = new HashMap<>();
    featureUpdateHashMap.put("test", new FeatureUpdate((short) 1, false));
    adminClient.updateFeatures(featureUpdateHashMap, ctx.asyncAssertFailure(v -> {
      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testListAllPartitionReassignments(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.listPartitionReassignments(ctx.asyncAssertSuccess(r -> {
      ctx.assertEquals(r.size(), 0);
      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testListOnePartitionReassignments(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.listPartitionReassignments(Collections.singleton(new TopicPartition("test", 1)), ctx.asyncAssertSuccess(r -> {
      ctx.assertEquals(r.size(), 0);
      adminClient.close();
      async.complete();
    }));
  }

  @Test
  public void testAlterPartitionReassignments(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.createTopics(Collections.singletonList(
        new NewTopic("alter_partition_reassignment_topic", 2, (short) 1)),
      ctx.asyncAssertSuccess(t -> adminClient.describeTopics(
        Collections.singletonList("alter_partition_reassignment_topic"), ctx.asyncAssertSuccess(d -> {
          int currentReplica = d.get("alter_partition_reassignment_topic").getPartitions().get(0).getReplicas().get(0).getId();
          int newReplica = currentReplica == 1 ? 2 : 1;
          adminClient.alterPartitionReassignments(Collections.singletonMap(new TopicPartition("alter_partition_reassignment_topic", 0),
            new NewPartitionReassignment(Stream.of(1, 2).collect(Collectors.toList()))), ctx.asyncAssertSuccess(r -> {
            adminClient.listPartitionReassignments(ctx.asyncAssertSuccess(d2 -> {
              ctx.assertEquals(d2.size(), 1);
              ctx.assertEquals(d2, Collections.singletonMap(
                new TopicPartition("alter_partition_reassignment_topic", 0),
                new PartitionReassignment(Stream.of(1, 2).collect(Collectors.toList()), Collections.singletonList(newReplica), new ArrayList<>())
              ));
              adminClient.deleteTopics(Collections.singletonList("alter_partition_reassignment_topic"), ctx.asyncAssertSuccess(e -> {
                adminClient.close();
                async.complete();
              }));
            }));
          }));
        }))));
  }

  @Test
  public void testDescribeReplicaLogDirs(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.createTopics(Collections.singletonList(new NewTopic("describe_replica_logdirs_topic", 1, (short) 1)),
      ctx.asyncAssertSuccess(t -> adminClient.describeTopics(Collections.singletonList("describe_replica_logdirs_topic"), ctx.asyncAssertSuccess(d -> {
        int currentReplica = d.get("describe_replica_logdirs_topic").getPartitions().get(0).getReplicas().get(0).getId();
        int otherReplica = currentReplica == 1 ? 2 : 1;
        adminClient.describeReplicaLogDirs(Stream.of(
            new TopicPartitionReplica("describe_replica_logdirs_topic", 0, 1),
            new TopicPartitionReplica("describe_replica_logdirs_topic", 0, 2)
          ).collect(Collectors.toList()),
          ctx.asyncAssertSuccess(dr -> {
            ctx.assertNotNull(dr.get(new TopicPartitionReplica("describe_replica_logdirs_topic",0,currentReplica)).getCurrentReplicaLogDir());
            ctx.assertNull(dr.get(new TopicPartitionReplica("describe_replica_logdirs_topic",0,otherReplica)).getCurrentReplicaLogDir());
            adminClient.deleteTopics(Collections.singletonList("describe_replica_logdirs_topic"), ctx.asyncAssertSuccess(e -> {
              adminClient.close();
              async.complete();
            }));
          }));
      }))));
  }

  @Test
  public void testAlterReplicaLogDirs(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.createTopics(Collections.singletonList(new NewTopic("alter_replica_logdirs_topic", 1, (short) 1)),
      ctx.asyncAssertSuccess(t -> adminClient.describeTopics(Collections.singletonList("alter_replica_logdirs_topic"), ctx.asyncAssertSuccess(d -> {
        int currentReplica = d.get("alter_replica_logdirs_topic").getPartitions().get(0).getReplicas().get(0).getId();
        int otherReplica = currentReplica == 1 ? 2 : 1;
        adminClient.describeReplicaLogDirs(Stream.of(
            new TopicPartitionReplica("alter_replica_logdirs_topic", 0, 1),
            new TopicPartitionReplica("alter_replica_logdirs_topic", 0, 2)
          ).collect(Collectors.toList()),
          ctx.asyncAssertSuccess(dr -> {
            String currentReplicaLogDir = dr.get(new TopicPartitionReplica("alter_replica_logdirs_topic", 0, currentReplica)).getCurrentReplicaLogDir();
            String newReplicaLogDir = currentReplicaLogDir.substring(0, currentReplicaLogDir.length() - 1) + currentReplica;
            ctx.assertNotNull(currentReplicaLogDir);
            ctx.assertNull(dr.get(new TopicPartitionReplica("alter_replica_logdirs_topic", 0, otherReplica)).getCurrentReplicaLogDir());
            adminClient.alterReplicaLogDirs(Collections.singletonMap(new TopicPartitionReplica("alter_replica_logdirs_topic", 0, currentReplica), newReplicaLogDir),
              ctx.asyncAssertSuccess(al -> adminClient.describeReplicaLogDirs(Stream.of(
                  new TopicPartitionReplica("alter_replica_logdirs_topic", 0, 1),
                  new TopicPartitionReplica("alter_replica_logdirs_topic", 0, 2)
                ).collect(Collectors.toList()), ctx.asyncAssertSuccess(dr2 -> {
                  ctx.assertNotNull(dr2.get(new TopicPartitionReplica("alter_replica_logdirs_topic", 0, currentReplica)).getCurrentReplicaLogDir());
                  ctx.assertNull(dr2.get(new TopicPartitionReplica("alter_replica_logdirs_topic", 0, otherReplica)).getCurrentReplicaLogDir());
                  adminClient.deleteTopics(Collections.singletonList("alter_replica_logdirs_topic"), ctx.asyncAssertSuccess(e -> {
                    adminClient.close();
                    async.complete();
                  }));
                }))
              ));

          }));
      }))));
  }

  @Test
  public void testUnregisterBroker(TestContext ctx) {
    KafkaAdminClient adminClient = KafkaAdminClient.create(this.vertx, config);
    Async async = ctx.async();
    adminClient.unregisterBroker(1, ctx.asyncAssertFailure(v -> {
      /*
       * It is supported only on Kafka clusters which use Raft to store metadata, rather than ZooKeeper
       * */
      adminClient.close();
      async.complete();
    }));
  }
}
