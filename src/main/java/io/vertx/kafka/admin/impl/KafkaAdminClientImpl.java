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

package io.vertx.kafka.admin.impl;

import io.vertx.core.*;
import io.vertx.core.impl.ContextInternal;
import io.vertx.kafka.admin.Config;
import io.vertx.kafka.admin.ConsumerGroupDescription;
import io.vertx.kafka.admin.ConsumerGroupListing;
import io.vertx.kafka.admin.FeatureMetadata;
import io.vertx.kafka.admin.FeatureUpdate;
import io.vertx.kafka.admin.KafkaAdminClient;
import io.vertx.kafka.admin.ListConsumerGroupOffsetsOptions;
import io.vertx.kafka.admin.MemberDescription;
import io.vertx.kafka.admin.NewPartitionReassignment;
import io.vertx.kafka.admin.NewPartitions;
import io.vertx.kafka.admin.NewTopic;
import io.vertx.kafka.admin.OffsetSpec;
import io.vertx.kafka.admin.PartitionReassignment;
import io.vertx.kafka.admin.RemoveMembersFromConsumerGroupOptions;
import io.vertx.kafka.admin.TopicDescription;
import io.vertx.kafka.admin.*;
import io.vertx.kafka.client.common.*;
import io.vertx.kafka.client.common.acl.AclBinding;
import io.vertx.kafka.client.common.acl.AclBindingFilter;
import io.vertx.kafka.client.common.impl.Helper;
import io.vertx.kafka.client.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KafkaAdminClientImpl implements KafkaAdminClient {

  private Vertx vertx;
  private AdminClient adminClient;

  public KafkaAdminClientImpl(Vertx vertx, AdminClient adminClient) {
    this.vertx = vertx;
    this.adminClient = adminClient;
  }

  @Override
  public void describeTopics(List<String> topicNames, Handler<AsyncResult<Map<String, TopicDescription>>> completionHandler) {
    describeTopics(topicNames).onComplete(completionHandler);
  }

  @Override
  public Future<Map<String, TopicDescription>> describeTopics(List<String> topicNames) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<String, TopicDescription>> promise = ctx.promise();

    DescribeTopicsResult describeTopicsResult = this.adminClient.describeTopics(topicNames);
    describeTopicsResult.all().whenComplete((t, ex) -> {
      if (ex == null) {

        Map<String, TopicDescription> topics = new HashMap<>();

        for (Map.Entry<String, org.apache.kafka.clients.admin.TopicDescription> topicDescriptionEntry : t.entrySet()) {

          List<TopicPartitionInfo> partitions = new ArrayList<>();

          for (org.apache.kafka.common.TopicPartitionInfo kafkaPartitionInfo : topicDescriptionEntry.getValue().partitions()) {

            TopicPartitionInfo topicPartitionInfo = new TopicPartitionInfo();
            topicPartitionInfo.setIsr(
              kafkaPartitionInfo.isr().stream().map(Helper::from).collect(Collectors.toList()))
              .setLeader(Helper.from(kafkaPartitionInfo.leader()))
              .setPartition(kafkaPartitionInfo.partition())
              .setReplicas(
                kafkaPartitionInfo.replicas().stream().map(Helper::from).collect(Collectors.toList()));

            partitions.add(topicPartitionInfo);
          }

          TopicDescription topicDescription = new TopicDescription();

          topicDescription.setInternal(topicDescriptionEntry.getValue().isInternal())
            .setName(topicDescriptionEntry.getKey())
            .setPartitions(partitions);

          topics.put(topicDescriptionEntry.getKey(), topicDescription);
        }

        promise.complete(topics);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void listTopics(Handler<AsyncResult<Set<String>>> completionHandler) {
    listTopics().onComplete(completionHandler);
  }

  @Override
  public Future<Set<String>> listTopics() {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Set<String>> promise = ctx.promise();

    ListTopicsResult listTopicsResult = this.adminClient.listTopics();
    listTopicsResult.names().whenComplete((topics, ex) -> {
      if (ex == null) {
        promise.complete(topics);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void createTopics(List<NewTopic> topics, Handler<AsyncResult<Void>> completionHandler) {
    createTopics(topics).onComplete(completionHandler);
  }

  @Override
  public Future<Void> createTopics(List<NewTopic> topics) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    CreateTopicsResult createTopicsResult = this.adminClient.createTopics(Helper.toNewTopicList(topics));
    createTopicsResult.all().whenComplete((v, ex) -> {

      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void deleteTopics(List<String> topicNames, Handler<AsyncResult<Void>> completionHandler) {
    deleteTopics(topicNames).onComplete(completionHandler);
  }

  @Override
  public Future<Void> deleteTopics(List<String> topicNames) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    DeleteTopicsResult deleteTopicsResult = this.adminClient.deleteTopics(topicNames);
    deleteTopicsResult.all().whenComplete((v, ex) -> {

      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void createPartitions(Map<String, NewPartitions> partitions, Handler<AsyncResult<Void>> completionHandler) {
    createPartitions(partitions).onComplete(completionHandler);
  }

  @Override
  public Future<Void> createPartitions(Map<String, NewPartitions> partitions) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    CreatePartitionsResult createPartitionsResult = this.adminClient.createPartitions(Helper.toPartitions(partitions));
    createPartitionsResult.all().whenComplete((v, ex) -> {

      if (ex == null) {
        promise.handle(Future.succeededFuture());
      } else {
        promise.handle(Future.failedFuture(ex));
      }
    });
    return promise.future();
  }


  @Override
  public void describeConfigs(List<ConfigResource> configResources, Handler<AsyncResult<Map<ConfigResource, Config>>> completionHandler) {
    describeConfigs(configResources).onComplete(completionHandler);
  }

  @Override
  public Future<Map<ConfigResource, Config>> describeConfigs(List<ConfigResource> configResources) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<ConfigResource, Config>> promise = ctx.promise();

    DescribeConfigsResult describeConfigsResult = this.adminClient.describeConfigs(Helper.toConfigResourceList(configResources));
    describeConfigsResult.all().whenComplete((m, ex) -> {

      if (ex == null) {

        Map<ConfigResource, Config> configs = new HashMap<>();

        for (Map.Entry<org.apache.kafka.common.config.ConfigResource, org.apache.kafka.clients.admin.Config> configEntry : m.entrySet()) {

          ConfigResource configResource = Helper.from(configEntry.getKey());
          Config config = Helper.from(configEntry.getValue());

          configs.put(configResource, config);
        }

        promise.complete(configs);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void alterConfigs(Map<ConfigResource,Config> configs, Handler<AsyncResult<Void>> completionHandler) {
    alterConfigs(configs).onComplete(completionHandler);
  }

  @Override
  public Future<Void> alterConfigs(Map<ConfigResource, Config> configs) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    AlterConfigsResult alterConfigsResult = this.adminClient.incrementalAlterConfigs(Helper.toConfigMaps(configs));
    alterConfigsResult.all().whenComplete((v, ex) -> {

      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void listConsumerGroups(Handler<AsyncResult<List<ConsumerGroupListing>>> completionHandler) {
    listConsumerGroups().onComplete(completionHandler);
  }

  @Override
  public Future<List<ConsumerGroupListing>> listConsumerGroups() {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<List<ConsumerGroupListing>> promise = ctx.promise();

    ListConsumerGroupsResult listConsumerGroupsResult = this.adminClient.listConsumerGroups();
    listConsumerGroupsResult.all().whenComplete((groupIds, ex) -> {

      if (ex == null) {
        promise.complete(Helper.fromConsumerGroupListings(groupIds));
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void describeConsumerGroups(List<java.lang.String> groupIds, Handler<AsyncResult<Map<String, ConsumerGroupDescription>>> completionHandler) {
    describeConsumerGroups(groupIds).onComplete(completionHandler);
  }

  @Override
  public Future<Map<String, ConsumerGroupDescription>> describeConsumerGroups(List<String> groupIds) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<String, ConsumerGroupDescription>> promise = ctx.promise();

    DescribeConsumerGroupsResult describeConsumerGroupsResult = this.adminClient.describeConsumerGroups(groupIds);
    describeConsumerGroupsResult.all().whenComplete((cg, ex) -> {
      if (ex == null) {
        Map<String, ConsumerGroupDescription> consumerGroups = new HashMap<>();

        for (Map.Entry<String, org.apache.kafka.clients.admin.ConsumerGroupDescription> cgDescriptionEntry: cg.entrySet()) {
          List<MemberDescription> members = new ArrayList<>();

          for (org.apache.kafka.clients.admin.MemberDescription memberDescription : cgDescriptionEntry.getValue().members()) {
            MemberDescription m = new MemberDescription();
            m.setConsumerId(memberDescription.consumerId())
              .setClientId(memberDescription.clientId())
              .setAssignment(Helper.from(memberDescription.assignment()))
              .setHost(memberDescription.host());

            members.add(m);
          }

          ConsumerGroupDescription consumerGroupDescription = new ConsumerGroupDescription();

          consumerGroupDescription.setGroupId(cgDescriptionEntry.getValue().groupId())
            .setCoordinator(Helper.from(cgDescriptionEntry.getValue().coordinator()))
            .setMembers(members)
            .setPartitionAssignor(cgDescriptionEntry.getValue().partitionAssignor())
            .setSimpleConsumerGroup(cgDescriptionEntry.getValue().isSimpleConsumerGroup())
            .setState(cgDescriptionEntry.getValue().state());

          consumerGroups.put(cgDescriptionEntry.getKey(), consumerGroupDescription);
        }
        promise.complete(consumerGroups);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  public void listConsumerGroupOffsets(String groupId, ListConsumerGroupOffsetsOptions options, Handler<AsyncResult<Map<TopicPartition, OffsetAndMetadata>>> completionHandler) {
    listConsumerGroupOffsets(groupId, options).onComplete(completionHandler);
  }

  public Future<Map<TopicPartition, OffsetAndMetadata>> listConsumerGroupOffsets(String groupId, ListConsumerGroupOffsetsOptions options) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<TopicPartition, OffsetAndMetadata>> promise = ctx.promise();

    ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = this.adminClient.listConsumerGroupOffsets(groupId, Helper.to(options));
    listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().whenComplete((cgo, ex) -> {

      if (ex == null) {
        Map<TopicPartition, OffsetAndMetadata> consumerGroupOffsets = new HashMap<>();

        for (Map.Entry<org.apache.kafka.common.TopicPartition, org.apache.kafka.clients.consumer.OffsetAndMetadata> cgoOffset : cgo.entrySet()) {
          consumerGroupOffsets.put(Helper.from(cgoOffset.getKey()), Helper.from(cgoOffset.getValue()));
        }
        promise.complete(consumerGroupOffsets);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void deleteConsumerGroups(List<String> groupIds, Handler<AsyncResult<Void>> completionHandler) {
    deleteConsumerGroups(groupIds).onComplete(completionHandler);
  }

  @Override
  public Future<Void> deleteConsumerGroups(List<String> groupIds) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    DeleteConsumerGroupsResult deleteConsumerGroupsResult = this.adminClient.deleteConsumerGroups(groupIds);
    deleteConsumerGroupsResult.all().whenComplete((v, ex) -> {
      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void alterConsumerGroupOffsets(String groupId, Map<TopicPartition, OffsetAndMetadata> offsets, Handler<AsyncResult<Void>> completionHandler) {
    alterConsumerGroupOffsets(groupId, offsets).onComplete(completionHandler);
  }

  @Override
  public Future<Void> alterConsumerGroupOffsets(String groupId, Map<TopicPartition, OffsetAndMetadata> offsets) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    AlterConsumerGroupOffsetsResult alterConsumerGroupOffsetsResult = this.adminClient.alterConsumerGroupOffsets(groupId, Helper.to(offsets));
    alterConsumerGroupOffsetsResult.all().whenComplete((v, ex) -> {
      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }


  @Override
  public void deleteConsumerGroupOffsets(String groupId, Set<TopicPartition> partitions, Handler<AsyncResult<Void>> completionHandler) {
    deleteConsumerGroupOffsets(groupId, partitions).onComplete(completionHandler);
  }

  @Override
  public Future<Void> deleteConsumerGroupOffsets(String groupId, Set<TopicPartition> partitions) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();

    DeleteConsumerGroupOffsetsResult deleteConsumerGroupOffsetsResult = this.adminClient.deleteConsumerGroupOffsets(groupId, Helper.toTopicPartitionSet(partitions));
    deleteConsumerGroupOffsetsResult.all().whenComplete((v, ex) -> {
      if (ex == null) {
        promise.complete();
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void describeCluster(Handler<AsyncResult<ClusterDescription>> completionHandler) {
    describeCluster().onComplete(completionHandler);
  }

  @Override
  public Future<ClusterDescription> describeCluster() {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<ClusterDescription> promise = ctx.promise();

    DescribeClusterResult describeClusterResult = this.adminClient.describeCluster();
    KafkaFuture.allOf(describeClusterResult.clusterId(), describeClusterResult.controller(), describeClusterResult.nodes()).whenComplete((r, ex) -> {
      if (ex == null) {
        try {
          String clusterId = describeClusterResult.clusterId().get();
          org.apache.kafka.common.Node rcontroller = describeClusterResult.controller().get();
          Collection<org.apache.kafka.common.Node> rnodes = describeClusterResult.nodes().get();

          Node controller = Helper.from(rcontroller);
          List<Node> nodes = new ArrayList<>();
          rnodes.forEach(rnode -> {
            nodes.add(Helper.from(rnode));
          });
          ClusterDescription clusterDescription = new ClusterDescription(clusterId, controller, nodes);
          promise.complete(clusterDescription);
        } catch (InterruptedException|ExecutionException e) {
          promise.fail(e);
          Thread.currentThread().interrupt();
        }
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  public void listOffsets(Map<TopicPartition, OffsetSpec> topicPartitionOffsets, Handler<AsyncResult<Map<TopicPartition, ListOffsetsResultInfo>>> completionHandler) {
    listOffsets(topicPartitionOffsets).onComplete(completionHandler);
  }

  @Override
  public Future<Map<TopicPartition, ListOffsetsResultInfo>> listOffsets(Map<TopicPartition, OffsetSpec> topicPartitionOffsets) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<TopicPartition, ListOffsetsResultInfo>> promise = ctx.promise();

    ListOffsetsResult listOffsetsResult = this.adminClient.listOffsets(Helper.toTopicPartitionOffsets(topicPartitionOffsets));
    listOffsetsResult.all().whenComplete((o, ex) -> {
      if (ex == null) {
        Map<TopicPartition, ListOffsetsResultInfo> listOffsets = new HashMap<>();

        for (Map.Entry<org.apache.kafka.common.TopicPartition, org.apache.kafka.clients.admin.ListOffsetsResult.ListOffsetsResultInfo> oOffset : o.entrySet()) {
          listOffsets.put(Helper.from(oOffset.getKey()), Helper.from(oOffset.getValue()));
        }
        promise.complete(listOffsets);
      } else {
        promise.fail(ex);
      }
    });
    return promise.future();
  }

  @Override
  public void electLeaders(ElectionType electionType, Set<TopicPartition> partitions, Handler<AsyncResult<Void>> completionHandler) {
    electLeaders(electionType, partitions).onComplete(completionHandler);
  }

  @Override
  public Future<Void> electLeaders(ElectionType electionType, Set<TopicPartition> partitions) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    electLeadersInternal(electionType, partitions, promise);
    return promise.future();
  }

  private void electLeadersInternal(ElectionType electionType, Set<TopicPartition> partitions, Promise<Void> promise) {
    try {
      ElectLeadersResult electLeadersResult = this.adminClient.electLeaders(Helper.to(electionType), Helper.toTopicPartitionSet(partitions));
      electLeadersResult.all().whenComplete((p, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public Future<Void> close() {
    return close(0);
  }

  @Override
  public Future<Void> close(long timeout) {
    return vertx.executeBlocking(prom -> {
      if (timeout > 0) {
        adminClient.close(Duration.ofMillis(timeout));
      } else {
        adminClient.close();
      }
      prom.complete();
    });
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    close().onComplete(handler);
  }

  @Override
  public void close(long timeout, Handler<AsyncResult<Void>> handler) {
    close(timeout).onComplete(handler);
  }

  @Override
  public void deleteRecords(Map<TopicPartition, Long> recordsToDelete, Handler<AsyncResult<Void>> completionHandler) {
    deleteRecords(recordsToDelete).onComplete(completionHandler);
  }

  @Override
  public Future<Void> deleteRecords(Map<TopicPartition, Long> recordsToDelete) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    deleteRecordsInternal(recordsToDelete, promise);
    return promise.future();
  }

  private void deleteRecordsInternal(Map<TopicPartition, Long> recordsToDelete, Promise<Void> promise) {
    try {
      DeleteRecordsResult deleteRecordsResult = this.adminClient.deleteRecords(recordsToDelete.entrySet().stream()
        .collect(Collectors.toMap(
          e -> Helper.to(e.getKey()),
          e -> RecordsToDelete.beforeOffset(e.getValue()))));

      deleteRecordsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void describeLogDirs(List<Integer> brokerIds, Handler<AsyncResult<Map<Integer, List<LogDirInfo>>>> completionHandler) {
    describeLogDirs(brokerIds).onComplete(completionHandler);
  }

  @Override
  public Future<Map<Integer, List<LogDirInfo>>> describeLogDirs(List<Integer> brokerIds) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<Integer, List<LogDirInfo>>> promise = ctx.promise();
    describeLogDirsInternal(brokerIds, promise);
    return promise.future();
  }

  private void describeLogDirsInternal(List<Integer> brokerIds, Promise<Map<Integer, List<LogDirInfo>>> promise) {
    try {
      DescribeLogDirsResult describeLogDirsResult = this.adminClient.describeLogDirs(brokerIds);
      describeLogDirsResult.allDescriptions().whenComplete((t, ex) -> {
        if (ex == null) {
          Map<Integer, List<LogDirInfo>> brokerInfos = new HashMap<>();
          for (Map.Entry<Integer, Map<String, LogDirDescription>> brokerEntry : t.entrySet()) {
            List<LogDirInfo> logDirInfos = new ArrayList<>();
            for (Map.Entry<String, LogDirDescription> logDirEntry : brokerEntry.getValue().entrySet()) {
              LogDirInfo logDirInfo = new LogDirInfo();
              logDirInfo
                .setPath(logDirEntry.getKey())
                .setReplicaInfos(logDirEntry.getValue().replicaInfos().entrySet().stream()
                  .map(v -> new TopicPartitionReplicaInfo(v.getKey(), Helper.from(v.getValue())))
                  .collect(Collectors.toList()));
              logDirInfos.add(logDirInfo);
            }
            brokerInfos.put(brokerEntry.getKey(), logDirInfos);
          }
          promise.complete(brokerInfos);
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void alterClientQuotas(List<ClientQuotaAlteration> entries, Handler<AsyncResult<Void>> completionHandler) {
    alterClientQuotas(entries).onComplete(completionHandler);
  }

  @Override
  public Future<Void> alterClientQuotas(List<ClientQuotaAlteration> entries) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    alterClientQuotasInternal(entries, promise);
    return promise.future();
  }

  private void alterClientQuotasInternal(List<ClientQuotaAlteration> entries, Promise<Void> promise) {
    try {
      AlterClientQuotasResult alterClientQuotasResult = this.adminClient.alterClientQuotas(
        entries.stream().map(Helper::to).collect(Collectors.toList()));

      alterClientQuotasResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void describeClientQuotas(ClientQuotaFilter filter, Handler<AsyncResult<Map<ClientQuotaEntity, Map<String, Double>>>> completionHandler) {
    describeClientQuotas(filter).onComplete(completionHandler);
  }

  @Override
  public Future<Map<ClientQuotaEntity, Map<String, Double>>> describeClientQuotas(ClientQuotaFilter filter) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<ClientQuotaEntity, Map<String, Double>>> promise = ctx.promise();
    describeClientQuotasInternal(filter, promise);
    return promise.future();
  }

  private void describeClientQuotasInternal(ClientQuotaFilter filter, Promise<Map<ClientQuotaEntity, Map<String, Double>>> promise) {
    try {
      org.apache.kafka.common.quota.ClientQuotaFilter clientQuotaFilter = org.apache.kafka.common.quota.ClientQuotaFilter.all();
      List<org.apache.kafka.common.quota.ClientQuotaFilterComponent> components = filter.getComponents().stream().map(Helper::to).collect(Collectors.toList());

      if (!filter.getComponents().isEmpty() && filter.isStrict()) {
        clientQuotaFilter = org.apache.kafka.common.quota.ClientQuotaFilter.containsOnly(components);
      } else if (!filter.getComponents().isEmpty() && !filter.isStrict()) {
        clientQuotaFilter = org.apache.kafka.common.quota.ClientQuotaFilter.contains(components);
      }

      DescribeClientQuotasResult describeClientQuotasResult = this.adminClient.describeClientQuotas(clientQuotaFilter);

      describeClientQuotasResult.entities().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v.entrySet().stream().collect(Collectors.toMap(
            e -> Helper.from(e.getKey()),
            Map.Entry::getValue
          )));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void removeMembersFromConsumerGroup(String groupId, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions, Handler<AsyncResult<Void>> completionHandler) {
    removeMembersFromConsumerGroup(groupId,removeMembersFromConsumerGroupOptions).onComplete(completionHandler);
  }

  @Override
  public Future<Void> removeMembersFromConsumerGroup(String groupId, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    removeMembersFromConsumerGroupInternal(groupId, removeMembersFromConsumerGroupOptions, promise);
    return promise.future();
  }

  private void removeMembersFromConsumerGroupInternal(String groupId, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions, Promise<Void> promise) {
    try {
      RemoveMembersFromConsumerGroupResult removeMembersFromConsumerGroupResult = this.adminClient.removeMembersFromConsumerGroup(groupId, Helper.to(removeMembersFromConsumerGroupOptions));
      removeMembersFromConsumerGroupResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void describeAcls(AclBindingFilter filter, Handler<AsyncResult<List<AclBinding>>> completionHandler) {
    describeAcls(filter).onComplete(completionHandler);
  }

  @Override
  public Future<List<AclBinding>> describeAcls(AclBindingFilter filter) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<List<AclBinding>> promise = ctx.promise();
    describeAclsInternal(filter, promise);
    return promise.future();
  }

  private void describeAclsInternal(AclBindingFilter filter, Promise<List<AclBinding>> promise) {
    try {
      DescribeAclsResult describeAclsResult = this.adminClient.describeAcls(Helper.to(filter));
      describeAclsResult.values().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v.stream().map(Helper::from).collect(Collectors.toList()));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void createAcls(List<AclBinding> acls, Handler<AsyncResult<Void>> completionHandler) {
    createAcls(acls).onComplete(completionHandler);
  }

  @Override
  public Future<Void> createAcls(List<AclBinding> acls) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    createAclsInternal(acls, promise);
    return promise.future();
  }

  private void createAclsInternal(List<AclBinding> acls, Promise<Void> promise) {
    try {
      CreateAclsResult createAclsResult = this.adminClient.createAcls(acls.stream().map(Helper::to).collect(Collectors.toList()));
      createAclsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void deleteAcls(List<AclBindingFilter> acls, Handler<AsyncResult<List<AclBinding>>> completionHandler) {
    deleteAcls(acls).onComplete(completionHandler);
  }

  @Override
  public Future<List<AclBinding>> deleteAcls(List<AclBindingFilter> acls) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<List<AclBinding>> promise = ctx.promise();
    deleteAclsInternal(acls, promise);
    return promise.future();
  }

  private void deleteAclsInternal(List<AclBindingFilter> acls, Promise<List<AclBinding>> promise) {
    try {
      DeleteAclsResult deleteAclsResult = this.adminClient.deleteAcls(acls.stream().map(Helper::to).collect(Collectors.toList()));
      deleteAclsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v.stream().map(Helper::from).collect(Collectors.toList()));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void describeFeatures(Handler<AsyncResult<FeatureMetadata>> completionHandler) {
    describeFeatures().onComplete(completionHandler);
  }

  @Override
  public Future<FeatureMetadata> describeFeatures() {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<FeatureMetadata> promise = ctx.promise();
    describeFeaturesInternal(promise);
    return promise.future();
  }

  private void describeFeaturesInternal(Promise<FeatureMetadata> promise) {
    try {
      DescribeFeaturesResult describeFeaturesResult = this.adminClient.describeFeatures();
      describeFeaturesResult.featureMetadata().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(Helper.from(v));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void updateFeatures(Map<String, FeatureUpdate> featureUpdates, Handler<AsyncResult<Void>> completionHandler) {
    updateFeatures(featureUpdates).onComplete(completionHandler);
  }

  @Override
  public Future<Void> updateFeatures(Map<String, FeatureUpdate> featureUpdates) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    updateFeaturesInternal(featureUpdates, promise);
    return promise.future();
  }

  private void updateFeaturesInternal(Map<String, FeatureUpdate> featureUpdates, Promise<Void> promise) {
    try {
      UpdateFeaturesResult updateFeaturesResult = this.adminClient.updateFeatures(
        featureUpdates.entrySet().stream().collect(Collectors.toMap(
          Map.Entry::getKey,
          e -> Helper.to(e.getValue()))),
        new UpdateFeaturesOptions());

      updateFeaturesResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void listPartitionReassignments(Handler<AsyncResult<Map<TopicPartition, PartitionReassignment>>> completionHandler) {
    listPartitionReassignments().onComplete(completionHandler);
  }

  @Override
  public Future<Map<TopicPartition, PartitionReassignment>> listPartitionReassignments() {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<TopicPartition, PartitionReassignment>> promise = ctx.promise();
    listPartitionReassignmentsInternal(null, promise);
    return promise.future();
  }

  @Override
  public void listPartitionReassignments(Set<TopicPartition> partitions, Handler<AsyncResult<Map<TopicPartition, PartitionReassignment>>> completionHandler) {
    listPartitionReassignments(partitions).onComplete(completionHandler);
  }

  @Override
  public Future<Map<TopicPartition, PartitionReassignment>> listPartitionReassignments(Set<TopicPartition> partitions) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<TopicPartition, PartitionReassignment>> promise = ctx.promise();
    listPartitionReassignmentsInternal(partitions, promise);
    return promise.future();
  }

  private void listPartitionReassignmentsInternal(Set<TopicPartition> partitions, Promise<Map<TopicPartition, PartitionReassignment>> promise) {
    try {
      ListPartitionReassignmentsResult listPartitionReassignmentsResult;
      if(Objects.isNull(partitions)) {
        listPartitionReassignmentsResult = this.adminClient.listPartitionReassignments();
      } else {
        listPartitionReassignmentsResult = this.adminClient.listPartitionReassignments(
          partitions.stream()
            .map(Helper::to).collect(Collectors.toSet()));
      }
      listPartitionReassignmentsResult.reassignments().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v.entrySet().stream().collect(Collectors.toMap(
            e -> Helper.from(e.getKey()),
            e -> Helper.from(e.getValue())
          )));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void alterPartitionReassignments(Map<TopicPartition, NewPartitionReassignment> reassignments, Handler<AsyncResult<Void>> completionHandler) {
    alterPartitionReassignments(reassignments).onComplete(completionHandler);
  }

  @Override
  public Future<Void> alterPartitionReassignments(Map<TopicPartition, NewPartitionReassignment> reassignments) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    alterPartitionReassignmentsInternal(reassignments, promise);
    return promise.future();
  }

  private void alterPartitionReassignmentsInternal(Map<TopicPartition, NewPartitionReassignment> reassignments, Promise<Void> promise) {
    try {
      AlterPartitionReassignmentsResult alterPartitionReassignmentsResult = this.adminClient.alterPartitionReassignments(
        reassignments.entrySet().stream()
          .collect(Collectors.toMap(
            e -> Helper.to(e.getKey()),
            e -> Optional.ofNullable(e.getValue()).map(Helper::to)
          ))
      );
      alterPartitionReassignmentsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void describeReplicaLogDirs(List<TopicPartitionReplica> replicas, Handler<AsyncResult<Map<TopicPartitionReplica, ReplicaLogDirInfo>>> completionHandler) {
    describeReplicaLogDirs(replicas).onComplete(completionHandler);
  }

  @Override
  public Future<Map<TopicPartitionReplica, ReplicaLogDirInfo>> describeReplicaLogDirs(List<TopicPartitionReplica> replicas) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Map<TopicPartitionReplica, ReplicaLogDirInfo>> promise = ctx.promise();
    describeReplicaLogDirsInternal(replicas, promise);
    return promise.future();
  }

  private void describeReplicaLogDirsInternal(List<TopicPartitionReplica> replicas, Promise<Map<TopicPartitionReplica, ReplicaLogDirInfo>> promise) {
    try {
      DescribeReplicaLogDirsResult describeReplicaLogDirsResult = this.adminClient.describeReplicaLogDirs(
        replicas.stream()
          .map(Helper::to)
          .collect(Collectors.toList()));
      describeReplicaLogDirsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v.entrySet().stream()
            .collect(Collectors.toMap(
              e -> Helper.from(e.getKey()),
              e -> Helper.from(e.getValue())
            )));
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void alterReplicaLogDirs(Map<TopicPartitionReplica, String> replicaAssignment, Handler<AsyncResult<Void>> completionHandler) {
    alterReplicaLogDirs(replicaAssignment).onComplete(completionHandler);
  }

  @Override
  public Future<Void> alterReplicaLogDirs(Map<TopicPartitionReplica, String> replicaAssignment) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    alterReplicaLogDirsInternal(replicaAssignment, promise);
    return promise.future();
  }

  private void alterReplicaLogDirsInternal(Map<TopicPartitionReplica, String> replicaAssignment, Promise<Void> promise) {
    try {
      AlterReplicaLogDirsResult alterReplicaLogDirsResult = this.adminClient.alterReplicaLogDirs(
        replicaAssignment.entrySet().stream()
          .collect(Collectors.toMap(
            e -> Helper.to(e.getKey()),
            Map.Entry::getValue
          )));
      alterReplicaLogDirsResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete();
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  @Override
  public void unregisterBroker(int brokerId, Handler<AsyncResult<Void>> completionHandler) {
    unregisterBroker(brokerId).onComplete(completionHandler);
  }

  @Override
  public Future<Void> unregisterBroker(int brokerId) {
    ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
    Promise<Void> promise = ctx.promise();
    unregisterBrokerInternal(brokerId, promise);
    return promise.future();
  }

  private void unregisterBrokerInternal(int brokerId, Promise<Void> promise) {
    try {
      UnregisterBrokerResult unregisterBrokerResult = this.adminClient.unregisterBroker(brokerId);
      unregisterBrokerResult.all().whenComplete((v, ex) -> {
        if (ex == null) {
          promise.complete(v);
        } else {
          promise.fail(ex);
        }
      });
    } catch (Exception e) {
      promise.fail(e);
    }
  }
}
