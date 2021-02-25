package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.impl.RecordOutput;

import java.io.ByteArrayOutputStream;

class WrappedRecordOutput extends RecordOutput {
  final ByteArrayOutputStream underlying;

  public WrappedRecordOutput(ByteArrayOutputStream underlying) {
    super(underlying);
    this.underlying = underlying;
  }
}
