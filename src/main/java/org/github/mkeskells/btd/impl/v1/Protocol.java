package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Encoding;
import org.github.mkeskells.btd.coder.FieldScratchData;
import org.github.mkeskells.btd.coder.Transcoder;
import org.github.mkeskells.btd.coder.TranscoderHelper;
import org.github.mkeskells.btd.impl.RecordInput;
import org.github.mkeskells.btd.impl.RecordOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * header (mandatory)
 * <h2>Header row</h2>
 * token - HEADER_START <br/>
 * customisations -count (1byte range 0-16)<br/>
 * for each customisation<br/>
 * starting factory name, string parameter (string encoded as int length, + utf)<br/>
 * token - int - number of columns<br/>
 * for each column a String column name<br/>
 * <h2>data rows</h2>
 * Data - a series of rows, each row contains<br/>
 * token - ROW_START<br/>
 * for each column a Value.* and the data for that<br/>
 * <p>
 * end of the data denoted by a ROWS_END
 */

class Rows {
  static final byte HEADER_START = 0;
  static final byte ROW_START = 1;
  static final byte ROWS_END = 2;
}

final class NullValue extends Value implements Transcoder {

  NullValue() {
    super(0);
  }

  public void write(RecordOutput data) throws IOException {
    data.writeByte(start);
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) {
    assert firstByteLocal == 0;
    current.objects[column] = null;
    current.primitive[column] = 0L;
    current.transcoder[column] = this;
  }

  @Override
  protected String describeCode(int i) {
    return "value null - no extra bytes";
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    throw new NullPointerException("value is null");
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return null;
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    return null;
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    throw new NullPointerException("value is null");
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    throw new NullPointerException("value is null");
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    throw new NullPointerException("value is null");
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    return null;
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    throw new NullPointerException("value is null");
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return null;
  }

}

final class BooleanValue extends Value implements Transcoder {
  private final static int FALSE = 0;
  private final static int TRUE = 1;

  BooleanValue() {
    super(TRUE);
  }

  @Override
  protected String describeCode(int i) {
    switch (i) {
      case FALSE:
        return "value false - no extra bytes";
      case TRUE:
        return "value true  - no extra bytes";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  public void write(RecordOutput data, boolean value) throws IOException {
    if (value)
      data.writeByte(start + TRUE);
    else
      data.writeByte(start + FALSE);
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) {
    current.objects[column] = null;
    current.primitive[column] = firstByteLocal;
    current.transcoder[column] = this;
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return Boolean.toString(parsedLong == TRUE);
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    return TranscoderHelper.convert(type, parsedLong == TRUE);
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Integer.class, parsedLong == TRUE);
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Long.class, parsedLong == TRUE);
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Float.class, parsedLong == TRUE);
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Double.class, parsedLong == TRUE);
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Number.class, parsedLong == TRUE);
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    return parsedLong == TRUE;
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return parsedLong == TRUE;
  }
}

final class IntegerValue extends Value implements Transcoder {
  private final static int _1B = 0;
  private final static int _2B = 1;
  private final static int _4B = 2;
  private final static int _8B = 3;
  private final static int ZERO = 4;
  private final static int ONE = 5;
  private final static int MINUS_ONE = 6;

  IntegerValue() {
    super(MINUS_ONE);
  }

  @Override
  protected String describeCode(int i) {
    switch (i) {
      case _1B:
        return "1 byte value (byte)  - values between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE + " : actual value in next byte";
      case _2B:
        return "2 byte value (short) - values between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE + " : actual value in next short";
      case _4B:
        return "4 byte value (int)   - values between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + " : actual value in next int";
      case _8B:
        return "8 byte value (long)  - values between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE + " : actual value in next long";
      case ZERO:
        return " constant value      - 0  : no extra bytes";
      case ONE:
        return " constant value      - 1  : no extra bytes";
      case MINUS_ONE:
        return " constant value      - -1 : no extra bytes";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  public void write(RecordOutput data, long value) throws IOException {
    short sValue = (short) value;
    if (sValue == value) {
      byte bValue = (byte) value;
      if (bValue == sValue) {
        if (bValue == (byte) -1)
          data.writeByte(start + MINUS_ONE);
        else if (bValue == (byte) 0)
          data.writeByte(start + ZERO);
        else if (bValue == (byte) 1)
          data.writeByte(start + ONE);
        else {
          data.writeByte(start + _1B);
          data.writeByte(bValue);
        }
      } else {
        data.writeByte(start + _2B);
        data.writeShort(sValue);
      }
    } else {
      int iValue = (int) value;
      if (iValue == value) {
        data.writeByte(start + _4B);
        data.writeInt(iValue);
      } else {
        data.writeByte(start + _8B);
        data.writeLong(value);
      }
    }
  }

  public int readInt(RecordInput in) throws IOException {
    long res = readLong(in);
    if ((int) res != res) {
      throw new IllegalStateException("int expected but long found: " + res);
    }
    return (int) res;

  }

  public long readLong(RecordInput in) throws IOException {
    return readLongImpl(in, in.readUnsignedByte() - start);
  }

  private long readLongImpl(RecordInput in, int firstByteLocal) throws IOException {
    switch (firstByteLocal) {
      case _1B:
        return in.readByte();
      case _2B:
        return in.readShort();
      case _4B:
        return in.readInt();
      case _8B:
        return in.readLong();
      case ZERO:
        return 0L;
      case ONE:
        return 1L;
      case MINUS_ONE:
        return -1L;

    }
    throw new IllegalStateException("Expected an integer, but the raw value was " + (firstByteLocal + start) + ". " + describeSafe(firstByteLocal + start));
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    current.objects[column] = null;
    current.primitive[column] = readLongImpl(in, firstByteLocal);
    current.transcoder[column] = this;
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return Long.toString(parsedLong);
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    if ((type == Integer.class) ||
        (type.isAssignableFrom(Integer.class) && (int) parsedLong == parsedLong))
      return type.cast(asInt(parsedLong, type));
    return TranscoderHelper.convert(type, asNumber(parsedObject, parsedLong));
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    return asInt(parsedLong, Integer.TYPE);
  }

  private int asInt(long parsedLong, Class<?> type) {
    if ((int) parsedLong == parsedLong)
      return (int) parsedLong;
    throw new NumberFormatException("cant convert " + parsedLong + " to a " + type + " without loss of precision");
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    return parsedLong;
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return (float) parsedLong;
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return (double) parsedLong;
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    if ((int) parsedLong == parsedLong)
      return (int) parsedLong;
    return parsedLong;
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Boolean.class, asNumber(parsedObject, parsedLong));
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return asBoolean(parsedObject, parsedLong);
  }
}

final class DoubleValue extends FloatingValue {
  final static int DOUBLE = NAN + 1;
  final static int FLOAT = DOUBLE + 1;

