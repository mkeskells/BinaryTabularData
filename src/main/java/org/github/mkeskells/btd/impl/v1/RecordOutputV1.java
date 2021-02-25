package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.impl.RecordOutput;

import java.io.OutputStream;

public class RecordOutputV1 extends RecordOutput {
  private final OutputStream output;

  public RecordOutputV1(OutputStream output) {
    super(output);
    this.output = output;
  }

  OutputStream underlying() {
    return output;
  }

}
