package org.github.mkeskells.btd.coder;

import org.github.mkeskells.btd.impl.RecordOutput;

import java.io.IOException;
import java.util.List;

public interface Encoding<P> extends Transcoder {
  /**
   * the list of types that this encoder can encode
   *
   * @return
   */
  List<Class<?>> types();

  /**
   * can the encoder encode the specified value.
   *
   * @param value an instance of one of the listed {@link #types()}
   * @return true if this encoder can encode this value
   */
  boolean canEncode(Object value);

  void encode(RecordOutput data, Object value, FieldScratchData scratchData, P privateData) throws IOException;

  /**
   * generate some private data to be associated with the encoding/decoding of the stream
   * The return value will be passed to {@link #encode(RecordOutput, Object, FieldScratchData, Object)} for the duration
   * of the processing of the stream.
   *
   * @return the private data for calls to {@link #encode(RecordOutput, Object, FieldScratchData, Object)}
   */
  P generatePrivateData();
}

