package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.List;

@DataObject(generateConverter = true)
public class ClientQuotaFilter {

  private List<ClientQuotaFilterComponent> components;
  private boolean strict;

  public ClientQuotaFilter() {
  }

  public static ClientQuotaFilter all() {
    return new ClientQuotaFilter(Collections.emptyList(), false);
  }

  public static ClientQuotaFilter contains(List<ClientQuotaFilterComponent> components) {
    return new ClientQuotaFilter(components, false);
  }

  public static ClientQuotaFilter containsOnly(List<ClientQuotaFilterComponent> components) {
    return new ClientQuotaFilter(components, true);
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
