package org.github.mkeskells.btd;

import java.io.IOException;

public interface RecordWriter extends AutoCloseable {

  void write(int columnId, int value);

  void write(int columnId, Integer value);

  void write(int columnId, long value);

  void write(int columnId, Long value);

  void write(int columnId, float value);

  void write(int columnId, Float value);

  void write(int columnId, double value);

  void write(int columnId, Double value);

  void write(int columnId, String value);

  void write(int columnId, Boolean value);

  void write(int columnId, boolean value);

  void writeNull(int columnId);

  void endRow() throws IOException;

  void close(boolean closeUnderlyingStream) throws IOException;

  void close() throws IOException;

}
