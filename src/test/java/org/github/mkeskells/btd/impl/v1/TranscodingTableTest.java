package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.RecordReaders;
import org.github.mkeskells.btd.RecordWriter;
import org.github.mkeskells.btd.RecordWriters;
import org.github.mkeskells.btd.Records;
import org.github.mkeskells.btd.coder.DefaultCoderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TranscodingTableTest {

  @Test
  void code_null() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 0;
    String msg = "value is null";

    assertTrue(reader.nextRow());
    assertNull(reader.getObject(col, Object.class));
    assertNull(reader.getObject(col, String.class));
    assertNull(reader.getObject(col, Number.class));
    assertNull(reader.getObject(col, Boolean.class));
    assertNull(reader.getObject(col, Test.class));

    assertNull(reader.getString(col));
    assertNull(reader.getBooleanOrNull(col));
    assertNull(reader.getNumber(col));
    assertNull(reader.getObject(col, Boolean.class));

    assertEquals(msg, assertThrows(NullPointerException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(msg, assertThrows(NullPointerException.class, () -> reader.getInt(col)).getMessage());
    assertEquals(msg, assertThrows(NullPointerException.class, () -> reader.getLong(col)).getMessage());
    assertEquals(msg, assertThrows(NullPointerException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_boolean_true() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 1;
    String value = "true";

    assertTrue(reader.nextRow());
    assertSame(Boolean.TRUE, reader.getObject(col, Object.class));
    assertSame("true", reader.getObject(col, String.class));
    assertEquals("For input string: \"true\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertSame(Boolean.TRUE, reader.getObject(col, Boolean.class));
    assertEquals(cantConvert(Test.class, Boolean.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertSame("true", reader.getString(col));
    assertSame(Boolean.TRUE, reader.getBooleanOrNull(col));
    assertEquals("For input string: \"true\"", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertSame(Boolean.TRUE, reader.getObject(col, Boolean.class));

    assertEquals(true, reader.getBoolean(col));
    assertEquals("For input string: \"true\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"true\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("For input string: \"true\"", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  String cantConvert(Class<?> to, Class<?> from, Object value) {
    return "cant convert to a " + to + " from a " + from + " <" + value + ">";
  }

  @Test
  void code_boolean_false() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 2;
    String value = "false";

    assertTrue(reader.nextRow());
    assertSame(Boolean.FALSE, reader.getObject(col, Object.class));
    assertSame("false", reader.getObject(col, String.class));
    assertEquals("For input string: \"false\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertSame(Boolean.FALSE, reader.getObject(col, Boolean.class));
    assertEquals(cantConvert(Test.class, Boolean.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertSame("false", reader.getString(col));
    assertSame(Boolean.FALSE, reader.getBooleanOrNull(col));
    assertEquals("For input string: \"false\"", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertSame(Boolean.FALSE, reader.getObject(col, Boolean.class));

    assertEquals(false, reader.getBoolean(col));
    assertEquals("For input string: \"false\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"false\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("For input string: \"false\"", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_int_0() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 3;
    int value = 0;

    assertTrue(reader.nextRow());
    assertSame(0, reader.getObject(col, Object.class));
    assertEquals("0", reader.getObject(col, String.class));
    assertEquals(0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("0", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(0, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(0, reader.getInt(col));
    assertEquals(0L, reader.getLong(col));
    assertEquals(0.0, reader.getDouble(col));
  }

  @Test
  void code_int_1() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 4;
    int value = 1;

    assertTrue(reader.nextRow());
    assertSame(1, reader.getObject(col, Object.class));
    assertEquals("1", reader.getObject(col, String.class));
    assertEquals(1, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("1", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(1, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(1, reader.getInt(col));
    assertEquals(1L, reader.getLong(col));
    assertEquals(1.0, reader.getDouble(col));
  }

  @Test
  void code_int_2() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 5;
    int value = 2;

    assertTrue(reader.nextRow());
    assertSame(2, reader.getObject(col, Object.class));
    assertEquals("2", reader.getObject(col, String.class));
    assertEquals(2, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(2, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Integer.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(2, reader.getInt(col));
    assertEquals(2L, reader.getLong(col));
    assertEquals(2.0, reader.getDouble(col));
  }

  @Test
  void code_long_20000000000L() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 6;
    long value = 20000000000L;

    assertTrue(reader.nextRow());
    assertEquals(20000000000L, reader.getObject(col, Object.class));
    assertEquals("20000000000", reader.getObject(col, String.class));
    assertEquals(20000000000L, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Long.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Long.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("20000000000", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Long.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(20000000000L, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Long.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("cant convert 20000000000 to a int without loss of precision", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals(20000000000L, reader.getLong(col));
    assertEquals(20000000000.0, reader.getDouble(col));
  }

  @Test
  void code_double_0() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 7;
    double value = 0;

    assertTrue(reader.nextRow());
    assertEquals(0.0, reader.getObject(col, Object.class));
    assertEquals("0.0", reader.getObject(col, String.class));
    assertEquals(0.0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("0.0", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(0.0, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(0, reader.getInt(col));
    assertEquals(0L, reader.getLong(col));
    assertEquals(0.0, reader.getDouble(col));
  }

  @Test
  void code_double_1() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 8;
    double value = 1;

    assertTrue(reader.nextRow());
    assertEquals(1.0, reader.getObject(col, Object.class));
    assertEquals("1.0", reader.getObject(col, String.class));
    assertEquals(1.0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("1.0", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(1.0, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(1, reader.getInt(col));
    assertEquals(1L, reader.getLong(col));
    assertEquals(1.0, reader.getDouble(col));
  }

  @Test
  void code_double_2() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 9;
    double value = 2;

    assertTrue(reader.nextRow());
    assertEquals(2.0, reader.getObject(col, Object.class));
    assertEquals("2.0", reader.getObject(col, String.class));
    assertEquals(2.0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2.0", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(2.0, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(2, reader.getInt(col));
    assertEquals(2L, reader.getLong(col));
    assertEquals(2.0, reader.getDouble(col));
  }

  @Test
  void code_double_20000000000() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 10;
    double value = 20000000000.0;

    assertTrue(reader.nextRow());
    assertEquals(20000000000.0, reader.getObject(col, Object.class));
    assertEquals("2.0E10", reader.getObject(col, String.class));
    assertEquals(20000000000.0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2.0E10", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(20000000000.0, reader.getNumber(col));

    assertEquals(cantConvert(Boolean.class, Double.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("cannot be narrowed to an int : 2.0E10", assertThrows(UnsupportedOperationException.class, () -> reader.getInt(col)).getMessage());
    assertEquals(20000000000L, reader.getLong(col));
    assertEquals(20000000000.0, reader.getDouble(col));
  }

  @Test
  void code_string_empty() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 11;
    String value = "";

    assertTrue(reader.nextRow());
    assertEquals("", reader.getObject(col, Object.class));
    assertEquals("", reader.getObject(col, String.class));
    assertEquals("empty String", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals("empty String", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("For input string: \"\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("empty String", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_string_true() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 12;
    String value = "trUe";

    assertTrue(reader.nextRow());
    assertEquals("trUe", reader.getObject(col, Object.class));
    assertEquals("trUe", reader.getObject(col, String.class));
    assertEquals("For input string: \"trUe\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertSame(Boolean.TRUE, reader.getObject(col, Boolean.class));
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("trUe", reader.getString(col));
    assertSame(Boolean.TRUE, reader.getBooleanOrNull(col));
    assertEquals("For input string: \"trUe\"", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertSame(Boolean.TRUE, reader.getObject(col, Boolean.class));

    assertEquals(true, reader.getBoolean(col));
    assertEquals("For input string: \"trUe\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"trUe\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("For input string: \"trUe\"", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_string_false() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 13;
    String value = "faLse";

    assertTrue(reader.nextRow());
    assertEquals("faLse", reader.getObject(col, Object.class));
    assertEquals("faLse", reader.getObject(col, String.class));
    assertEquals("For input string: \"faLse\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertSame(Boolean.FALSE, reader.getObject(col, Boolean.class));
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("faLse", reader.getString(col));
    assertSame(Boolean.FALSE, reader.getBooleanOrNull(col));
    assertEquals("For input string: \"faLse\"", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertSame(Boolean.FALSE, reader.getObject(col, Boolean.class));

    assertEquals(false, reader.getBoolean(col));
    assertEquals("For input string: \"faLse\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"faLse\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("For input string: \"faLse\"", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_string_0() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 14;
    String value = "0";

    assertTrue(reader.nextRow());
    assertEquals("0", reader.getObject(col, Object.class));
    assertEquals("0", reader.getObject(col, String.class));
    assertEquals(0, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("0", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(0, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(0, reader.getInt(col));
    assertEquals(0L, reader.getLong(col));
    assertEquals(0.0, reader.getDouble(col));
  }

  @Test
  void code_string_1() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 15;
    String value = "1";

    assertTrue(reader.nextRow());
    assertEquals("1", reader.getObject(col, Object.class));
    assertEquals("1", reader.getObject(col, String.class));
    assertEquals(1, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("1", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(1, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(1, reader.getInt(col));
    assertEquals(1L, reader.getLong(col));
    assertEquals(1.0, reader.getDouble(col));
  }

  @Test
  void code_string_2() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 16;
    String value = "2";

    assertTrue(reader.nextRow());
    assertEquals("2", reader.getObject(col, Object.class));
    assertEquals("2", reader.getObject(col, String.class));
    assertEquals(2, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(2, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals(2, reader.getInt(col));
    assertEquals(2L, reader.getLong(col));
    assertEquals(2.0, reader.getDouble(col));
  }

  @Test
  void code_string_20000000000L() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 17;
    String value = "20000000000";

    assertTrue(reader.nextRow());
    assertEquals("20000000000", reader.getObject(col, Object.class));
    assertEquals("20000000000", reader.getObject(col, String.class));
    assertEquals(20000000000L, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("20000000000", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(20000000000L, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("cant convert 20000000000 to a class java.lang.Integer without loss of precision", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals(20000000000L, reader.getLong(col));
    assertEquals(2E10, reader.getDouble(col));
  }

  @Test
  void code_string_2E10() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 18;
    String value = "2.0E10";

    assertTrue(reader.nextRow());
    assertEquals("2.0E10", reader.getObject(col, Object.class));
    assertEquals("2.0E10", reader.getObject(col, String.class));
    assertEquals(2.0E10, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2.0E10", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(2.0E10, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("For input string: \"2.0E10\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"2.0E10\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals(2E10, reader.getDouble(col));
  }

  @Test
  void code_string_2_5() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 19;
    String value = "2.5";

    assertTrue(reader.nextRow());
    assertEquals("2.5", reader.getObject(col, Object.class));
    assertEquals("2.5", reader.getObject(col, String.class));
    assertEquals(2.5, reader.getObject(col, Number.class));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("2.5", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals(2.5, reader.getNumber(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("For input string: \"2.5\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"2.5\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals(2.5, reader.getDouble(col));
  }

  @Test
  void code_string_foo() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    int col = 20;
    String value = "foo";

    assertTrue(reader.nextRow());
    assertEquals("foo", reader.getObject(col, Object.class));
    assertEquals("foo", reader.getObject(col, String.class));
    assertEquals("For input string: \"foo\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Number.class)).getMessage());
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());
    assertEquals(cantConvert(Test.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Test.class)).getMessage());

    assertEquals("foo", reader.getString(col));
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBooleanOrNull(col)).getMessage());
    assertEquals("For input string: \"foo\"", assertThrows(NumberFormatException.class, () -> reader.getNumber(col)).getMessage());
    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getObject(col, Boolean.class)).getMessage());

    assertEquals(cantConvert(Boolean.class, String.class, value), assertThrows(UnsupportedOperationException.class, () -> reader.getBoolean(col)).getMessage());
    assertEquals("For input string: \"foo\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"foo\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals("For input string: \"foo\"", assertThrows(NumberFormatException.class, () -> reader.getDouble(col)).getMessage());
  }

  @Test
  void code_byte() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());

    Assertions.assertAll("Byte boundary",
        () -> checkByte(reader, 21, (int) Byte.MIN_VALUE, (int) Byte.MIN_VALUE),
        () -> checkByte(reader, 22, (int) Byte.MIN_VALUE, String.valueOf(Byte.MIN_VALUE)),

        () -> checkByte(reader, 25, (int) Byte.MAX_VALUE, (int) Byte.MAX_VALUE),
        () -> checkByte(reader, 26, (int) Byte.MAX_VALUE, String.valueOf(Byte.MAX_VALUE))
    );
  }

  private void checkByte(Records reader, int col, Integer expected, Object raw) {
    assertEquals(raw, reader.getObject(col, Object.class));
    assertEquals(expected.toString(), reader.getObject(col, String.class));
    assertEquals(expected.intValue(), reader.getObject(col, Number.class));
    assertEquals(expected.intValue(), reader.getObject(col, Integer.class));
    assertEquals(expected.longValue(), reader.getObject(col, Long.class));
    assertEquals(expected.floatValue(), reader.getObject(col, Float.class));
    assertEquals(expected.doubleValue(), reader.getObject(col, Double.class));

    assertEquals(expected.toString(), reader.getString(col));
    assertEquals(expected.intValue(), reader.getNumber(col));
    assertEquals(expected.intValue(), reader.getInt(col));
    assertEquals(expected.longValue(), reader.getLong(col));
    assertEquals(expected.doubleValue(), reader.getDouble(col));
  }

  @Test
  void code_short() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());

    Assertions.assertAll("Byte overflow",
        () -> checkShort(reader, 23, Byte.MIN_VALUE - 1, (int) Byte.MIN_VALUE - 1),
        () -> checkShort(reader, 24, Byte.MIN_VALUE - 1, String.valueOf(Byte.MIN_VALUE - 1)),

        () -> checkShort(reader, 27, Byte.MAX_VALUE + 1, (int) Byte.MAX_VALUE + 1),
        () -> checkShort(reader, 28, Byte.MAX_VALUE + 1, String.valueOf(Byte.MAX_VALUE + 1))
    );
    Assertions.assertAll("Short boundary",
        () -> checkShort(reader, 31, (int) Short.MIN_VALUE, (int) Short.MIN_VALUE),
        () -> checkShort(reader, 32, (int) Short.MIN_VALUE, String.valueOf(Short.MIN_VALUE)),

        () -> checkShort(reader, 35, (int) Short.MAX_VALUE, (int) Short.MAX_VALUE),
        () -> checkShort(reader, 36, (int) Short.MAX_VALUE, String.valueOf(Short.MAX_VALUE))
    );
  }

  private void checkShort(Records reader, int col, Integer expected, Object raw) {
    assertEquals(raw, reader.getObject(col, Object.class));
    assertEquals(expected.toString(), reader.getObject(col, String.class));
    assertEquals(expected.intValue(), reader.getObject(col, Number.class));
    assertEquals(expected.intValue(), reader.getObject(col, Integer.class));
    assertEquals(expected.longValue(), reader.getObject(col, Long.class));
    assertEquals(expected.floatValue(), reader.getObject(col, Float.class));
    assertEquals(expected.doubleValue(), reader.getObject(col, Double.class));

    assertEquals(expected.toString(), reader.getString(col));
    assertEquals(expected.intValue(), reader.getNumber(col));
    assertEquals(expected.intValue(), reader.getInt(col));
    assertEquals(expected.longValue(), reader.getLong(col));
    assertEquals(expected.doubleValue(), reader.getDouble(col));
  }

  @Test
  void code_int() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());

    Assertions.assertAll("short overflow",
        () -> checkInt(reader, 33, Short.MIN_VALUE - 1, (int) Short.MIN_VALUE - 1),
        () -> checkInt(reader, 34, Short.MIN_VALUE - 1, String.valueOf(Short.MIN_VALUE - 1)),

        () -> checkInt(reader, 37, Short.MAX_VALUE + 1, (int) Short.MAX_VALUE + 1),
        () -> checkInt(reader, 38, Short.MAX_VALUE + 1, String.valueOf(Short.MAX_VALUE + 1))
    );
    Assertions.assertAll("int boundary",
        () -> checkInt(reader, 41, Integer.MIN_VALUE, Integer.MIN_VALUE),
        () -> checkInt(reader, 42, Integer.MIN_VALUE, String.valueOf(Integer.MIN_VALUE)),

        () -> checkInt(reader, 45, Integer.MAX_VALUE, Integer.MAX_VALUE),
        () -> checkInt(reader, 46, Integer.MAX_VALUE, String.valueOf(Integer.MAX_VALUE))
    );
  }

  private void checkInt(Records reader, int col, Integer expected, Object raw) {
    assertEquals(raw, reader.getObject(col, Object.class));
    assertEquals(expected.toString(), reader.getObject(col, String.class));
    assertEquals(expected.intValue(), reader.getObject(col, Number.class));
    assertEquals(expected.intValue(), reader.getObject(col, Integer.class));
    assertEquals(expected.longValue(), reader.getObject(col, Long.class));
    assertEquals(expected.floatValue(), reader.getObject(col, Float.class));
    assertEquals(expected.doubleValue(), reader.getObject(col, Double.class));

    assertEquals(expected.toString(), reader.getString(col));
    assertEquals(expected.intValue(), reader.getNumber(col));
    assertEquals(expected.intValue(), reader.getInt(col));
    assertEquals(expected.longValue(), reader.getLong(col));
    assertEquals(expected.doubleValue(), reader.getDouble(col));
  }

  @Test
  void code_long() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());

    Assertions.assertAll("int overflow",
        () -> checkLong(reader, 43, (long) Integer.MIN_VALUE - 1, (long) Integer.MIN_VALUE - 1),
        () -> checkLong(reader, 44, (long) Integer.MIN_VALUE - 1, String.valueOf((long) Integer.MIN_VALUE - 1)),

        () -> checkLong(reader, 47, (long) Integer.MAX_VALUE + 1, (long) Integer.MAX_VALUE + 1),
        () -> checkLong(reader, 48, (long) Integer.MAX_VALUE + 1, String.valueOf((long) Integer.MAX_VALUE + 1))
    );
    Assertions.assertAll("long boundary",
        () -> checkLong(reader, 51, Long.MIN_VALUE, Long.MIN_VALUE),
        () -> checkLong(reader, 52, Long.MIN_VALUE, String.valueOf(Long.MIN_VALUE)),

        () -> checkLong(reader, 55, Long.MAX_VALUE, Long.MAX_VALUE),
        () -> checkLong(reader, 56, Long.MAX_VALUE, String.valueOf(Long.MAX_VALUE))
    );
  }

  private void checkLong(Records reader, int col, Long expected, Object raw) {
    assertEquals(raw, reader.getObject(col, Object.class));
    assertEquals(expected.toString(), reader.getObject(col, String.class));
    assertEquals(expected, reader.getObject(col, Number.class));
    assertEquals("cant convert " + expected + " to a class java.lang.Integer without loss of precision", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Integer.class)).getMessage());
    assertEquals(expected.longValue(), reader.getObject(col, Long.class));
    assertEquals(expected.floatValue(), reader.getObject(col, Float.class));
    assertEquals(expected.doubleValue(), reader.getObject(col, Double.class));

    assertEquals(expected.toString(), reader.getString(col));
    assertEquals(expected, reader.getNumber(col));
    if (raw instanceof String)
      assertEquals("cant convert " + expected + " to a class java.lang.Integer without loss of precision", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    else
      assertEquals("cant convert " + expected + " to a int without loss of precision", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals(expected.longValue(), reader.getLong(col));
    assertEquals(expected.doubleValue(), reader.getDouble(col));
  }

  @Test
  void code_big() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());

    Assertions.assertAll("long overflow",
        () -> checkBig(reader, 54, BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.valueOf(1))),

        () -> checkBig(reader, 58, BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(1)))
    );
  }

  private void checkBig(Records reader, int col, BigInteger raw) {
    assertEquals(raw.toString(), reader.getObject(col, Object.class));
    assertEquals(raw.toString(), reader.getObject(col, String.class));
    assertEquals(raw.doubleValue(), reader.getObject(col, Number.class));
    assertEquals("For input string: \"" + raw + "\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Integer.class)).getMessage());
    assertEquals("For input string: \"" + raw + "\"", assertThrows(NumberFormatException.class, () -> reader.getObject(col, Long.class)).getMessage());
    assertEquals(raw.floatValue(), reader.getObject(col, Float.class));
    assertEquals(raw.doubleValue(), reader.getObject(col, Double.class));

    assertEquals(raw.toString(), reader.getString(col));
    assertEquals(raw.doubleValue(), reader.getNumber(col));
    assertEquals("For input string: \"" + raw + "\"", assertThrows(NumberFormatException.class, () -> reader.getInt(col)).getMessage());
    assertEquals("For input string: \"" + raw + "\"", assertThrows(NumberFormatException.class, () -> reader.getLong(col)).getMessage());
    assertEquals(raw.doubleValue(), reader.getDouble(col));
  }

  private Records records(byte[] bytes) throws IOException, ReflectiveOperationException {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    Records reader = RecordReaders.createReader(bais);
    return reader;
  }

  private byte[] simpleToBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    RecordWriter writer = RecordWriters.createWriter(baos, 1, DefaultCoderFactory.instance().forConfig("",
        "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9",
        "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19",
        "c20", "c21", "c22", "c23", "c24", "c25", "c26", "c27", "c28", "c29",
        "c30", "c31", "c32", "c33", "c34", "c35", "c36", "c37", "c38", "c39",
        "c40", "c41", "c42", "c43", "c44", "c45", "c46", "c47", "c48", "c49",
        "c50", "c51", "c52", "c53", "c54", "c55", "c56", "c57", "c58", "c59"));

    //skip 0 - nulls
    writer.write(1, true);
    writer.write(2, false);
    writer.write(3, 0);
    writer.write(4, 1);
    writer.write(5, 2);
    writer.write(6, 20000000000L);
    writer.write(7, 0.0);
    writer.write(8, 1.0);
    writer.write(9, 2.0);
    writer.write(10, 20000000000.0);
    writer.write(11, "");
    writer.write(12, "trUe");
    writer.write(13, "faLse");
    writer.write(14, "0");
    writer.write(15, "1");
    writer.write(16, "2");
    writer.write(17, "20000000000");
    writer.write(18, "2.0E10");
    writer.write(19, "2.5");
    writer.write(20, "foo");
    // byte
    writer.write(21, Byte.MIN_VALUE);
    writer.write(22, String.valueOf(Byte.MIN_VALUE));
    writer.write(23, Byte.MIN_VALUE - 1);
    writer.write(24, String.valueOf(Byte.MIN_VALUE - 1));
    writer.write(25, Byte.MAX_VALUE);
    writer.write(26, String.valueOf(Byte.MAX_VALUE));
    writer.write(27, Byte.MAX_VALUE + 1);
    writer.write(28, String.valueOf(Byte.MAX_VALUE + 1));
    // short
    writer.write(31, Short.MIN_VALUE);
    writer.write(32, String.valueOf(Short.MIN_VALUE));
    writer.write(33, Short.MIN_VALUE - 1);
    writer.write(34, String.valueOf(Short.MIN_VALUE - 1));
    writer.write(35, Short.MAX_VALUE);
    writer.write(36, String.valueOf(Short.MAX_VALUE));
    writer.write(37, Short.MAX_VALUE + 1);
    writer.write(38, String.valueOf(Short.MAX_VALUE + 1));
    // int
    writer.write(41, Integer.MIN_VALUE);
    writer.write(42, String.valueOf(Integer.MIN_VALUE));
    writer.write(43, (long) Integer.MIN_VALUE - 1);
    writer.write(44, String.valueOf((long) Integer.MIN_VALUE - 1));
    writer.write(45, Integer.MAX_VALUE);
    writer.write(46, String.valueOf(Integer.MAX_VALUE));
    writer.write(47, (long) Integer.MAX_VALUE + 1);
    writer.write(48, String.valueOf((long) Integer.MAX_VALUE + 1));
    // long
    writer.write(51, Long.MIN_VALUE);
    writer.write(52, String.valueOf(Long.MIN_VALUE));
//    writer.write(53, Long.MIN_VALUE - 1);
    writer.write(54, String.valueOf(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.valueOf(1))));
    writer.write(55, Long.MAX_VALUE);
    writer.write(56, String.valueOf(Long.MAX_VALUE));
//    writer.write(57, Long.MAX_VALUE + 1);
    writer.write(58, String.valueOf(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(1))));

    writer.endRow();
    writer.close();
    return baos.toByteArray();
  }
}
