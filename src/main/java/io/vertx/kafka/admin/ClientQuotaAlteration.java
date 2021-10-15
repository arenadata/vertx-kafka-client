package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject(generateConverter = true)
public class ClientQuotaAlteration {

  private ClientQuotaEntity entity;
  private List<QuotaAlterationOperation> ops;

  public ClientQuotaAlteration() {
  }

  public ClientQuotaAlteration(ClientQuotaEntity entity, List<QuotaAlterationOperation> ops) {
    this.entity = entity;
    this.ops = ops;
  }

  public ClientQuotaAlteration(JsonObject json) {
    ClientQuotaAlterationConverter.fromJson(json, this);
  }

  public ClientQuotaEntity getEntity() {
    return entity;
  }

  public ClientQuotaAlteration setEntity(ClientQuotaEntity entity) {
    this.entity = entity;
    return this;
  }

  public List<QuotaAlterationOperation> getOps() {
    return ops;
  }

  public ClientQuotaAlteration setOps(List<QuotaAlterationOperation> ops) {
    this.ops = ops;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ClientQuotaAlterationConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "ClientQuotaAlteration{" +
      "entity=" + entity +
      ", ops=" + ops +
      '}';
  }
}
