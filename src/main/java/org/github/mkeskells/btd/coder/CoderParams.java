package org.github.mkeskells.btd.coder;

import java.util.List;
import java.util.Objects;

//should be a Record
public final class CoderParams {
  public final List<String> columns;
  public final String config;

  public CoderParams(String[] columns, String config) {
    this.columns = List.of(columns);
    this.config = config;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CoderParams key = (CoderParams) o;
    return columns.equals(key.columns) && config.equals(key.config);
  }

  @Override
  public int hashCode() {
    return Objects.hash(columns, config);
  }
}
