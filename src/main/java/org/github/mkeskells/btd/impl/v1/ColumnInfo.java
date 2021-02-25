package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Coder;
import org.github.mkeskells.btd.coder.Column;
import org.github.mkeskells.btd.coder.Encoding;
import org.github.mkeskells.btd.coder.Validator;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

final class ColumnInfo {
  final String columnName;
  final boolean mandatory;
  final boolean nullable;
  /**
   * cam be null
   */
  final Validator validator;
  final Encoding<?>[] encodings;
  final Object[] encodingPrivateData;
  final ClassValue<int[]> encodingsLookup;
  final int maxEqualsCheck;
  final boolean allowOverwrite;

  public ColumnInfo(Column column,
                    Encoding<?>[] encodings, Object[] encodingPrivateData) {
    this.columnName = column.getColumnName();
    this.mandatory = column.isMandatory();
    this.nullable = column.isNullable();
    this.allowOverwrite = column.allowsOverwrite();
    this.validator = column.getValidator().orElse(null);
    this.maxEqualsCheck = Math.max(0, Math.min(PreviousValue.MAX_PREVIOUS - 1, column.getMaxEqualsCheck()));

    this.encodings = encodings;
    this.encodingPrivateData = encodingPrivateData;

    this.encodingsLookup = new ClassValue<>() {
      @Override
      protected int[] computeValue(Class<?> type) {
        int[] possibles = new int[encodings.length];
        int next = 0;
        for (int i = 0; i < encodings.length; i++) {
          Encoding<?> encoding = encodings[i];
          if (encoding.types().stream().anyMatch(cls -> cls.isAssignableFrom(type))) {
            possibles[next++] = i;
          }
        }
        return Arrays.copyOfRange(possibles, 0, next);
      }
    };
  }

  public <P> P getEncodingPrivateData(Encoding<P> encoding) {
    for (int i = 0; i < encodings.length; i++) {
      if (encodings[i] == encoding)
        return (P) encodingPrivateData[i];
    }
    throw new IllegalArgumentException(" not a known encoding");
  }

  public void validate(Object value) {
    if (validator != null)
      validator.validate(value);
  }

  public void validate(long value) {
    if (validator != null)
      validator.validate(value);
  }

  public void validate(double value) {
    if (validator != null)
      validator.validate(value);
  }

  public void validate(boolean value) {
    if (validator != null)
      validator.validate(value);
  }

  public boolean allowOverwrite() {
    return allowOverwrite;
  }

  public int maxEqualsCheck() {
    return maxEqualsCheck;
  }

  static List<ColumnInfo> createColumnInfo(Coder coder) {
    class ReusedColumnEncoding {
      final Encoding[] encodings;
      final Object[] privateData;

      public ReusedColumnEncoding(List<Encoding<?>> enc, Map<Encoding<?>, Object> privateDataCache) {
        this.encodings = enc.toArray(new Encoding[enc.size()]);
        this.privateData = new Object[enc.size()];
        for (int i = 0; i < privateData.length; i++) {
          privateData[i] = privateDataCache.computeIfAbsent(encodings[i], Encoding::generatePrivateData);
        }
      }
    }
    //private data is shared across columns
    Map<Encoding<?>, Object> privateData = new HashMap<>();
    Map<Encoding<?>, CustomEncoding<?>> untrusted = new HashMap<>();
    Map<List<Encoding<?>>, ReusedColumnEncoding> reusedColumnEncoding = new HashMap<>();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    WrappedRecordOutput untrustedRecordOutput = new WrappedRecordOutput(baos);

    List<Column> columns = coder.getColumns();
    List<ColumnInfo> result = new ArrayList(columns.size());
    for (Column column : columns) {

      List<Encoding<?>> high = column.getHighPriorityEncodings();
      List<Encoding<?>> low = column.getLowPriorityEncodings();
      if (high.size() + low.size() > CustomValue.MAX_CUSTOM_BLOCKS_INC)
        throw new IllegalArgumentException("too many custom protocols - max " + CustomValue.MAX_CUSTOM_BLOCKS_INC + " but found " + (low.size() + high.size()));

      AtomicInteger offset = new AtomicInteger();
      List<Encoding<?>> encoders = new ArrayList<>();
      for (Encoding<?> e : high) {
        encoders.add(untrusted.computeIfAbsent(e, enc -> new CustomEncoding(enc, offset.getAndIncrement(), untrustedRecordOutput)));
      }
      if (column.shouldIncludeStandardEncoders())
        encoders.addAll(InternalCoodings.all());
      for (Encoding<?> e : low) {
        encoders.add(untrusted.computeIfAbsent(e, enc -> new CustomEncoding(enc, offset.getAndIncrement(), untrustedRecordOutput)));
      }
      ReusedColumnEncoding colEncoding = reusedColumnEncoding.computeIfAbsent(encoders, all -> new ReusedColumnEncoding(all, privateData));
      result.add(new ColumnInfo(column,
          colEncoding.encodings, colEncoding.privateData));
    }
    return List.copyOf(result);
  }

  public int getEncoderIndex(Object value) {
    for (int possibleIndex : encodingsLookup.get(value.getClass())) {
      if (encodings[possibleIndex].canEncode(value))
        return possibleIndex;
    }
    throw new IllegalArgumentException();

  }
}
