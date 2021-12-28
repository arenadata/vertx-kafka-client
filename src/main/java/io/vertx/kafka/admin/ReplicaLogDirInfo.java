package io.vertx.kafka.admin;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject
public class ReplicaLogDirInfo {
  private String currentReplicaLogDir;
  private long currentReplicaOffsetLag;
  private String futureReplicaLogDir;
  private long futureReplicaOffsetLag;

  public ReplicaLogDirInfo() {
    currentReplicaOffsetLag = -1L;
    futureReplicaOffsetLag = -1L;
  }

  public ReplicaLogDirInfo(String currentReplicaLogDir, long currentReplicaOffsetLag, String futureReplicaLogDir, long futureReplicaOffsetLag) {
    this.currentReplicaLogDir = currentReplicaLogDir;
    this.currentReplicaOffsetLag = currentReplicaOffsetLag;
    this.futureReplicaLogDir = futureReplicaLogDir;
    this.futureReplicaOffsetLag = futureReplicaOffsetLag;
  }

  public ReplicaLogDirInfo(JsonObject json) {
    currentReplicaLogDir = json.getString("currentReplicaLogDir");
    currentReplicaOffsetLag = json.getLong("currentReplicaOffsetLag");
    futureReplicaLogDir = json.getString("futureReplicaLogDir");
    futureReplicaOffsetLag = json.getLong("futureReplicaOffsetLag");
  }

  public String getCurrentReplicaLogDir() {
    return currentReplicaLogDir;
  }

  public ReplicaLogDirInfo setCurrentReplicaLogDir(String currentReplicaLogDir) {
    this.currentReplicaLogDir = currentReplicaLogDir;
    return this;
  }

  public long getCurrentReplicaOffsetLag() {
    return currentReplicaOffsetLag;
  }

  public ReplicaLogDirInfo setCurrentReplicaOffsetLag(long currentReplicaOffsetLag) {
    this.currentReplicaOffsetLag = currentReplicaOffsetLag;
    return this;
  }

  public String getFutureReplicaLogDir() {
    return futureReplicaLogDir;
  }

  public ReplicaLogDirInfo setFutureReplicaLogDir(String futureReplicaLogDir) {
    this.futureReplicaLogDir = futureReplicaLogDir;
    return this;
  }

  public long getFutureReplicaOffsetLag() {
    return futureReplicaOffsetLag;
  }

  public ReplicaLogDirInfo setFutureReplicaOffsetLag(long futureReplicaOffsetLag) {
    this.futureReplicaOffsetLag = futureReplicaOffsetLag;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json
      .put("currentReplicaLogDir", currentReplicaLogDir)
      .put("currentReplicaOffsetLag", currentReplicaOffsetLag)
      .put("futureReplicaLogDir", futureReplicaLogDir)
      .put("futureReplicaOffsetLag", futureReplicaOffsetLag);
    return json;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ReplicaLogDirInfo that = (ReplicaLogDirInfo) o;

    if (currentReplicaOffsetLag != that.currentReplicaOffsetLag) return false;
    if (futureReplicaOffsetLag != that.futureReplicaOffsetLag) return false;
    if (currentReplicaLogDir != null ? !currentReplicaLogDir.equals(that.currentReplicaLogDir) : that.currentReplicaLogDir != null)
      return false;
    return futureReplicaLogDir != null ? futureReplicaLogDir.equals(that.futureReplicaLogDir) : that.futureReplicaLogDir == null;
  }

  @Override
  public int hashCode() {
    int result = currentReplicaLogDir != null ? currentReplicaLogDir.hashCode() : 0;
    result = 31 * result + (int) (currentReplicaOffsetLag ^ (currentReplicaOffsetLag >>> 32));
    result = 31 * result + (futureReplicaLogDir != null ? futureReplicaLogDir.hashCode() : 0);
    result = 31 * result + (int) (futureReplicaOffsetLag ^ (futureReplicaOffsetLag >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "ReplicaLogDirInfo{" +
      "currentReplicaLogDir='" + currentReplicaLogDir + '\'' +
      ", currentReplicaOffsetLag=" + currentReplicaOffsetLag +
      ", futureReplicaLogDir='" + futureReplicaLogDir + '\'' +
      ", futureReplicaOffsetLag=" + futureReplicaOffsetLag +
      '}';
  }
}
