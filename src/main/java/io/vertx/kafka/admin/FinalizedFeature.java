package io.vertx.kafka.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FinalizedFeature {
  private final String featureName;
  private final short minVersionLevel;
  private final short maxVersionLevel;
}