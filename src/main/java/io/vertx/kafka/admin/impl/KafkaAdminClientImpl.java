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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertx.core.Promise;
import io.vertx.core.impl.ContextInternal;
import io.vertx.kafka.admin.Config;
import io.vertx.kafka.admin.ConsumerGroupDescription;
import io.vertx.kafka.admin.ConsumerGroupListing;
import io.vertx.kafka.admin.MemberDescription;
import io.vertx.kafka.admin.NewTopic;
import io.vertx.kafka.admin.TopicDescription;
import io.vertx.kafka.client.common.ConfigResource;
import io.vertx.kafka.client.common.TopicPartitionInfo;
import io.vertx.kafka.client.common.impl.Helper;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigsResult;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.kafka.admin.KafkaAdminClient;

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

    AlterConfigsResult alterConfigsResult = this.adminClient.alterConfigs(Helper.toConfigMaps(configs));
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
}
