package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Transcoder;

import java.util.Arrays;

abstract class DataRow {
  final long[] primitive;
  final Object[] objects;
  final int colCount;

  public DataRow(int colCount) {
    primitive = new long[colCount];
    objects = new Object[colCount];
    this.colCount = colCount;
  }

  void clear() {
    Arrays.fill(primitive, 0L);
    Arrays.fill(objects, null);
  }
}

class ReadDataRow extends DataRow {

  final Transcoder[] transcoder;
  final Object[] generated;
  final Class[] generatedType;

  public ReadDataRow(int colCount) {
    super(colCount);
    transcoder = new Transcoder[colCount];
    generated = new Object[colCount];
    generatedType = new Class[colCount];
  }

  @Override
  void clear() {
    super.clear();
    Arrays.fill(transcoder, null);
    Arrays.fill(generated, null);
    Arrays.fill(generatedType, null);
  }
}

class WriteDataRow extends DataRow {
  //default value is TYPE_UNSET
  final static byte TYPE_UNSET = 0;
  final static byte TYPE_OBJECT = 10;
  final static byte TYPE_LONG = 11;
  final static byte TYPE_BOOLEAN = 12;
  final static byte TYPE_DOUBLE = 13;
  final static byte TYPE_FLOAT = 14;


  final byte[] types;

  public WriteDataRow(int colCount) {
    super(colCount);
    types = new byte[colCount];
  }

  @Override
  void clear() {
    super.clear();
    Arrays.fill(types, (byte) 0);
  }
}
