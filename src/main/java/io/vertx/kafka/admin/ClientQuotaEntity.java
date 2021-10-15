package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@DataObject(generateConverter = true)
public class ClientQuotaEntity {

  public static final String USER = "user";
  public static final String CLIENT_ID = "client-id";

  private Map<String, String> entries;

  public ClientQuotaEntity() {
  }

  public ClientQuotaEntity(Map<String, String> entries) {
    this.entries = entries;
  }

  public ClientQuotaEntity(JsonObject json) {
    ClientQuotaEntityConverter.fromJson(json, this);
  }

  public Map<String, String> getEntries() {
    return entries;
  }

  public ClientQuotaEntity setEntries(Map<String, String> entries) {
    this.entries = entries;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ClientQuotaEntityConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "ClientQuotaEntity{" +
      "entries=" + entries +
      '}';
  }
}
