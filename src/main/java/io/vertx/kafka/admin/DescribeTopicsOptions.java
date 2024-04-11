package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Optional;

@DataObject(generateConverter = true)
public class DescribeTopicsOptions {

    private Integer timeoutMs = null;
    private boolean includeAuthorizedOperations = false;

    public DescribeTopicsOptions() {
    }

    public DescribeTopicsOptions(Integer timeoutMs, boolean includeAuthorizedOperations) {
        this.timeoutMs = timeoutMs;
        this.includeAuthorizedOperations = includeAuthorizedOperations;
    }

    public DescribeTopicsOptions(JsonObject json) {
        DescribeTopicsOptionsConverter.fromJson(json, this);
    }

    public Integer timeoutMs() {
        return timeoutMs;
    }

    public boolean isIncludeAuthorizedOperations() {
        return includeAuthorizedOperations;
    }

    public DescribeTopicsOptions setTimeoutMs(Integer timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    public DescribeTopicsOptions setIncludeAuthorizedOperations(boolean includeAuthorizedOperations) {
        this.includeAuthorizedOperations = includeAuthorizedOperations;
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        DescribeTopicsOptionsConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        return "ListTopicsOptions{" +
                "timeoutMs=" + timeoutMs +
                "includeAuthorizedOperations=" + includeAuthorizedOperations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DescribeTopicsOptions that = (DescribeTopicsOptions) o;

        return Objects.equals(timeoutMs, that.timeoutMs) && includeAuthorizedOperations == that.includeAuthorizedOperations;
    }

  @Override
  public int hashCode() {
    return Objects.hash(timeoutMs, includeAuthorizedOperations);
  }
}