  DoubleValue() {
    super(FLOAT);
  }

  protected String describeCode(int i) {
    switch (i) {
      case DOUBLE:
        return "double value : actual value in next double";
      case FLOAT:
        return "float value : actual value in next float";
      default:
        return super.describeCode(i);
    }
  }

  public void write(RecordOutput data, double value) throws IOException {
    if (Double.isNaN(value)) {
      data.writeByte(start + NAN);
    } else {
      float f = (float) value;
      if (f == value) {
        if (value == -1.0)
          data.writeByte(start + MINUS_ONE);
        else if (value == 0.0)
          data.writeByte(start + ZERO);
        else if (value == 1.0)
          data.writeByte(start + ONE);
        else {
          data.writeByte(start + FLOAT);
          data.writeFloat(f);
        }
      } else {
        data.writeByte(start + DOUBLE);
        data.writeDouble(value);
      }
    }
  }

  public double readDouble(RecordInput in) throws IOException {
    return readDoubleImpl(in, in.readUnsignedByte() - start);
  }

  private double readDoubleImpl(RecordInput in, int firstByteLocal) throws IOException {
    switch (firstByteLocal) {
      case DOUBLE:
        return in.readDouble();
      case FLOAT:
        return in.readFloat();
      case ZERO:
        return 0.0;
      case ONE:
        return 1.0;
      case MINUS_ONE:
        return -1.0;
      case NAN:
        return Double.NaN;

    }
    throw new IllegalStateException("Expected an double, but the raw value was " + (firstByteLocal + start) + ". " + describeSafe((firstByteLocal + start)));
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    current.objects[column] = null;
    current.primitive[column] = Double.doubleToRawLongBits(readDoubleImpl(in, firstByteLocal));
    current.transcoder[column] = this;
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return Double.toString(asDouble(parsedObject, parsedLong));
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    double d = asDouble(parsedObject, parsedLong);
    if (type == Double.class)
      return type.cast(d);
    if (type == Float.class)
      return type.cast((float) d);
    return TranscoderHelper.convert(type, d);
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return Double.longBitsToDouble((parsedLong));
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return (float) asDouble(parsedObject, parsedLong);
  }

}

final class FloatValue extends FloatingValue {
  final static int FLOAT = NAN + 1;

  FloatValue() {
    super(FLOAT);
  }

  protected String describeCode(int i) {
    switch (i) {
      case FLOAT:
        return "float value : actual value in next float";
      default:
        return super.describeCode(i);
    }
  }

  public void write(RecordOutput data, float value) throws IOException {
    if (value == -1.0f)
      data.writeByte(start + MINUS_ONE);
    else if (value == 0.0f)
      data.writeByte(start + ZERO);
    else if (value == 1.0f)
      data.writeByte(start + ONE);
    else if (Float.isNaN(value))
      data.writeByte(start + NAN);
    else {
      data.writeByte(start + FLOAT);
      data.writeFloat(value);
    }
  }

  public float readFloat(RecordInput in) throws IOException {
    return readFloatImpl(in, in.readUnsignedByte() - start);
  }

  private float readFloatImpl(RecordInput in, int firstByteLocal) throws IOException {
    switch (firstByteLocal) {
      case FLOAT:
        return in.readFloat();
      case ZERO:
        return 0.0f;
      case ONE:
        return 1.0f;
      case MINUS_ONE:
        return -1.0f;
      case NAN:
        return Float.NaN;

    }
    throw new IllegalStateException("Expected an float, but the raw value was " + (firstByteLocal + start) + ". " + describeSafe((firstByteLocal + start)));
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    current.objects[column] = null;
    current.primitive[column] = Float.floatToRawIntBits(readFloatImpl(in, firstByteLocal));
    current.transcoder[column] = this;
  }


  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return Float.toString(asFloat(parsedObject, parsedLong));
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    float f = Float.intBitsToFloat((int) parsedLong);
    if (type == Float.class)
      return type.cast(f);
    if (type == Double.class)
      return type.cast((double) f);
    return TranscoderHelper.convert(type, f);
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return Float.intBitsToFloat((int) parsedLong);
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return (double) asFloat(parsedObject, parsedLong);
  }

}

abstract class FloatingValue extends Value implements Transcoder {
  protected final static int ZERO = 0;
  protected final static int ONE = 1;
  protected final static int MINUS_ONE = 2;
  protected final static int NAN = 3;

  FloatingValue(int max) {
    super(max);
  }

