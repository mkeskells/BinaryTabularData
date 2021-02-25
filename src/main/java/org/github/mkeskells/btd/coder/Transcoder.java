package org.github.mkeskells.btd.coder;

public interface Transcoder {
  String asString(Object parsedObject, long parsedLong);

  <T> T asObject(Object parsedObject, long parsedLong, Class<T> type);

  int asInt(Object parsedObject, long parsedLong);

  long asLong(Object parsedObject, long parsedLong);

  float asFloat(Object parsedObject, long parsedLong);

  double asDouble(Object parsedObject, long parsedLong);

  Number asNumber(Object parsedObject, long parsedLong);

  boolean asBoolean(Object parsedObject, long parsedLong);

  Boolean asBooleanOrNull(Object parsedObject, long parsedLong);
}
