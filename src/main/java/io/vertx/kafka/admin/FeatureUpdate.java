package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class FeatureUpdate {

  private short maxVersionLevel;
  private boolean allowDowngrade;

  public FeatureUpdate() {
    maxVersionLevel = 1;
    allowDowngrade = true;
  }

  public FeatureUpdate(short maxVersionLevel, boolean allowDowngrade) {
    this.maxVersionLevel = maxVersionLevel;
    this.allowDowngrade = allowDowngrade;
    checkFeatureVersion(maxVersionLevel, allowDowngrade);
  }

  public FeatureUpdate(JsonObject json) {
    if(json.containsKey("maxVersionLevel")) {
      maxVersionLevel = json.getInteger("maxVersionLevel").shortValue();
    } else {
      maxVersionLevel = 1;
    }
    if(json.containsKey("allowDowngrade")) {
      allowDowngrade = json.getBoolean("maxVersionLevel");
    } else {
      allowDowngrade =true;
    }
    checkFeatureVersion(maxVersionLevel, allowDowngrade);
  }

  public short getMaxVersionLevel() {
    return maxVersionLevel;
  }

  public FeatureUpdate setMaxVersionLevel(short maxVersionLevel) {
    checkFeatureVersion(maxVersionLevel, this.allowDowngrade);
    this.maxVersionLevel = maxVersionLevel;
    return this;
  }

  public boolean isAllowDowngrade() {
    return allowDowngrade;
  }

  public FeatureUpdate setAllowDowngrade(boolean allowDowngrade) {
    checkFeatureVersion(this.maxVersionLevel, allowDowngrade);
    this.allowDowngrade = allowDowngrade;
    return this;
  }

  private void checkFeatureVersion(short maxVersionLevel, boolean allowDowngrade) {
    if (maxVersionLevel < 1 && !allowDowngrade) {
      throw new IllegalArgumentException(String.format(
        "The allowDowngrade flag should be set when the provided maxVersionLevel:%d is < 1.",
        maxVersionLevel));
    }
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("maxVersionLevel", maxVersionLevel)
      .put("allowDowngrade", allowDowngrade);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FeatureUpdate that = (FeatureUpdate) o;

    if (maxVersionLevel != that.maxVersionLevel) return false;
    return allowDowngrade == that.allowDowngrade;
  }

  @Override
  public int hashCode() {
    int result = maxVersionLevel;
    result = 31 * result + (allowDowngrade ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "FeatureUpdate{" +
      "maxVersionLevel=" + maxVersionLevel +
      ", allowDowngrade=" + allowDowngrade +
      '}';
  }
}
