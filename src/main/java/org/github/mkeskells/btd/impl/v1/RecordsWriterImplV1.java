package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Coder;
import org.github.mkeskells.btd.coder.DefaultCoderFactory;
import org.github.mkeskells.btd.coder.Encoding;
import org.github.mkeskells.btd.coder.FieldScratchData;
import org.github.mkeskells.btd.impl.AbstractRecordWriter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RecordsWriterImplV1 extends AbstractRecordWriter {
  private final RecordOutputV1 data;
  private final int colCount;
  private final Coder coder;

  private final List<ColumnInfo> columnsInfo;

  private WriteDataRow current;
  private WriteDataRow[] prev;

  private final FieldScratchData scratch = new FieldScratchData();

  public RecordsWriterImplV1(RecordOutputV1 data, Coder coder) throws IOException {
    this.data = data;
    this.coder = coder;
    this.colCount = coder.getColumns().size();
    this.columnsInfo = ColumnInfo.createColumnInfo(coder);

    current = new WriteDataRow(colCount);
    prev = new WriteDataRow[Value.PREVIOUS.size];

    data.writeByte(Rows.HEADER_START);
    if (coder.getFactoryClass() == DefaultCoderFactory.class)
      writeFieldNull();
    else
      writeFieldString(coder.getFactoryClass().getName());
    writeFieldString(coder.getConfig().config);

    writeFieldInteger(colCount);
    for (String col : coder.getConfig().columns) {
      writeFieldString(col);
    }
  }

  //data handlers
  @Override
  protected void writeObject(int columnId, Object value) {
    ColumnInfo columnInfo = checkColumn(columnId);

    if (value == null) {
      if (!columnInfo.nullable)
        throw new IllegalArgumentException("column is not nullable");
    } else {
      columnInfo.validate(value);
      current.primitive[columnId] = columnInfo.getEncoderIndex(value);
    }
    current.objects[columnId] = value;
    current.types[columnId] = WriteDataRow.TYPE_OBJECT;
  }

  @Override
  public void write(int columnId, boolean value) {
    ColumnInfo columnInfo = checkColumn(columnId);
    columnInfo.validate(value);
    current.primitive[columnId] = value ? 1 : 0;
    current.types[columnId] = WriteDataRow.TYPE_BOOLEAN;
    current.objects[columnId] = null;
  }


  @Override
  public void write(int columnId, double value) {
    ColumnInfo columnInfo = checkColumn(columnId);
    columnInfo.validate(value);
    current.primitive[columnId] = Double.doubleToRawLongBits(value);
    current.types[columnId] = WriteDataRow.TYPE_DOUBLE;
    current.objects[columnId] = null;
  }

  @Override
  public void write(int columnId, float value) {
    ColumnInfo columnInfo = checkColumn(columnId);
    columnInfo.validate(value);
    current.primitive[columnId] = Float.floatToRawIntBits(value);
    current.types[columnId] = WriteDataRow.TYPE_FLOAT;
    current.objects[columnId] = null;
  }

  @Override
  public void write(int columnId, long value) {
    ColumnInfo columnInfo = checkColumn(columnId);
    columnInfo.validate(value);
    current.primitive[columnId] = value;
    current.types[columnId] = WriteDataRow.TYPE_LONG;
    current.objects[columnId] = null;
  }


  private ColumnInfo checkColumn(int columnId) {
    if (columnId < 0 || columnId >= colCount) {
      throw new IllegalArgumentException("column are values between 0.." + (colCount - 1) + " inclusive. " + columnId + " is invalid");
    }
    ColumnInfo column = columnsInfo.get(columnId);
    if (current.types[columnId] != WriteDataRow.TYPE_UNSET && !column.allowOverwrite())
      throw new IllegalArgumentException("column is not nullable");
    return column;
  }

  @Override
  public void close(boolean closeUnderlyingStream) throws IOException {
    data.writeByte(Rows.ROWS_END);
    if (closeUnderlyingStream)
      data.close();
  }

  //write known fields
  private void writeFieldNull() throws IOException {
    Value.NULL.write(data);
  }

  private void writeFieldPrev(int prevIndex) throws IOException {
    Value.PREVIOUS.write(data, prevIndex);
  }

  private void writeFieldInteger(long value) throws IOException {
    Value.INTEGER.write(data, value);
  }

  private void writeFieldDouble(double value) throws IOException {
    Value.DOUBLE.write(data, value);
  }

  private void writeFieldFloat(float value) throws IOException {
    Value.FLOAT.write(data, value);
  }

  private void writeFieldBoolean(boolean value) throws IOException {
    Value.BOOLEAN.write(data, value);
  }

  private void writeFieldString(String value) throws IOException {
    Value.STRING.encode(data, value, scratch, null);
  }

  @Override
  public void endRow() throws IOException {
    data.writeByte(Rows.ROW_START);

    nextColumn:
    for (int columnId = 0; columnId < colCount; columnId++) {
      ColumnInfo columnInfo = columnsInfo.get(columnId);
      byte type = current.types[columnId];
      if (type == WriteDataRow.TYPE_OBJECT) {
        Object value = current.objects[columnId];
        if (value == null)
          writeFieldNull();
        else {
          //first check to see if we have the same object
          for (int prevIndex = 0; prevIndex < prev.length; prevIndex++) {
            DataRow prevRow = prev[prevIndex];
            if (prevRow == null)
              break;
            if (prevRow.objects[columnId] == value) {
              writeFieldPrev(prevIndex);
              continue nextColumn;
            }
          }
          int maxEqualsCheck = Math.min(columnInfo.maxEqualsCheck(), prev.length);
          for (int prevIndex = 0; prevIndex < maxEqualsCheck; prevIndex++) {
            DataRow prevRow = prev[prevIndex];
            if (prevRow == null)
              break;
            if (Objects.equals(prevRow.objects[columnId], value)) {
              writeFieldPrev(prevIndex);
              continue nextColumn;
            }
          }
          int encoderIndex = (int) current.primitive[columnId];
          Encoding encoding = columnInfo.encodings[encoderIndex];
          Object privateData = columnInfo.encodingPrivateData[encoderIndex];

          encoding.encode(data, value, scratch, privateData);
        }
      } else {
        long raw = current.primitive[columnId];
        //first check to see if we have the same value
        for (int prevIndex = 0; prevIndex < prev.length; prevIndex++) {
          WriteDataRow prevRow = prev[prevIndex];
          if (prevRow == null)
            break;
          if (prevRow.primitive[columnId] == raw && prevRow.types[columnId] == type) {
            writeFieldPrev(prevIndex);
            continue nextColumn;
          }
        }
        switch (type) {
          case WriteDataRow.TYPE_LONG: {
            writeFieldInteger(raw);
            break;
          }
          case WriteDataRow.TYPE_DOUBLE: {
            writeFieldDouble(Double.longBitsToDouble(raw));
            break;
          }
          case WriteDataRow.TYPE_FLOAT: {
            writeFieldFloat(Float.intBitsToFloat((int) raw));
            break;
          }
          case WriteDataRow.TYPE_BOOLEAN:
            writeFieldBoolean(raw == 1L);
            break;

          case WriteDataRow.TYPE_UNSET:
            if (columnInfo.mandatory)
              throw new IllegalStateException("column " + columnInfo.columnName + " is mandatory");
            writeFieldNull();
            break;

          default:
            throw new IllegalStateException("type " + type);
        }
      }
    }
    WriteDataRow last = prev[prev.length - 1];
    System.arraycopy(prev, 0, prev, 1, prev.length - 1);
    prev[0] = current;
    if (last == null) last = new WriteDataRow(colCount);
    else last.clear();
    current = last;

  }
}
