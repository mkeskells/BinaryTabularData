package org.github.mkeskells.btd.impl;

import org.github.mkeskells.btd.RecordWriter;

import java.io.IOException;

public abstract class AbstractRecordWriter implements RecordWriter {

  //forwarders
  @Override
  public void write(int columnId, int value) {
    write(columnId, (long) value);
  }


  @Override
  public void write(int columnId, Integer value) {
    if (value == null) writeNull(columnId);
    else write(columnId, value.intValue());
  }

  @Override
  public void write(int columnId, Long value) {
    if (value == null) writeNull(columnId);
    else write(columnId, value.longValue());
  }

  @Override
  public void write(int columnId, Float value) {
    if (value == null) writeNull(columnId);
    else write(columnId, value.floatValue());
  }

  @Override
  public void write(int columnId, Double value) {
    if (value == null) writeNull(columnId);
    else write(columnId, value.doubleValue());

  }

  @Override
  public void write(int columnId, String value) {
    writeObject(columnId, value);
  }

  @Override
  public void write(int columnId, Boolean value) {
    if (value == null) writeNull(columnId);
    else write(columnId, value.booleanValue());
  }

  @Override
  public void writeNull(int columnId) {
    writeObject(columnId, null);
  }


  @Override
  public void close() throws IOException {
    close(true);
  }

  /**
   * o never null
   */
  protected abstract void writeObject(int columnId, Object o);

}
