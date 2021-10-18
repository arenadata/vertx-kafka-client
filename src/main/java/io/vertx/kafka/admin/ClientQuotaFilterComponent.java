package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ClientQuotaFilterComponent {

  private String entityType;
  private String match;

  public ClientQuotaFilterComponent() {
  }

  public ClientQuotaFilterComponent(String entityType, String match) {
    this.entityType = entityType;
    this.match = match;
  }

  public ClientQuotaFilterComponent(JsonObject json) {
    this.entityType = json.getString("entityType");
    this.match = json.getString("match");
  }

  public String getEntityType() {
    return entityType;
  }

  public ClientQuotaFilterComponent setEntityType(String entityType) {
    this.entityType = entityType;
    return this;
  }

  public String getMatch() {
    return match;
  }

  public ClientQuotaFilterComponent setMatch(String match) {
    this.match = match;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("entityType", this.entityType)
      .put("match", this.match);
    return json;
  }

  @Override
  public String toString() {
    return "ClientQuotaFilterComponent{" +
      "entityType='" + entityType + '\'' +
      ", match='" + match + '\'' +
      '}';
  }
}
