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

package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.kafka.admin.impl.KafkaAdminClientImpl;
import io.vertx.kafka.client.common.ConfigResource;
import io.vertx.kafka.client.common.ElectionType;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.common.TopicPartitionReplica;
import io.vertx.kafka.client.common.acl.AclBinding;
import io.vertx.kafka.client.common.acl.AclBindingFilter;
import io.vertx.kafka.client.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.admin.AdminClient;

import java.util.*;

/**
 * Vert.x Kafka Admin client implementation
 */
@VertxGen
public interface KafkaAdminClient {

  /**
   * Create a new KafkaAdminClient instance
   *
   * @param vertx Vert.x instance to use
   * @param adminClient Kafka native Admin client instance
   * @return an instance of the KafkaAdminClient
   */
  @GenIgnore
  static KafkaAdminClient create(Vertx vertx, AdminClient adminClient) {
    return new KafkaAdminClientImpl(vertx, adminClient);
  }

  /**
   * Create a new KafkaAdminClient instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka admin client configuration
   * @return an instance of the KafkaAdminClient
   */
  static KafkaAdminClient create(Vertx vertx, Map<String, String> config) {
    return create(vertx, AdminClient.create(new HashMap<>(config)));
  }

  /**
   * Create a new KafkaAdminClient instance
   *
   * @param vertx Vert.x instance to use
   * @param config Kafka admin client configuration
   * @return an instance of the KafkaAdminClient
   */
  @GenIgnore
  static KafkaAdminClient create(Vertx vertx, Properties config) {
    return create(vertx, AdminClient.create(config));
  }

  /**
   * List the topics available in the cluster with the default options.
   *
   * @param completionHandler handler called on operation completed with the topics set
   */
  void listTopics(Handler<AsyncResult<Set<String>>> completionHandler);

  /**
   * Like {@link #listTopics(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Set<String>> listTopics();

  /**
   * Describe some topics in the cluster, with the default options.
   *
   * @param topicNames the names of the topics to describe
   * @param completionHandler handler called on operation completed with the topics descriptions
   */
  void describeTopics(List<String> topicNames, Handler<AsyncResult<Map<String, TopicDescription>>> completionHandler);

