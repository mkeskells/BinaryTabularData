package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.Header;
import org.github.mkeskells.btd.Records;
import org.github.mkeskells.btd.coder.Coder;
import org.github.mkeskells.btd.coder.CoderFactories;
import org.github.mkeskells.btd.coder.DefaultCoderFactory;
import org.github.mkeskells.btd.coder.FieldScratchData;
import org.github.mkeskells.btd.impl.RecordInput;

import java.io.IOException;
import java.util.List;

public class RecordsReaderImplV1 implements Records {

  private final RecordInput in;
  private final Coder coder;
  private final List<ColumnInfo> columnInfo;
  private final int colCount;

  private final FieldScratchData scratch = new FieldScratchData();
  private final Header header;

  public RecordsReaderImplV1(RecordInput in) throws IOException, ReflectiveOperationException {
    this.in = in;
    require(Rows.HEADER_START, this.in.readByte());
    String coderFactoryClassName = readFieldString();
    String coderFactoryParam = readFieldString();
    this.colCount = readFieldInteger();
    if (colCount < 1)
      throw new IllegalArgumentException("invalid column count: " + colCount);
    String[] columns = new String[colCount];
    for (int i = 0; i < colCount; i++)
      columns[i] = readFieldString();
    this.coder = (
        coderFactoryClassName == null ?
            DefaultCoderFactory.instance() :
            CoderFactories.getCoderFactory(coderFactoryClassName)
    ).forConfig(coderFactoryParam, columns);
    this.columnInfo = ColumnInfo.createColumnInfo(coder);
    this.header = new HeaderImpl(coder);
    current = new ReadDataRow(colCount);
  }

  private int readFieldInteger() throws IOException {
    return Value.INTEGER.readInt(in);
  }

  private String readFieldString() throws IOException {
    return Value.STRING.readStringOrNull(in, scratch);
  }

  private byte require(byte required, byte actual) {
    if (required != actual)
      throw new IllegalStateException("expectation violation");
    return actual;
  }

  private ReadDataRow current;
  private ReadDataRow[] prev = new ReadDataRow[Value.PREVIOUS.size];

  @Override
  public boolean nextRow() throws IOException {
    int rowStart = in.readUnsignedByte();
    switch (rowStart) {
      case Rows.ROWS_END:
        current = null;
        return false;
      case Rows.ROW_START:
        readRow();
        return true;
      default:
        throw new IllegalStateException();
    }

  }

  private void readRow() throws IOException {
    ReadDataRow last = prev[prev.length - 1];
    System.arraycopy(prev, 0, prev, 1, prev.length - 1);
    prev[0] = current;
    if (last == null) last = new ReadDataRow(colCount);
    else last.clear();
    current = last;
    Value.decodeRow(in, current, prev, scratch, columnInfo);
  }


  @Override
  public Header getHeader() {
    return header;
  }

  @Override
  public int getInt(int columnId) {
    return current.transcoder[columnId].asInt(current.objects[columnId], current.primitive[columnId]);
  }

  @Override
  public long getLong(int columnId) {
    return current.transcoder[columnId].asLong(current.objects[columnId], current.primitive[columnId]);
  }

  @Override
  public double getDouble(int columnId) {
    return current.transcoder[columnId].asDouble(current.objects[columnId], current.primitive[columnId]);
  }

  @Override
  public boolean getBoolean(int columnId) {
    return current.transcoder[columnId].asBoolean(current.objects[columnId], current.primitive[columnId]);
  }

  //object forms
  //We record previous generated values. This helps with repeated reads, and also when using prev values
  private <T> T recordingGenerated(int columnId, Class<T> clazz, T value) {
    current.generatedType[columnId] = clazz;
    current.generated[columnId] = value;
    return value;
  }

  @Override
  public Number getNumber(int columnId) {
    if (current.generatedType[columnId] == Number.class)
      return (Number) current.generated[columnId];

    return recordingGenerated(columnId, Number.class, current.transcoder[columnId].asNumber(current.objects[columnId], current.primitive[columnId]));
  }


  @Override
  public Boolean getBooleanOrNull(int columnId) {
    if (current.generatedType[columnId] == Boolean.class)
      return (Boolean) current.generated[columnId];

    return recordingGenerated(columnId, Boolean.class, current.transcoder[columnId].asBooleanOrNull(current.objects[columnId], current.primitive[columnId]));
  }

  @Override
  public String getString(int columnId) {
    if (current.generatedType[columnId] == String.class)
      return (String) current.generated[columnId];

    return recordingGenerated(columnId, String.class, current.transcoder[columnId].asString(current.objects[columnId], current.primitive[columnId]));
  }

  @Override
  public <T> T getObject(int columnId, Class<T> type) {
    if (current.generatedType[columnId] == type)
      return type.cast(current.generated[columnId]);

    return recordingGenerated(columnId, type, current.transcoder[columnId].asObject(current.objects[columnId], current.primitive[columnId], type));
  }
}
