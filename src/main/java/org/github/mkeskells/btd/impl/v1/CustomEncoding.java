package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Encoding;
import org.github.mkeskells.btd.coder.FieldScratchData;
import org.github.mkeskells.btd.impl.RecordOutput;

import java.io.IOException;
import java.util.List;

final class CustomEncoding<P> extends AbstractEncoding<P> {
  private final Encoding<P> underlying;
  private final int customBlock;
  private final WrappedRecordOutput untrustedRecordOutput;

  public CustomEncoding(Encoding<P> underlying, int customBlock, WrappedRecordOutput untrustedRecordOutput) {
    this.underlying = underlying;
    this.customBlock = customBlock;
    this.untrustedRecordOutput = untrustedRecordOutput;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CustomEncoding<?> that = (CustomEncoding<?>) o;

    if (customBlock != that.customBlock) return false;
    if (!underlying.equals(that.underlying)) return false;
    return untrustedRecordOutput.equals(that.untrustedRecordOutput);
  }

  private int hash;

  @Override
  public int hashCode() {
    int result = hash;
    if (result == 0) {
      result = underlying.hashCode();
      result = 31 * result + customBlock;
      result = 31 * result + untrustedRecordOutput.hashCode();
      hash = result;
    }
    return result;
  }

  @Override
  public List<Class<?>> types() {
    return underlying.types();
  }

  @Override
  public boolean canEncode(Object value) {
    return underlying.canEncode(value);
  }

  @Override
  public P generatePrivateData() {
    return underlying.generatePrivateData();
  }

  @Override
  public void encode(RecordOutput data, Object value, FieldScratchData scratchData, P privateData) throws IOException {
    underlying.encode(untrustedRecordOutput, value, scratchData, privateData);
    Value.CUSTOM.writeData(customBlock, (RecordOutputV1) data, untrustedRecordOutput.underlying);
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return underlying.asString(parsedObject, parsedLong);
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    return underlying.asObject(parsedObject, parsedLong, type);
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    return underlying.asInt(parsedObject, parsedLong);
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    return underlying.asLong(parsedObject, parsedLong);
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return underlying.asFloat(parsedObject, parsedLong);
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return underlying.asDouble(parsedObject, parsedLong);
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    return underlying.asNumber(parsedObject, parsedLong);
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    return underlying.asBoolean(parsedObject, parsedLong);
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return underlying.asBooleanOrNull(parsedObject, parsedLong);
  }
}
