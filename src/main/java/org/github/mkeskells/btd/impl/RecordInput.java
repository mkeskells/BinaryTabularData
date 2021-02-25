package org.github.mkeskells.btd.impl;

import org.github.mkeskells.btd.coder.FieldScratchData;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class RecordInput {
  private final DataInputStream input;

  public RecordInput(InputStream input) {
    //short term
    this.input = new DataInputStream(input);
  }

  public byte readByte() throws IOException {
    return input.readByte();
  }

  public short readShort() throws IOException {
    return input.readShort();
  }

  public char readChar() throws IOException {
    return input.readChar();
  }

  public int readInt() throws IOException {
    return input.readInt();
  }

  public long readLong() throws IOException {
    return input.readLong();
  }

  public float readFloat() throws IOException {
    return input.readFloat();
  }

  public double readDouble() throws IOException {
    return input.readDouble();
  }

  public void readBytes(byte[] bytes, int startOffset, int length) throws IOException {
    while (length > 0) {
      int read = input.read(bytes, startOffset, length);
      if (read == -1)
        throw new EOFException();
      length -= read;
      startOffset += read;
    }
  }

  public void readChars(FieldScratchData data, char[] chars, int startOffset, int length) throws IOException {
    //TODO use readBytes(data, startOffset * 2, length * 2);
    for (int i = startOffset; i < startOffset + length; i++) {
      chars[i] = readChar();
    }
  }

  public int readUnsignedByte() throws IOException {
    int r = input.read();
    if (r < 0) throw new EOFException();
    return r;
  }

  public int readUnsignedShort() throws IOException {
    return ((int) readShort() & 0xFFFF);
  }
}
