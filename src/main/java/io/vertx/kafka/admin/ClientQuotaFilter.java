package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject(generateConverter = true)
public class ClientQuotaFilter {

  private List<ClientQuotaFilterComponent> components;
  private boolean strict;

  public ClientQuotaFilter() {
  }

  public ClientQuotaFilter(List<ClientQuotaFilterComponent> components, boolean strict) {
    this.components = components;
    this.strict = strict;
  }

  public ClientQuotaFilter(JsonObject json) {
    ClientQuotaFilterConverter.fromJson(json, this);
  }

  public List<ClientQuotaFilterComponent> getComponents() {
    return components;
  }

  public ClientQuotaFilter setComponents(List<ClientQuotaFilterComponent> components) {
    this.components = components;
    return this;
  }

  public boolean isStrict() {
    return strict;
  }

  public ClientQuotaFilter setStrict(boolean strict) {
    this.strict = strict;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ClientQuotaFilterConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "ClientQuotaFilter{" +
      "components=" + components +
      ", strict=" + strict +
      '}';
  }
}