  /**
   * Like {@link #describeTopics(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Map<String, TopicDescription>> describeTopics(List<String> topicNames);

  /**
   * Creates a batch of new Kafka topics
   *
   * @param topics topics to create
   * @param completionHandler handler called on operation completed
   */
  void createTopics(List<NewTopic> topics, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #createTopics(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> createTopics(List<NewTopic> topics);

  /**
   * Deletes a batch of Kafka topics
   *
   * @param topicNames the names of the topics to delete
   * @param completionHandler handler called on operation completed
   */
  void deleteTopics(List<String> topicNames, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #deleteTopics(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> deleteTopics(List<String> topicNames);

  /**
   * Creates a batch of new partitions in the Kafka topic
   *
   * @param partitions partitions to create
   * @param completionHandler handler called on operation completed
   */
  void createPartitions(Map<String, io.vertx.kafka.admin.NewPartitions> partitions, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #createPartitions(Map, Handler)} but returns a {@code Future} of the asynchronous result
   * @param partitions
   */
  Future<Void> createPartitions(Map<String, io.vertx.kafka.admin.NewPartitions> partitions);


  /**
   * Get the configuration for the specified resources with the default options
   *
   * @param configResources the resources (topic and broker resource types are currently supported)
   * @param completionHandler handler called on operation completed with the configurations
   */
  @GenIgnore
  void describeConfigs(List<ConfigResource> configResources, Handler<AsyncResult<Map<ConfigResource, Config>>> completionHandler);

  /**
   * Like {@link #describeConfigs(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  Future<Map<ConfigResource, Config>> describeConfigs(List<ConfigResource> configResources);

  /**
   * Update the configuration for the specified resources with the default options
   *
   * @param configs The resources with their configs (topic is the only resource type with configs that can be updated currently)
   * @param completionHandler handler called on operation completed
   */
  @GenIgnore
  void alterConfigs(Map<ConfigResource,Config> configs, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #alterConfigs(Map, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  Future<Void> alterConfigs(Map<ConfigResource,Config> configs);

  /**
   * Get the the consumer groups available in the cluster with the default options
   *
   * @param completionHandler handler called on operation completed with the consumer groups ids
   */
  void listConsumerGroups(Handler<AsyncResult<List<ConsumerGroupListing>>> completionHandler);

  /**
   * Like {@link #listConsumerGroups(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<List<ConsumerGroupListing>> listConsumerGroups();

  /**
   * Describe some group ids in the cluster, with the default options
   *
   * @param groupIds the ids of the groups to describe
   * @param completionHandler handler called on operation completed with the consumer groups descriptions
   */
  void describeConsumerGroups(List<String> groupIds, Handler<AsyncResult<Map<String, ConsumerGroupDescription>>> completionHandler);

  /**
   * Like {@link #describeConsumerGroups(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Map<String, ConsumerGroupDescription>> describeConsumerGroups(List<String> groupIds);

  /**
   * Describe the nodes in the cluster with the default options
   *
   * @param completionHandler handler called on operation completed with the cluster description
   */
  void describeCluster(Handler<AsyncResult<ClusterDescription>> completionHandler);

  /**
   * Like {@link #describeCluster(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<ClusterDescription> describeCluster();

  /**
   * Delete consumer groups from the cluster.
   *
   * @param groupIds the ids of the groups to delete
   * @param completionHandler handler called on operation completed
   */
  void deleteConsumerGroups(List<String> groupIds, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #deleteConsumerGroups(List, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> deleteConsumerGroups(List<String> groupIds);

  /**
   * List the consumer group offsets available in the cluster.
   *
   * @param groupId The group id of the group whose offsets will be listed
   * @param options The options to use when listing the consumer group offsets.
   * @param completionHandler handler called on operation completed with the consumer groups offsets
   */
  @GenIgnore
  void listConsumerGroupOffsets(String groupId, ListConsumerGroupOffsetsOptions options, Handler<AsyncResult<Map<TopicPartition, OffsetAndMetadata>>> completionHandler);

  /**
   * Like {@link #listConsumerGroupOffsets(String, ListConsumerGroupOffsetsOptions, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  Future<Map<TopicPartition, OffsetAndMetadata>> listConsumerGroupOffsets(String groupId, ListConsumerGroupOffsetsOptions options);

  /**
   * List the consumer group offsets available in the cluster.
   *
   * @param groupId The group id of the group whose offsets will be listed
   * @param completionHandler handler called on operation completed with the consumer groups offsets
   */
  @GenIgnore
  default void listConsumerGroupOffsets(String groupId, Handler<AsyncResult<Map<TopicPartition, OffsetAndMetadata>>> completionHandler) {
    listConsumerGroupOffsets(groupId, new ListConsumerGroupOffsetsOptions(), completionHandler);
  }

  /**
   * Like {@link #listConsumerGroupOffsets(String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  default Future<Map<TopicPartition, OffsetAndMetadata>> listConsumerGroupOffsets(String groupId) {
    return listConsumerGroupOffsets(groupId, new ListConsumerGroupOffsetsOptions());
  }

  /**
   * Delete committed offsets for a set of partitions in a consumer group. This will
   * succeed at the partition level only if the group is not actively subscribed
   * to the corresponding topic.
   *
   * @param groupId The group id of the group whose offsets will be deleted
   * @param partitions The set of partitions in the consumer group whose offsets will be deleted
   */
  void deleteConsumerGroupOffsets(String groupId, Set<TopicPartition> partitions, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #deleteConsumerGroupOffsets(String, Set, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> deleteConsumerGroupOffsets(String groupId, Set<TopicPartition> partitions);

  /**
   * Alter committed offsets for a set of partitions in a consumer group.
   *
   * @param groupId The group id of the group whose offsets will be altered
   * @param offsets The map of offsets in the consumer group which will be altered
   */
  @GenIgnore
  void alterConsumerGroupOffsets(String groupId, Map<TopicPartition, OffsetAndMetadata> offsets, Handler<AsyncResult<Void>> completionHandler);

  /**
   * Like {@link #alterConsumerGroupOffsets(String, Map, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  Future<Void> alterConsumerGroupOffsets(String groupId, Map<TopicPartition, OffsetAndMetadata> offsets);

  /**
   * List the offsets available for a set of partitions.
   *
   * @param topicPartitionOffsets The options to use when listing the partition offsets.
   * @param completionHandler handler called on operation completed with the partition offsets
   */
  @GenIgnore
  void listOffsets(Map<TopicPartition, OffsetSpec> topicPartitionOffsets, Handler<AsyncResult<Map<TopicPartition, ListOffsetsResultInfo>>> completionHandler);

  /**
   * Like {@link #listOffsets(Map<TopicPartition, OffsetSpec>, Handler)} but returns a {@code Future} of the asynchronous result
   */
  @GenIgnore
  Future<Map<TopicPartition, ListOffsetsResultInfo>> listOffsets(Map<TopicPartition, OffsetSpec> topicPartitionOffsets);

  @GenIgnore
  void electLeaders(ElectionType electionType, Set<TopicPartition> partitions, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> electLeaders(ElectionType electionType, Set<TopicPartition> partitions);

  /**
   * Close the admin client
   *
   * @param handler a {@code Handler} completed with the operation result
   */
  void close(Handler<AsyncResult<Void>> handler);

  /**
   * Like {@link #close(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> close();

  /**
   * Close the admin client
   *
   * @param timeout timeout to wait for closing
   * @param handler a {@code Handler} completed with the operation result
   */
  void close(long timeout, Handler<AsyncResult<Void>> handler);

  /**
   * Like {@link #close(long, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> close(long timeout);

  @GenIgnore
  void alterClientQuotas(List<ClientQuotaAlteration> entries, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> alterClientQuotas(List<ClientQuotaAlteration> entries);

  @GenIgnore
  void describeClientQuotas(ClientQuotaFilter filter, Handler<AsyncResult<Map<ClientQuotaEntity, Map<String, Double>>>> completionHandler);

  @GenIgnore
  Future<Map<ClientQuotaEntity, Map<String, Double>>> describeClientQuotas(ClientQuotaFilter filter);

  @GenIgnore
  void describeLogDirs(List<Integer> brokerIds, Handler<AsyncResult<Map<Integer, List<LogDirInfo>>>> completionHandler);

  @GenIgnore
  Future<Map<Integer, List<LogDirInfo>>> describeLogDirs(List<Integer> brokerIds);


  @GenIgnore
  void deleteRecords(Map<TopicPartition, Long> recordsToDelete, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> deleteRecords(Map<TopicPartition, Long> recordsToDelete);

  @GenIgnore
  void removeMembersFromConsumerGroup(String groupId, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> removeMembersFromConsumerGroup(String groupId, RemoveMembersFromConsumerGroupOptions removeMembersFromConsumerGroupOptions);

  @GenIgnore
  void describeAcls(AclBindingFilter filter, Handler<AsyncResult<List<AclBinding>>> completionHandler);

  @GenIgnore
  Future<List<AclBinding>> describeAcls(AclBindingFilter filter);

  @GenIgnore
  void createAcls(List<AclBinding> acls, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> createAcls(List<AclBinding> acls);

  @GenIgnore
  void deleteAcls(List<AclBindingFilter> acls, Handler<AsyncResult<List<AclBinding>>> completionHandler);

  @GenIgnore
  Future<List<AclBinding>> deleteAcls(List<AclBindingFilter> acls);

  @GenIgnore
  void describeFeatures(Handler<AsyncResult<FeatureMetadata>> completionHandler);

  @GenIgnore
  Future<FeatureMetadata> describeFeatures();

  @GenIgnore
  void updateFeatures(Map<String, FeatureUpdate> featureUpdates, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> updateFeatures(Map<String, FeatureUpdate> featureUpdates);

  @GenIgnore
  void listPartitionReassignments(Handler<AsyncResult<Map<TopicPartition, PartitionReassignment>>> completionHandler);

  @GenIgnore
  Future<Map<TopicPartition, PartitionReassignment>> listPartitionReassignments();

  @GenIgnore
  void listPartitionReassignments(Set<TopicPartition> partitions, Handler<AsyncResult<Map<TopicPartition, PartitionReassignment>>> completionHandler);

  @GenIgnore
  Future<Map<TopicPartition, PartitionReassignment>> listPartitionReassignments(Set<TopicPartition> partitions);

  @GenIgnore
  void alterPartitionReassignments(Map<TopicPartition, NewPartitionReassignment> reassignments, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> alterPartitionReassignments(Map<TopicPartition, NewPartitionReassignment> reassignments);

  @GenIgnore
  void describeReplicaLogDirs(List<TopicPartitionReplica> replicas, Handler<AsyncResult<Map<TopicPartitionReplica, ReplicaLogDirInfo>>> completionHandler);

  @GenIgnore
  Future<Map<TopicPartitionReplica, ReplicaLogDirInfo>> describeReplicaLogDirs(List<TopicPartitionReplica> replicas);

  @GenIgnore
  void alterReplicaLogDirs(Map<TopicPartitionReplica, String> replicaAssignment, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> alterReplicaLogDirs(Map<TopicPartitionReplica, String> replicaAssignment);

  @GenIgnore
  void unregisterBroker(int brokerId, Handler<AsyncResult<Void>> completionHandler);

  @GenIgnore
  Future<Void> unregisterBroker(int brokerId);
}
