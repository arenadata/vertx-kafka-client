package io.vertx.kafka.client.common.acl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.common.resource.ResourcePatternFilter;

import java.util.Objects;

@DataObject(generateConverter = true)
public class AclBindingFilter {

  private ResourcePatternFilter patternFilter;
  private AccessControlEntryFilter entryFilter;

  public static final AclBindingFilter ANY = new AclBindingFilter(ResourcePatternFilter.ANY, AccessControlEntryFilter.ANY);

  public AclBindingFilter() {
    patternFilter = new ResourcePatternFilter();
    entryFilter = new AccessControlEntryFilter();
  }

  public AclBindingFilter(ResourcePatternFilter patternFilter, AccessControlEntryFilter entryFilter) {
    this.patternFilter = Objects.requireNonNull(patternFilter, "patternFilter");
    this.entryFilter = Objects.requireNonNull(entryFilter, "entryFilter");
  }

  public AclBindingFilter(JsonObject json) {
    AclBindingFilterConverter.fromJson(json, this);
  }

  public ResourcePatternFilter getPatternFilter() {
    return patternFilter;
  }

  public AclBindingFilter setPatternFilter(ResourcePatternFilter patternFilter) {
    this.patternFilter = Objects.requireNonNull(patternFilter);
    return this;
  }

  public AccessControlEntryFilter getEntryFilter() {
    return entryFilter;
  }

  public AclBindingFilter setEntryFilter(AccessControlEntryFilter entryFilter) {
    this.entryFilter = Objects.requireNonNull(entryFilter);
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    AclBindingFilterConverter.toJson(this, json);
    return json;
  }

  /**
   * Return true if the resource and entry filters can only match one ACE. In other words, if
   * there are no ANY or UNKNOWN fields.
   */
  public boolean matchesAtMostOne() {
    return patternFilter.matchesAtMostOne() && entryFilter.matchesAtMostOne();
  }

  /**
   * Return a string describing an ANY or UNKNOWN field, or null if there is no such field.
   */
  public String findIndefiniteField() {
    String indefinite = patternFilter.findIndefiniteField();
    if (indefinite != null)
      return indefinite;
    return entryFilter.findIndefiniteField();
  }

  /**
   * Return true if the resource filter matches the binding's resource and the entry filter matches binding's entry.
   */
  public boolean matches(AclBinding binding) {
    return patternFilter.matches(binding.getPattern()) && entryFilter.matches(binding.getEntry());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AclBindingFilter that = (AclBindingFilter) o;

    if (!Objects.equals(patternFilter, that.patternFilter)) return false;
    return Objects.equals(entryFilter, that.entryFilter);
  }

  @Override
  public int hashCode() {
    int result = patternFilter != null ? patternFilter.hashCode() : 0;
    result = 31 * result + (entryFilter != null ? entryFilter.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AclBindingFilter{" +
      "patternFilter=" + patternFilter +
      ", entryFilter=" + entryFilter +
      '}';
  }
}
