package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class SupportedVersionRange {
  private short minVersion;

  private short maxVersion;

  public SupportedVersionRange() {
    minVersion = 1;
    maxVersion = 1;
  }

  public SupportedVersionRange(short minVersion, short maxVersion) {
    checkRangeFormat(minVersion, maxVersion);
    this.minVersion = minVersion;
    this.maxVersion = maxVersion;
  }

  public SupportedVersionRange(JsonObject json) {
    if(json.containsKey("minVersion")) {
      minVersion = json.getInteger("minVersion").shortValue();
    } else {
      minVersion = 1;
    }
    if(json.containsKey("maxVersion")) {
      maxVersion = json.getInteger("maxVersion").shortValue();
    } else {
      maxVersion = 1;
    }
    checkRangeFormat(minVersion, maxVersion);
  }


  public short getMinVersion() {
    return minVersion;
  }

  public SupportedVersionRange setMinVersion(short minVersion) {
    checkRangeFormat(minVersion, this.maxVersion);
    this.minVersion = minVersion;
    return this;
  }

  public short getMaxVersion() {
    return maxVersion;
  }

  public SupportedVersionRange setMaxVersion(short maxVersion) {
    checkRangeFormat(this.minVersion, maxVersion);
    this.maxVersion = maxVersion;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("minVersion", minVersion)
      .put("maxVersion", maxVersion);
    return json;
  }

  /**
   * Raises an exception unless the following conditions are met:
   *  1 <= minVersion <= maxVersion.
   *
   * @param minVersion           The minimum version value.
   * @param maxVersion           The maximum version value.
   *
   * @throws IllegalArgumentException   Raised when the condition described above is not met.
   */
  private void checkRangeFormat(short minVersion, short maxVersion) {
    if (minVersion < 1 || maxVersion < 1 || maxVersion < minVersion) {
      throw new IllegalArgumentException(
        String.format(
          "Expected minVersion >= 1, maxVersion >= 1 and" +
            " maxVersion >= minVersion, but received" +
            " minVersion: %d, maxVersion: %d", minVersion, maxVersion));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SupportedVersionRange that = (SupportedVersionRange) o;

    if (minVersion != that.minVersion) return false;
    return maxVersion == that.maxVersion;
  }

  @Override
  public int hashCode() {
    int result = minVersion;
    result = 31 * result + (int) maxVersion;
    return result;
  }

  @Override
  public String toString() {
    return "SupportedVersionRange{" +
      "minVersion=" + minVersion +
      ", maxVersion=" + maxVersion +
      '}';
  }
}
