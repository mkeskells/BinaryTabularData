package org.github.mkeskells.btd;

import java.io.IOException;

public interface Records {
  Header getHeader();

  boolean nextRow() throws IOException;

  int getInt(int columnId);

  long getLong(int columnId);

  double getDouble(int columnId);

  Number getNumber(int columnId);

  Boolean getBooleanOrNull(int columnId);

  boolean getBoolean(int columnId);

  String getString(int columnId);

  <T> T getObject(int columnId, Class<T> type);
}
