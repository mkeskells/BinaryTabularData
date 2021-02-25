package org.github.mkeskells.btd.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RecordOutput {
  private final DataOutputStream output;

  public RecordOutput(OutputStream output) {
    //short term
    this.output = new DataOutputStream(output);
  }

  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  public void write(byte[] b, int off, int len) throws IOException {
    output.write(b, off, len);
  }

  public void write(char[] c) throws IOException {
    write(c, 0, c.length);
  }

  public void write(char[] c, int off, int len) throws IOException {
    for (int i = off; i < off + len; i++) {
      writeChar(c[i]);
    }
  }

  public void writeBoolean(boolean v) throws IOException {
    output.writeBoolean(v);
  }

  public void writeByte(int v) throws IOException {
    output.writeByte(v);
  }

  public void writeShort(int v) throws IOException {
    output.writeShort(v);
  }

  public void writeChar(int v) throws IOException {
    output.writeChar(v);
  }

  public void writeInt(int v) throws IOException {
    output.writeInt(v);
  }

  public void writeLong(long v) throws IOException {
    output.writeLong(v);
  }

  public void writeFloat(float v) throws IOException {
    output.writeFloat(v);
  }

  public void writeDouble(double v) throws IOException {
    output.writeDouble(v);
  }

  public void close() throws IOException {
    output.close();
  }
}
