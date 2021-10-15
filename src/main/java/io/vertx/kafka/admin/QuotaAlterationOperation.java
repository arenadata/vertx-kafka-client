package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class QuotaAlterationOperation {
  private String key;
  private double value;

  public QuotaAlterationOperation() {
  }

  public QuotaAlterationOperation(String key, double value) {
    this.key = key;
    this.value = value;
  }

  public QuotaAlterationOperation(JsonObject json) {
    this.key = json.getString("key");
    this.value = json.getDouble("value");
  }

  public String getKey() {
    return key;
  }

  public QuotaAlterationOperation setKey(String key) {
    this.key = key;
    return this;
  }

  public double getValue() {
    return value;
  }

  public QuotaAlterationOperation setValue(double value) {
    this.value = value;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("key", this.key)
      .put("value", this.value);
    return json;
  }

  @Override
  public String toString() {
    return "QuotaAlterationOperation{" +
      "key='" + key + '\'' +
      ", value=" + value +
      '}';
  }
}
