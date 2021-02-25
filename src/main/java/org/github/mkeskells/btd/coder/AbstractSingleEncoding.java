package org.github.mkeskells.btd.coder;

import java.util.List;

public abstract class AbstractSingleEncoding<T> implements Encoding {
  private final List<Class<T>> types;

  public AbstractSingleEncoding(Class<T> type) {
    this.types = List.of(type);
  }

  @Override
  public List<Class<T>> types() {
    return types;
  }


  @Override
  public String asString(Object parsedObject, long parsedLong) {
    Object x = asObject(parsedObject, parsedLong, Object.class);
    return x == null ? null : x.toString();
  }

}
