package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Column;
import org.github.mkeskells.btd.coder.Encoding;
import org.github.mkeskells.btd.coder.FieldScratchData;
import org.github.mkeskells.btd.impl.RecordInput;
import org.github.mkeskells.btd.impl.RecordOutput;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
  @FunctionalInterface
  interface Writer<T> {
    void write(RecordOutput data, T value, FieldScratchData scratch) throws IOException;
  }

  @FunctionalInterface
  interface Reader<T> {
    T read(RecordInput data, FieldScratchData scratch) throws IOException;
  }

  protected <P, T> void encode(T value, Writer<T> encode, int... expectedAsInt) throws IOException {
    byte[] expected = new byte[expectedAsInt.length];
    for (int i = 0; i < expected.length; i++) {
      expected[i] = (byte) expectedAsInt[i];
    }
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    FieldScratchData scratch = new FieldScratchData();
    RecordOutput out = new RecordOutput(os);
    encode.write(out, value, scratch);

    assertArrayEquals(expected, os.toByteArray(), "\n actual   - " + getBytesString(os.toByteArray()) + "\n expected - " + getBytesString(expected));
  }

  private ColumnInfo columnInfo;

  @BeforeEach
  void clearColumn() {
    columnInfo = null;
  }

  protected boolean roundTripWithString() {
    return true;
  }

  protected <P, C, T extends C> T roundTrip(Value valueCoder, T value, Class<C> clazz, Writer<? super T> encode) throws IOException {
    int columnNumber = 10;
    ReadDataRow dataRow = roundTripCore(valueCoder, value, clazz, encode, columnNumber);
    T res = (T) (dataRow.transcoder[columnNumber].asObject(dataRow.objects[columnNumber], dataRow.primitive[columnNumber], clazz));

    if (roundTripWithString()) {
      String r1 = dataRow.transcoder[columnNumber].asObject(dataRow.objects[columnNumber], dataRow.primitive[columnNumber], String.class);
      String r2 = dataRow.transcoder[columnNumber].asString(dataRow.objects[columnNumber], dataRow.primitive[columnNumber]);
      assertEquals(r1, r2);
      assertEquals(value.toString(), r1);
    }

    return res;
  }

  protected <P, T> String roundTripToString(Value valueCoder, T value, Writer<? super T> encode) throws IOException {
    int columnNumber = 10;
    ReadDataRow dataRow = roundTripCore(valueCoder, value, Object.class, encode, columnNumber);

    String r1 = dataRow.transcoder[columnNumber].asObject(dataRow.objects[columnNumber], dataRow.primitive[columnNumber], String.class);
    String r2 = dataRow.transcoder[columnNumber].asString(dataRow.objects[columnNumber], dataRow.primitive[columnNumber]);
    assertEquals(r1, r2);
    return r1;
  }

  protected <P, C, T extends C> ReadDataRow roundTripCore(Value valueCoder, T value, Class<C> clazz, Writer<? super T> encode, int columnNumber) throws IOException {
    int columnCount = columnNumber * 2;
    if (columnInfo == null) {
      Encoding<?>[] encodings = new Encoding<?>[columnCount];
      Object[] encodingPrivateData = new Object[columnCount];
      Arrays.fill(encodings, new UnusedEncoding());
      if (valueCoder instanceof Encoding) {
        encodings[columnNumber] = (Encoding) valueCoder;
        encodingPrivateData[columnNumber] = ((Encoding<?>) valueCoder).generatePrivateData();
      }
      Column column = new Column("TEST", false, false, true, Optional.empty(), 100, null, null, false);
      columnInfo = new ColumnInfo(column, encodings, encodingPrivateData);
    } else {
      if (valueCoder instanceof Encoding) {
        assertSame(valueCoder, columnInfo.encodings[columnNumber], "cant change encoding");
      } else {
        assertNotSame(valueCoder, columnInfo.encodings[columnNumber], "cant change encoding");
      }
    }
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    FieldScratchData scratch = new FieldScratchData();
    RecordOutput out = new RecordOutput(os);
    encode.write(out, value, scratch);

    byte[] bytes = os.toByteArray();
    assertTrue(bytes.length > 0);

    String bytesString = getBytesString(bytes);

    byte type = bytes[0];
    assertTrue(valueCoder.start <= type);
    assertTrue(valueCoder.endExcl > type);

    ReadDataRow currentRead = new ReadDataRow(columnNumber * 2);
    currentRead.transcoder[columnNumber] = columnInfo.encodings[columnNumber];

    ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
    RecordInput in = new RecordInput(bin);
    valueCoder.decodeField(columnNumber, in, in.readUnsignedByte() - valueCoder.start, currentRead, null, scratch, columnInfo);
    for (int i = 0; i < currentRead.colCount; i++) {
      if (i != columnNumber) {
        assertEquals(null, currentRead.objects[i]);
        assertEquals(0, currentRead.primitive[i]);
        assertEquals(null, currentRead.transcoder[i]);
      }
    }
    Object result = currentRead.transcoder[columnNumber].asObject(currentRead.objects[columnNumber], currentRead.primitive[columnNumber], clazz);

    assertEquals(value, result, " serial was " + bytesString);

    assertEquals(0, bin.available(), "Expected EOF");
    return currentRead;

  }

  private String getBytesString(byte[] bytes) {
    StringBuilder list = new StringBuilder();
    for (byte x : bytes) {
      list.append(Integer.toHexString(((int) x) & 0xFF));
      if (x > ' ' && x < '~')
        list.append(" '" + (char) x + "'");
      list.append(", ");
    }
    list.setLength(list.length() - 2);
    String bytesString = list.toString();
    return bytesString;
  }

  private class UnusedEncoding implements Encoding<Object> {

    @Override
    public List<Class<?>> types() {
      throw new IllegalStateException();
    }

    @Override
    public boolean canEncode(Object value) {
      throw new IllegalStateException();
    }

    @Override
    public void encode(RecordOutput data, Object value, FieldScratchData scratchData, Object privateData) throws IOException {
      throw new IllegalStateException();
    }

    @Override
    public Object generatePrivateData() {
      throw new IllegalStateException();
    }

    @Override
    public String asString(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public int asInt(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public long asLong(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public float asFloat(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public double asDouble(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public Number asNumber(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public boolean asBoolean(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public Boolean asBooleanOrNull(Object parsedObject, long parsedLong) {
      throw new IllegalStateException();
    }

    @Override
    public <T> T asObject(Object parsedObject, long parsedLong, Class<T> type) {
      throw new IllegalStateException();
    }

    @Override
    public int hashCode() {
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object obj) {
      throw new IllegalStateException();
    }

    @Override
    public String toString() {
      return super.toString();
    }
  }

}