  @Override
  protected String describeCode(int i) {
    switch (i) {
      case ZERO:
        return " constant value      - 0.0  : no extra bytes";
      case ONE:
        return " constant value      - 1.0  : no extra bytes";
      case MINUS_ONE:
        return " constant value      - -1.0 : no extra bytes";
      case NAN:
        return " constant value      - NaN  : no extra bytes";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    double v = asDouble(parsedObject, parsedLong);
    int i = (int) v;
    if (i == v)
      return i;
    throw new UnsupportedOperationException("cannot be narrowed to an int : " + v);
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    double v = asDouble(parsedObject, parsedLong);
    long i = (long) v;
    if (i == v)
      return i;
    throw new UnsupportedOperationException("cannot be narrowed to a long : " + v);
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    if (parsedObject == Float.class) {
      return Float.floatToRawIntBits((int) parsedLong);
    } else {
      return asDouble(parsedObject, parsedLong);
    }
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Boolean.class, asNumber(parsedObject, parsedLong));
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return asBoolean(parsedObject, parsedLong);
  }
}

final class Null {
  private Null() {
  }
}

final class StringValue extends AbstractSingleValueEncoding<Null, String> implements Transcoder {
  final static byte SHORT_BYTE_ENCODED = 0;
  final static byte SHORT_CHAR_ENCODED = 1;
  final static byte LONG_BLOCKS = 2;
  final static byte EMPTY = 3;
  final static byte BYTES_1 = EMPTY + 1;
  final static byte BYTES_2 = BYTES_1 + 1;
  final static byte BYTES_3 = BYTES_2 + 1;
  final static byte BYTES_4 = BYTES_3 + 1;

  StringValue() {
    super(BYTES_4, String.class);
  }

  private boolean areAllSmall(FieldScratchData scratch, int len) {
    for (int i = 0; i < len; i++) {
      char c = scratch.chars[i];
      if (c > 255)
        return false;
      else
        scratch.bytes[i] = (byte) c;
    }
    return true;
  }

