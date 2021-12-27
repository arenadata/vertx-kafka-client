package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class FinalizedVersionRange {

  private short minVersionLevel;

  private short maxVersionLevel;

  public FinalizedVersionRange() {
    minVersionLevel = 1;
    maxVersionLevel = 1;
  }


  public FinalizedVersionRange(short minVersionLevel, short maxVersionLevel) {
    checkRangeFormat(minVersionLevel, maxVersionLevel);
    this.minVersionLevel = minVersionLevel;
    this.maxVersionLevel = maxVersionLevel;
  }

  public FinalizedVersionRange(JsonObject json) {
    if(json.containsKey("minVersionLevel")) {
      minVersionLevel = json.getInteger("minVersionLevel").shortValue();
    } else {
      minVersionLevel = 1;
    }
    if(json.containsKey("maxVersionLevel")) {
      maxVersionLevel = json.getInteger("maxVersionLevel").shortValue();
    } else {
      maxVersionLevel = 1;
    }
    checkRangeFormat(minVersionLevel, maxVersionLevel);
  }

  public short getMinVersionLevel() {
    return minVersionLevel;
  }

  public FinalizedVersionRange setMinVersionLevel(short minVersionLevel) {
    checkRangeFormat(minVersionLevel, this.maxVersionLevel);
    this.minVersionLevel = minVersionLevel;
    return this;
  }

  public short getMaxVersionLevel() {
    return maxVersionLevel;
  }

  public FinalizedVersionRange setMaxVersionLevel(short maxVersionLevel) {
    checkRangeFormat(this.minVersionLevel, maxVersionLevel);
    this.maxVersionLevel = maxVersionLevel;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("minVersionLevel", minVersionLevel)
      .put("maxVersionLevel", maxVersionLevel);
    return json;
  }

  /**
   * Raises an exception unless the following condition is met:
   * minVersionLevel >= 1 and maxVersionLevel >= 1 and maxVersionLevel >= minVersionLevel.
   *
   * @param minVersionLevel   The minimum version level value.
   * @param maxVersionLevel   The maximum version level value.
   *
   * @throws IllegalArgumentException   Raised when the condition described above is not met.
   */
  private void checkRangeFormat(short minVersionLevel, short maxVersionLevel) {
    if (minVersionLevel < 1 || maxVersionLevel < 1 || maxVersionLevel < minVersionLevel) {
      throw new IllegalArgumentException(
        String.format(
          "Expected minVersionLevel >= 1, maxVersionLevel >= 1 and" +
            " maxVersionLevel >= minVersionLevel, but received" +
            " minVersionLevel: %d, maxVersionLevel: %d", minVersionLevel, maxVersionLevel));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FinalizedVersionRange that = (FinalizedVersionRange) o;

    if (minVersionLevel != that.minVersionLevel) return false;
    return maxVersionLevel == that.maxVersionLevel;
  }

  @Override
  public int hashCode() {
    int result = minVersionLevel;
    result = 31 * result + (int) maxVersionLevel;
    return result;
  }

  @Override
  public String toString() {
    return "FinalizedVersionRange{" +
      "minVersionLevel=" + minVersionLevel +
      ", maxVersionLevel=" + maxVersionLevel +
      '}';
  }
}
