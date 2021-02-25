package org.github.mkeskells.btd;

import org.github.mkeskells.btd.impl.RecordInput;
import org.github.mkeskells.btd.impl.Tokens;
import org.github.mkeskells.btd.impl.v1.RecordsReaderImplV1;

import java.io.IOException;
import java.io.InputStream;

public class RecordReaders {
  private RecordReaders() {
  }

  public static Records createReader(InputStream in) throws IOException, ReflectiveOperationException {
    RecordInput data = new RecordInput(in);
    byte v = data.readByte();
    if (v != Tokens.VERSION)
      throw new IllegalArgumentException("Invalid Record structure - expected " + Tokens.VERSION + " got " + v);
    byte version = data.readByte();
    switch (version) {
      case 1:
        return new RecordsReaderImplV1(data);
      default:
        throw new IllegalArgumentException("Unknown Version " + version);
    }
  }
}