  @Override
  protected String describeCode(int i) {
    switch (i) {
      case SHORT_BYTE_ENCODED:
        return "short byte encoded string (all char values < 256) : next byte LENGTH, LENGTH bytes";
      case SHORT_CHAR_ENCODED:
        return "short char encoded string                         : next byte LENGTH, LENGTH chars";
      case LONG_BLOCKS:
        return "long string                                       : next int length, blocks of 256 bytes/chars, last block < = 256. " +
            "Each block length (n) is either " + start + " and n bytes or " + (start + 1) + " n chars";
      case EMPTY:
        return "empty string                                      : no extra bytes";
      case BYTES_1:
        return "1 byte string (all char values < 256)             : 1 byte value of string";
      case BYTES_2:
        return "2 byte string (all char values < 256)             : 2 byte value of string";
      case BYTES_3:
        return "3 byte string (all char values < 256)             : 3 byte value of string";
      case BYTES_4:
        return "4 byte string (all char values < 256)             : 4 byte value of string";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  @Override
  public boolean canEncodeValue(String value) {
    return true;
  }

  @Override
  public Null generatePrivateData() {
    return null;
  }

  @Override
  protected void encodeValue(RecordOutput data, String value, FieldScratchData scratchData, Null privateData) throws IOException {
    int len = value.length();
    if (len <= 256) {
      //short string encoding
      value.getChars(0, len, scratchData.chars, 0);
      if (areAllSmall(scratchData, len)) {
        if (len <= 4) {
          data.writeByte(start + EMPTY + len);
        } else {
          data.writeByte(start + SHORT_BYTE_ENCODED);
          data.writeByte(len);
        }
        data.write(scratchData.bytes, 0, len);
      } else {
        data.writeByte(start + SHORT_CHAR_ENCODED);
        data.writeByte(len);
        data.write(scratchData.chars, 0, len);
      }
    } else {
      data.writeByte(start + LONG_BLOCKS);
      data.writeInt(len);
      int blockStart = 0;
      while (blockStart < len) {
        int fetch = Math.min(256, len - blockStart);
        value.getChars(blockStart, blockStart + fetch, scratchData.chars, 0);
        if (areAllSmall(scratchData, fetch)) {
          data.writeByte(start + SHORT_BYTE_ENCODED);
          data.write(scratchData.bytes, 0, fetch);
        } else {
          data.writeByte(start + SHORT_CHAR_ENCODED);
          data.write(scratchData.chars, 0, fetch);
        }
        blockStart += fetch;
      }
    }
  }

  private void bytesToChars(FieldScratchData scratch, char[] chars, int destStart, int length) {
    for (int i = 0; i < length; i++) {
      chars[i + destStart] = (char) ((short) scratch.bytes[i] & 0x00FF);
    }
  }

  public String readStringNotNull(RecordInput in, FieldScratchData scratch) throws IOException {
    return Objects.requireNonNull(readStringOrNull(in, scratch));
  }

  public String readStringOrNull(RecordInput in, FieldScratchData scratch) throws IOException {
    int firstByte = in.readUnsignedByte();
    if (firstByte == 0) return null;
    else return readString(in, firstByte - start, scratch);
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    String v = readString(in, firstByteLocal, scratch);
    current.objects[column] = v;
    current.primitive[column] = 0L;
    current.transcoder[column] = this;
  }


  private String readString(RecordInput in, int firstByteLocal, FieldScratchData scratch) throws IOException {
    switch (firstByteLocal) {
      case SHORT_BYTE_ENCODED: {
        int length = in.readUnsignedByte();
        return readShortString(in, scratch, length);
      }
      case SHORT_CHAR_ENCODED: {
        int length = in.readUnsignedByte();
        in.readChars(scratch, scratch.chars, 0, length);
        return new String(scratch.chars, 0, length);
      }
      case LONG_BLOCKS: {
        int length = in.readInt();
        char[] result = new char[length];
        int offset = 0;
        while (offset < length) {
          int blockType = in.readUnsignedByte();
          int toRead = Math.min(256, length - offset);
          switch (blockType - start) {
            case SHORT_BYTE_ENCODED:
              in.readBytes(scratch.bytes, 0, toRead);
              bytesToChars(scratch, result, offset, toRead);
              break;
            case SHORT_CHAR_ENCODED:
              in.readChars(scratch, result, offset, toRead);
              break;
            default:
              throw new IllegalStateException("Expected an String block, but the raw value was " + (blockType) + ". ");
          }
        }
        return new String(result);
      }
      case EMPTY:
        return "";
      case BYTES_1:
        return readShortString(in, scratch, 1);
      case BYTES_2:
        return readShortString(in, scratch, 2);
      case BYTES_3:
        return readShortString(in, scratch, 3);
      case BYTES_4:
        return readShortString(in, scratch, 4);
    }
    throw new IllegalArgumentException("Expected an String, but the raw value was " + (firstByteLocal + start) + ". " + describeSafe((firstByteLocal + start)));
  }

  private String readShortString(RecordInput data, FieldScratchData scratch, int length) throws IOException {
    data.readBytes(scratch.bytes, 0, length);
    bytesToChars(scratch, scratch.chars, 0, length);
    return new String(scratch.chars, 0, length);
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return (String) parsedObject;
  }

}

final class PreviousValue extends Value {
  public final static int MAX_PREVIOUS = 16;

  PreviousValue() {
    super(MAX_PREVIOUS);
  }

  protected String describeCode(int i) {
    return "the previous " + (i + 1) + " value in this column";
  }

  public void write(RecordOutput data, int prevIndex) throws IOException {
    assert (prevIndex >= 0);
    assert (prevIndex < size);
    data.writeByte(start + prevIndex);
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    assert (firstByteLocal >= 0);
    assert (firstByteLocal < size);

    current.objects[column] = prev[firstByteLocal].objects[column];
    current.primitive[column] = prev[firstByteLocal].primitive[column];
    current.transcoder[column] = prev[firstByteLocal].transcoder[column];
    current.generatedType[column] = prev[firstByteLocal].generatedType[column];
    current.generated[column] = prev[firstByteLocal].generated[column];
  }

}

final class EnumValue extends AbstractSingleValueEncoding<EnumValue.LocalData, Enum> {

  final static class LocalData {
    final Null stringPrivateData;
    //write data
    private final Map<Enum, Integer> writeEnums;
    private final Map<Class<? extends Enum>, Integer> writeClasses;

    private int writeNextClassId;
    private int writeNextEnumId;

    //read data
    private final List<Class<? extends Enum>> readClassList;
    private final List<Enum<?>> readEnumList;

    private LocalData() {
      stringPrivateData = Value.STRING.generatePrivateData();
      //write data
      writeEnums = new HashMap<>();
      writeClasses = new HashMap<>();

      writeNextClassId = 0;
      writeNextEnumId = 0;

      //read data
      readClassList = new ArrayList<>();
      readEnumList = new ArrayList<>();
    }

    public int getOrCreateEnumId(Enum value) {
      Integer res = writeEnums.get(value);
      if (res != null)
        return res;
      res = writeNextEnumId++;
      writeEnums.put(value, res);
      return -1;
    }

    public int getOrCreateEnumClassId(Class<? extends Enum> cls) {
      Integer res = writeClasses.get(cls);
      if (res != null)
        return res;
      res = writeNextClassId++;
      writeClasses.put(cls, res);
      return -1;
    }

    public int registerClass(String className) throws IOException {
      try {
        readClassList.add(Class.forName(className).asSubclass(Enum.class));
        return readClassList.size() - 1;
      } catch (ClassNotFoundException cnfe) {
        throw new IOException(cnfe);
      }
    }

    public Enum registerEnum(int classId, String enumName) {
      Enum r = Enum.valueOf(readClassList.get(classId), enumName);
      readEnumList.add(r);
      return r;
    }

    public Enum getEnum(int enumId) {
      return readEnumList.get(enumId);
    }
  }

  public final static int DECLARE_CLASS = 0;
  public final static int DECLARE_VALUE_1 = 1;
  public final static int DECLARE_VALUE_2 = 2;
  public final static int DECLARE_VALUE_4 = 3;
  public final static int VALUE_1 = 4;
  public final static int VALUE_2 = 5;
  public final static int VALUE_4 = 6;

  EnumValue() {
    super(VALUE_4, Enum.class);
  }

  protected String describeCode(int i) {
    switch (i) {
      case DECLARE_CLASS:
        return "A new enum class and value - Next value = (String) class name,(String) enum value" +
            "\n    - The class name is associated with the next class id" +
            "\n    - The enum  name is associated with the next enum id";
      case DECLARE_VALUE_1:
        return "Declare a new enum value - Next value = (unsigned byte) class ID,(String) enum value ";
      case DECLARE_VALUE_2:
        return "Declare a new enum value - Next value = (unsigned short) class ID,(String) enum value ";
      case DECLARE_VALUE_4:
        return "Declare a new enum value - Next value = (int) class ID,(String) enum value ";
      case VALUE_1:
        return "An existing enum - Next value = (unsigned byte) enum id";
      case VALUE_2:
        return "An existing enum - Next value = (unsigned short) enum id";
      case VALUE_4:
        return "An existing enum - Next value = (int) enum id";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  @Override
  public LocalData generatePrivateData() {
    return new LocalData();
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    LocalData privateData = Objects.requireNonNull(columnInfo.getEncodingPrivateData(this));
    switch (firstByteLocal) {
      case DECLARE_CLASS:
        current.objects[column] = privateData.registerEnum(privateData.registerClass(Value.STRING.readStringNotNull(in, scratch)), Value.STRING.readStringNotNull(in, scratch));
        break;

      case DECLARE_VALUE_1:
        current.objects[column] = privateData.registerEnum(in.readUnsignedByte(), Value.STRING.readStringNotNull(in, scratch));
        break;

      case DECLARE_VALUE_2:
        current.objects[column] = privateData.registerEnum(in.readUnsignedShort(), Value.STRING.readStringNotNull(in, scratch));
        break;

      case DECLARE_VALUE_4:
        current.objects[column] = privateData.registerEnum(in.readInt(), Value.STRING.readStringNotNull(in, scratch));
        break;

      case VALUE_1:
        current.objects[column] = privateData.getEnum(in.readUnsignedByte());
        break;

      case VALUE_2:
        current.objects[column] = privateData.getEnum(in.readUnsignedShort());
        break;

      case VALUE_4:
        current.objects[column] = privateData.getEnum(in.readInt());
        break;

      default:
        throw new IllegalStateException("code error");
    }
  }

  @Override
  protected void encodeValue(RecordOutput data, Enum value, FieldScratchData scratchData, LocalData privateData) throws IOException {
    int enumId = privateData.getOrCreateEnumId(value);
    if (enumId >= 0) {
      if (enumId < 0x100) {
        data.writeByte(start + VALUE_1);
        data.writeByte(enumId);
      } else if (enumId < 0x10000) {
        data.writeByte(start + VALUE_2);
        data.writeShort(enumId);
      } else {
        data.writeByte(start + VALUE_4);
        data.writeInt(enumId);
      }
    } else {
      int classId = privateData.getOrCreateEnumClassId(value.getClass());
      if (classId >= 0) {
        if (classId < 0x100) {
          data.writeByte(start + DECLARE_VALUE_1);
          data.writeByte(classId);
        } else if (classId < 0x10000) {
          data.writeByte(start + DECLARE_VALUE_2);
          data.writeShort(classId);
        } else {
          data.writeByte(start + DECLARE_VALUE_4);
          data.writeInt(classId);
        }
        Value.STRING.encodeValue(data, value.name(), scratchData, privateData.stringPrivateData);
      } else {
        //new class, new enum
        data.writeByte(start + DECLARE_CLASS);
        Value.STRING.encodeValue(data, value.getClass().getName(), scratchData, privateData.stringPrivateData);
        Value.STRING.encodeValue(data, value.name(), scratchData, privateData.stringPrivateData);
      }
    }
  }

  @Override
  protected boolean canEncodeValue(Enum value) {
    return true;
  }

}

final class TemporalValue extends AbstractSingleValueEncoding<TemporalValue.LocalData, Temporal> {

  final static class LocalData {
    final Null stringPrivateData;
    //write data
    private final Map<ZoneOffset, Integer> writeOffsets;
    private final Map<ZoneId, Integer> writeZones;

    private int writeNextZoneOffsetId;
    private int writeNextZoneId;

    //read data
    private final List<ZoneOffset> readOffsets;
    private final List<ZoneId> readZoneIds;

    private LocalData() {
      stringPrivateData = Value.STRING.generatePrivateData();
      //write data
      writeOffsets = new HashMap<>();
      writeZones = new HashMap<>();

      writeNextZoneOffsetId = 0;
      writeNextZoneId = 0;

      //read data
      readOffsets = new ArrayList<>();
      readZoneIds = new ArrayList<>();

      getOrCreateOffset(ZoneOffset.UTC);
      getOrCreateZone(ZoneOffset.UTC);
      readOffsets.add(ZoneOffset.UTC);
      readZoneIds.add(ZoneOffset.UTC);
    }

    public int getOrCreateOffset(ZoneOffset value) {
      Integer res = writeOffsets.get(value);
      if (res != null)
        return res;
      res = writeNextZoneOffsetId++;
      writeOffsets.put(value, res);
      return -1;
    }

    public int getOrCreateZone(ZoneId value) {
      Integer res = writeZones.get(value);
      if (res != null)
        return res;
      res = writeNextZoneId++;
      writeZones.put(value, res);
      return -1;
    }

    public int registerZoneOffset(ZoneOffset zoneOffset) throws IOException {
      readOffsets.add(zoneOffset);
      return readOffsets.size() - 1;
    }

    public int registerZone(ZoneId value) {
      readZoneIds.add(value);
      return readZoneIds.size() - 1;
    }

  }

  public final static int LOCAL_DATE = 0;
  public final static int LOCAL_TIME = 1;
  public final static int LOCAL_DATETIME = 2;
  public final static int INSTANT = 3;
  public final static int OFFSET_TIME = 4;
  public final static int OFFSET_DATETIME = 5;
  public final static int ZONED_DATETIME = 6;

  TemporalValue() {
    super(ZONED_DATETIME, Temporal.class);
  }

  protected String describeCode(int i) {
    switch (i) {
      case LOCAL_DATE:
        return "A LocalDate - Next value = (int) year,(byte) month, (byte) day";
      case LOCAL_TIME:
        return "A LocalTime - Next value = (byte) hour of day,(byte) minute of hour,(byte) second of minute,(cint) nanosOfSecond";
      case LOCAL_DATETIME:
        return "A LocalDateTime - Next value = (int) year,(byte) month, (byte) day, (byte) hour of day,(byte) minute of hour,(byte) second of minute,(cint) nanosOfSecond";
      case INSTANT:
        return "An Instant - Next value = (long) epoch seconds, (int) nanosOfSecond";
      case OFFSET_TIME:
        return "A OffsetTime - Next value = (int) seconds of day,(cint) nanosOfSecond, offset";
      case OFFSET_DATETIME:
        return "A OffsetDateTime - Next value = (int) year,(byte) month, (byte) day, (byte) hour of day,(byte) minute of hour,(byte) second of minute,(cint) nanosOfSecond, offset";
      case ZONED_DATETIME:
        return "A ZonedDateTime - Next value = (int) year,(byte) month, (byte) day, (byte) hour of day,(byte) minute of hour,(byte) second of minute,(cint) nanosOfSecond, offset, zone";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  @Override
  public LocalData generatePrivateData() {
    return new LocalData();
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    LocalData privateData = Objects.requireNonNull(columnInfo.getEncodingPrivateData(this));
    switch (firstByteLocal) {
      case LOCAL_DATE: {
        int year = in.readInt();
        int month = in.readUnsignedByte();
        int day = in.readUnsignedByte();
        current.objects[column] = LocalDate.of(year, month, day);
        break;
      }
      case LOCAL_TIME: {
        int hour = in.readUnsignedByte();
        int minute = in.readUnsignedByte();
        int seconds = in.readUnsignedByte();
        int nanos = readCompressedNanos(in);
        current.objects[column] = LocalTime.of(hour, minute, seconds, nanos);
        break;
      }
      case LOCAL_DATETIME: {
        int year = in.readInt();
        int month = in.readUnsignedByte();
        int day = in.readUnsignedByte();
        int hour = in.readUnsignedByte();
        int minute = in.readUnsignedByte();
        int seconds = in.readUnsignedByte();
        int nanos = readCompressedNanos(in);
        current.objects[column] = LocalDateTime.of(year, month, day, hour, minute, seconds, nanos);
        break;
      }
      case INSTANT: {
        long epochSeconds = in.readLong();
        int nanos = readCompressedNanos(in);
        current.objects[column] = Instant.ofEpochSecond(epochSeconds, nanos);
        break;
      }
      case OFFSET_TIME: {
        int hour = in.readUnsignedByte();
        int minute = in.readUnsignedByte();
        int seconds = in.readUnsignedByte();
        int nanos = readCompressedNanos(in);
        ZoneOffset offset = readZoneOffset(in, privateData, null);
        current.objects[column] = OffsetTime.of(hour, minute, seconds, nanos, offset);
        break;
      }
      case OFFSET_DATETIME: {
        int year = in.readInt();
        int month = in.readUnsignedByte();
        int day = in.readUnsignedByte();
        int hour = in.readUnsignedByte();
        int minute = in.readUnsignedByte();
        int seconds = in.readUnsignedByte();
        int nanos = readCompressedNanos(in);
        ZoneOffset offset = readZoneOffset(in, privateData, null);
        current.objects[column] = OffsetDateTime.of(year, month, day, hour, minute, seconds, nanos, offset);
        break;
      }
      case ZONED_DATETIME: {
        int year = in.readInt();
        int month = in.readUnsignedByte();
        int day = in.readUnsignedByte();
        int hour = in.readUnsignedByte();
        int minute = in.readUnsignedByte();
        int seconds = in.readUnsignedByte();
        int nanos = readCompressedNanos(in);
        ZoneId zoneId = readZone(in, scratch, privateData);
        ZoneOffset offset = readZoneOffset(in, privateData, zoneId);
        current.objects[column] = ZonedDateTime.ofLocal(LocalDateTime.of(year, month, day, hour, minute, seconds, nanos), zoneId, offset);
        break;
      }

      default:
        throw new IllegalStateException("code error");
    }
  }

  @Override
  protected void encodeValue(RecordOutput data, Temporal value, FieldScratchData scratchData, LocalData privateData) throws IOException {
    if (value instanceof LocalDate) {
      data.writeByte(start + LOCAL_DATE);
      encodeDate(data, value);
    } else if (value instanceof LocalTime) {
      data.writeByte(start + LOCAL_TIME);
      encodeTime(data, value);
    } else if (value instanceof LocalDateTime) {
      data.writeByte(start + LOCAL_DATETIME);
      encodeDate(data, value);
      encodeTime(data, value);
    } else if (value instanceof Instant) {
      data.writeByte(start + INSTANT);
      encodeInstant(data, (Instant) value);
    } else if (value instanceof OffsetTime) {
      data.writeByte(start + OFFSET_TIME);
      encodeTime(data, value);
      encodeZoneOffset(data, ((OffsetTime) value).getOffset(), null, privateData);
    } else if (value instanceof OffsetDateTime) {
      data.writeByte(start + OFFSET_DATETIME);
      encodeDate(data, value);
      encodeTime(data, value);
      encodeZoneOffset(data, ((OffsetDateTime) value).getOffset(), null, privateData);
    } else if (value instanceof ZonedDateTime) {
      data.writeByte(start + ZONED_DATETIME);
      encodeDate(data, value);
      encodeTime(data, value);
      ZonedDateTime zdt = (ZonedDateTime) value;
      encodeZone(data, zdt.getZone(), scratchData, privateData);
      encodeZoneOffset(data, zdt.getOffset(), zdt.getZone(), privateData);
    } else {
      throw new IllegalStateException("Unexpected value <" + value.getClass() + "> - " + value);
    }
  }

  final static int MAX_SIMPLE_ID = 200;
  final static int SHORT_ID = 240;
  final static int INT_ID = 241;
  final static int OFFSET_IS_ZONE = 252;
  final static int DECLARE_POFFSET = 253;
  final static int DECLARE_NOFFSET = 254;
  final static int DECLARE_ZONE = 255;

  private void writeId(RecordOutput data, int id) throws IOException {
    if (id < MAX_SIMPLE_ID) {
      data.writeByte(id);
    } else if (id < 0x10000) {
      data.writeByte(SHORT_ID);
      data.writeShort(id);
    } else {
      data.writeByte(INT_ID);
      data.writeInt(id);
    }
  }

  private ZoneOffset readZoneOffset(RecordInput data, LocalData privateData, ZoneId zoneId) throws IOException {

    int id = data.readUnsignedByte();
    if (id < MAX_SIMPLE_ID)
      return privateData.readOffsets.get(id);

    final int index;
    switch (id) {
      case SHORT_ID:
        index = data.readUnsignedShort();
        break;
      case INT_ID:
        index = data.readInt();
        break;
      case DECLARE_POFFSET:
        index = privateData.registerZoneOffset(ZoneOffset.ofTotalSeconds(data.readUnsignedShort()));
        break;
      case DECLARE_NOFFSET:
        index = privateData.registerZoneOffset(ZoneOffset.ofTotalSeconds(-data.readUnsignedShort()));
        break;
      case OFFSET_IS_ZONE:
        return (ZoneOffset) zoneId;
      default:
        throw new IllegalStateException("id " + id);
    }
    return privateData.readOffsets.get(index);
  }

  private void encodeZoneOffset(RecordOutput data, ZoneOffset offset, ZoneId zoneOrNull, LocalData privateData) throws IOException {
    if (zoneOrNull == offset)
      data.writeByte(OFFSET_IS_ZONE);
    else {
      int id = privateData.getOrCreateOffset(offset);
      if (id == -1) {
        int sec = offset.getTotalSeconds();
        data.writeByte(sec >= 0 ? DECLARE_POFFSET : DECLARE_NOFFSET);
        data.writeShort(Math.abs(sec));
      } else
        writeId(data, id);
    }
  }

  private void encodeZone(RecordOutput data, ZoneId zone, FieldScratchData scratchData, LocalData privateData) throws IOException {
    int id = privateData.getOrCreateZone(zone);
    if (id == -1) {
      if (zone instanceof ZoneOffset) {
        ZoneOffset offset = (ZoneOffset) zone;
        int sec = offset.getTotalSeconds();
        data.writeByte(sec >= 0 ? DECLARE_POFFSET : DECLARE_NOFFSET);
        data.writeShort(Math.abs(sec));
      } else {
        data.writeByte(DECLARE_ZONE);
        Value.STRING.encodeValue(data, zone.getId(), scratchData, privateData.stringPrivateData);
      }
    } else
      writeId(data, id);
  }

  private ZoneId readZone(RecordInput data, FieldScratchData scratchData, LocalData privateData) throws IOException {
    int id = data.readUnsignedByte();
    if (id < MAX_SIMPLE_ID)
      return privateData.readZoneIds.get(id);

    final int index;
    switch (id) {
      case SHORT_ID:
        index = data.readUnsignedShort();
        break;
      case INT_ID:
        index = data.readInt();
        break;
      case DECLARE_POFFSET:
        index = privateData.registerZone(ZoneOffset.ofTotalSeconds(data.readUnsignedShort()));
        break;
      case DECLARE_NOFFSET:
        index = privateData.registerZone(ZoneOffset.ofTotalSeconds(-data.readUnsignedShort()));
        break;
      case DECLARE_ZONE:
        index = privateData.registerZone(ZoneId.of(Value.STRING.readStringNotNull(data, scratchData)));
        break;
      default:
        throw new IllegalStateException("id " + id);
    }

    return privateData.readZoneIds.get(index);
  }

  private void encodeDate(RecordOutput data, Temporal value) throws IOException {
    data.writeInt(value.get(ChronoField.YEAR));
    data.writeByte(value.get(ChronoField.MONTH_OF_YEAR));
    data.writeByte(value.get(ChronoField.DAY_OF_MONTH));
  }

  private void encodeTime(RecordOutput data, Temporal value) throws IOException {
    data.writeByte(value.get(ChronoField.HOUR_OF_DAY));
    data.writeByte(value.get(ChronoField.MINUTE_OF_HOUR));
    data.writeByte(value.get(ChronoField.SECOND_OF_MINUTE));
    writeCompressedNanos(data, value);
  }

  private void writeCompressedNanos(RecordOutput data, Temporal value) throws IOException {
    int nanos = value.get(ChronoField.NANO_OF_SECOND);
    if (nanos == 0)
      data.writeByte(0xFF);
    else
      data.writeInt(nanos);
  }

  private int readCompressedNanos(RecordInput data) throws IOException {
    int flagOrData = data.readUnsignedByte();
    if (flagOrData == 0xFF)
      return 0;
    int b3 = data.readUnsignedByte();
    int rest = data.readUnsignedShort();
    return flagOrData << 24 | b3 << 16 | rest;
  }

  private void encodeInstant(RecordOutput data, Instant value) throws IOException {
    data.writeLong(value.getEpochSecond());
    writeCompressedNanos(data, value);
  }

  @Override
  protected boolean canEncodeValue(Temporal value) {
    return true;
  }

}

final class CustomValue extends Value {
  final static int MAX_CUSTOM_BLOCKS_INC = 4;
  private final static byte _1B = 0;
  private final static byte _2B = 1;
  private final static byte _3B = 2;
  private final static byte _4B = 3;
  private final static byte TINY = 4;
  private final static byte SMALL = 5;
  private final static byte LARGE = 6;
  final static int BLOCK_SIZE = LARGE + 1;

  CustomValue() {
    super((MAX_CUSTOM_BLOCKS_INC) * BLOCK_SIZE - 1);
  }

  @Override
  protected String describeCode(int i) {
    int inBlock = i % BLOCK_SIZE;
    int blockNumber = i / BLOCK_SIZE;
    String c = "Custom block #" + blockNumber + ", ";
    switch (inBlock) {
      case _1B:
        return c + "1 byte custom value : value encoded in next 1 byte";
      case _2B:
        return c + "2 byte custom value : value encoded in next 2 bytes";
      case _3B:
        return c + "3 byte custom value : value encoded in next 3 byte";
      case _4B:
        return c + "4 byte custom value : value encoded in next 4 byte";
      case TINY:
        return c + "tiny custom value   : length (< 256) in next byte, length bytes";
      case SMALL:
        return c + "small custom value  : length (< 65536) in next short, length bytes";
      case LARGE:
        return c + "large custom value  : length in next int, length bytes";
    }
    throw new IllegalArgumentException(Integer.toString(i));
  }

  @Override
  protected void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException {
    int inBlock = firstByteLocal % BLOCK_SIZE;
    int blockNumber = firstByteLocal / BLOCK_SIZE;
//    columnInfo.
    throw new IllegalStateException("TODO");
  }

  void writeData(int encoderNumber, RecordOutputV1 recordOutput, ByteArrayOutputStream from) throws IOException {
    int size = from.size();
    byte base = (byte) (start + encoderNumber * BLOCK_SIZE);
    if (size < 256) {
      switch (size) {
        case 1:
          recordOutput.writeByte(base + _1B);
          break;
        case 2:
          recordOutput.writeByte(base + _2B);
          break;
        case 3:
          recordOutput.writeByte(base + _3B);
          break;
        case 4:
          recordOutput.writeByte(base + _4B);
          break;
        default:
          recordOutput.writeByte(base + TINY);
          recordOutput.writeByte(size);
          break;
      }
    } else if (size < 65536) {
      recordOutput.writeByte(base + SMALL);
      recordOutput.writeShort(size);
    } else {
      recordOutput.writeByte(base + LARGE);
      recordOutput.writeInt(size);
    }
    from.writeTo(recordOutput.underlying());
    from.reset();
  }
}

abstract class Value {
  private static int next = 0;
  private static Value[] allValues = new Value[256];
  final int start;
  final int endExcl;
  final int size;

  Value(int size) {
    if (next + size > 256 || size > 256)
      throw new IllegalStateException("too many values");
    if (size < 0)
      throw new IllegalStateException("no size");
    this.start = next;
    this.endExcl = next + size + 1;
    for (int i = start; i < endExcl; i++) {
      allValues[i] = this;
    }
    this.size = size;
    next += size + 1;
  }

  protected static String describeSafe(int code) {
    if (code < 0 || code > SIZE) {
      return " Value out of range - 0 .. " + SIZE;
    }
    Value value = allValues[code];
    return value.describeCode(code - value.start);
  }

  static String describe(int code) {
    if (code < 0 || code > SIZE) {
      throw new IllegalArgumentException();
    }
    Value value = allValues[code];
    return value.describeCode(code - value.start);
  }

  protected abstract String describeCode(int i);

  final static NullValue NULL = new NullValue();
  final static BooleanValue BOOLEAN = new BooleanValue();
  final static IntegerValue INTEGER = new IntegerValue();
  final static FloatValue FLOAT = new FloatValue();
  final static DoubleValue DOUBLE = new DoubleValue();
  final static StringValue STRING = new StringValue();
  final static PreviousValue PREVIOUS = new PreviousValue();
  final static EnumValue ENUM = new EnumValue();
  final static TemporalValue DATE = new TemporalValue();
  final static CustomValue CUSTOM = new CustomValue();

  public final static int SIZE = next;

  static {
    allValues = Arrays.copyOfRange(allValues, 0, SIZE);
  }

  public static void decodeRow(RecordInput in, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, List<ColumnInfo> columnsInfo) throws IOException {
    for (int col = 0; col < current.colCount; col++) {
      int code = in.readUnsignedByte();
      Value value = allValues[code];
      value.decodeField(col, in, code - value.start, current, prev, scratch, columnsInfo.get(col));
    }
  }

  protected abstract void decodeField(int column, RecordInput in, int firstByteLocal, ReadDataRow current, ReadDataRow[] prev, FieldScratchData scratch, ColumnInfo columnInfo) throws IOException;

}

abstract class AbstractSingleValueEncoding<P, T> extends Value implements Encoding<P> {
  private final List<Class<?>> types;

  AbstractSingleValueEncoding(int size, Class<T> type) {
    super(size);
    this.types = List.of(type);
  }

  @Override
  public List<Class<?>> types() {
    return types;
  }

  protected abstract void encodeValue(RecordOutput data, T value, FieldScratchData scratchData, P privateData) throws IOException;

  @Override
  public final void encode(RecordOutput data, Object value, FieldScratchData scratchData, P privateData) throws IOException {
    encodeValue(data, (T) value, scratchData, privateData);
  }

  protected abstract boolean canEncodeValue(T value);

  @Override
  public final boolean canEncode(Object value) {
    return canEncodeValue((T) value);
  }

  @Override
  public String asString(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(String.class, parsedObject);
  }

  @Override
  public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
    return TranscoderHelper.convert(type, parsedObject);
  }

  @Override
  public int asInt(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Integer.class, parsedObject);
  }

  @Override
  public long asLong(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Long.class, parsedObject);
  }

  @Override
  public float asFloat(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Float.class, parsedObject);
  }

  @Override
  public double asDouble(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Double.class, parsedObject);
  }

  @Override
  public Number asNumber(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Number.class, parsedObject);
  }

  @Override
  public boolean asBoolean(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Boolean.class, parsedObject);
  }

  @Override
  public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
    return TranscoderHelper.convert(Boolean.class, parsedObject);
  }


}

public class Protocol {

  public static void main(String... args) {
    System.out.println("field values");
    for (int i = 0; i < Value.SIZE; i++)
      System.out.println("" + i + " -> " + Value.describe(i));
  }
}