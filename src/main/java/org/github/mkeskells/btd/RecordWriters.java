package org.github.mkeskells.btd;

import org.github.mkeskells.btd.coder.Coder;
import org.github.mkeskells.btd.impl.Tokens;
import org.github.mkeskells.btd.impl.v1.RecordOutputV1;
import org.github.mkeskells.btd.impl.v1.RecordsWriterImplV1;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class RecordWriters {
  private RecordWriters() {
  }

  public static RecordWriter createWriter(OutputStream out, Coder coder) throws IOException {
    return createWriter(out, 1, coder);
  }

  public static RecordWriter createWriter(OutputStream out, int version, Coder coder) throws IOException {
    Objects.requireNonNull(out, "out");

    switch (version) {
      case 1:
        RecordOutputV1 data = new RecordOutputV1(out);
        data.writeByte(Tokens.VERSION);
        data.writeByte(version);
        return new RecordsWriterImplV1(data, coder);
      default:
        throw new IllegalArgumentException("Unknown Version " + version);
    }


  }
}
