package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

@DataObject(generateConverter = true)
public class FeatureMetadata {

  private Map<String, FinalizedVersionRange> finalizedFeatures;

  private long finalizedFeaturesEpoch;

  private Map<String, SupportedVersionRange> supportedFeatures;

  public FeatureMetadata() {
    finalizedFeatures = new HashMap<>();
    finalizedFeaturesEpoch = -1;
    supportedFeatures = new HashMap<>();
  }

  public FeatureMetadata(Map<String, FinalizedVersionRange> finalizedFeatures, long finalizedFeaturesEpoch, Map<String, SupportedVersionRange> supportedFeatures) {
    this.finalizedFeatures = Objects.requireNonNull(finalizedFeatures);
    this.finalizedFeaturesEpoch = finalizedFeaturesEpoch;
    this.supportedFeatures = Objects.requireNonNull(supportedFeatures);
  }

  public FeatureMetadata(JsonObject json) {
    FeatureMetadataConverter.fromJson(json, this);
  }

  public Map<String, FinalizedVersionRange> getFinalizedFeatures() {
    return finalizedFeatures;
  }

  public FeatureMetadata setFinalizedFeatures(Map<String, FinalizedVersionRange> finalizedFeatures) {
    this.finalizedFeatures = Objects.requireNonNull(finalizedFeatures);
    return this;
  }

  public long getFinalizedFeaturesEpoch() {
    return finalizedFeaturesEpoch;
  }

  public FeatureMetadata setFinalizedFeaturesEpoch(long finalizedFeaturesEpoch) {
    this.finalizedFeaturesEpoch = finalizedFeaturesEpoch;
    return this;
  }

  public Map<String, SupportedVersionRange> getSupportedFeatures() {
    return supportedFeatures;
  }

  public FeatureMetadata setSupportedFeatures(Map<String, SupportedVersionRange> supportedFeatures) {
    this.supportedFeatures = Objects.requireNonNull(supportedFeatures);
    return this;
  }


  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    FeatureMetadataConverter.toJson(this, json);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FeatureMetadata that = (FeatureMetadata) o;

    if (finalizedFeaturesEpoch != that.finalizedFeaturesEpoch) return false;
    if (finalizedFeatures != null ? !finalizedFeatures.equals(that.finalizedFeatures) : that.finalizedFeatures != null)
      return false;
    return supportedFeatures != null ? supportedFeatures.equals(that.supportedFeatures) : that.supportedFeatures == null;
  }

  @Override
  public int hashCode() {
    int result = finalizedFeatures != null ? finalizedFeatures.hashCode() : 0;
    result = 31 * result + (int) (finalizedFeaturesEpoch ^ (finalizedFeaturesEpoch >>> 32));
    result = 31 * result + (supportedFeatures != null ? supportedFeatures.hashCode() : 0);
    return result;
  }

  private static <ValueType> String mapToString(final Map<String, ValueType> featureVersionsMap) {
    return String.format(
      "{%s}",
      featureVersionsMap
        .entrySet()
        .stream()
        .map(entry -> String.format("(%s -> %s)", entry.getKey(), entry.getValue()))
        .collect(joining(", "))
    );
  }

  @Override
  public String toString() {
    return String.format(
      "FeatureMetadata{finalizedFeatures:%s, finalizedFeaturesEpoch:%s, supportedFeatures:%s}",
      mapToString(finalizedFeatures),
      finalizedFeaturesEpoch != -1 ? finalizedFeaturesEpoch : "<none>",
      mapToString(supportedFeatures));
  }
}
