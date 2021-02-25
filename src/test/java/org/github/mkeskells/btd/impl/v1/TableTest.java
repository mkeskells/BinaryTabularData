package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.*;
import org.github.mkeskells.btd.coder.DefaultCoderFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {
  @Test
  void checkHeader() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    Header header = reader.getHeader();
    assertEquals(3, header.getHeaderCount());
    assertEquals("a", header.getHeaderValue(0));
    assertEquals("b", header.getHeaderValue(1));
    assertEquals("c", header.getHeaderValue(2));

    assertThrows(IndexOutOfBoundsException.class, () -> header.getHeaderValue(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> header.getHeaderValue(-10));
    assertThrows(IndexOutOfBoundsException.class, () -> header.getHeaderValue(3));
    assertThrows(IndexOutOfBoundsException.class, () -> header.getHeaderValue(10));

    assertEquals(List.of("a", "b", "c"), header.getHeaders());
  }

  @Test
  void defaultToNull() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleToBytes();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());
    assertNull(reader.getString(0));
    assertNull(reader.getString(1));
    assertNull(reader.getString(2));

    assertTrue(reader.nextRow());
    assertEquals("", reader.getString(0));
    assertEquals("foo", reader.getString(1));
    assertNull(reader.getString(2));

    assertTrue(reader.nextRow());
    assertNull(reader.getString(0));
    assertNull(reader.getString(1));
    assertNull(reader.getString(2));

    assertFalse(reader.nextRow());
  }

  @Test
  void internedDuplicates() throws IOException, ReflectiveOperationException {
    byte[] bytes = simpleWithDuplicates();
    Records reader = records(bytes);

    assertTrue(reader.nextRow());
    Object a0 = reader.getObject(0, Object.class);
    Object a1 = reader.getObject(1, Object.class);
    Object a2 = reader.getObject(2, Object.class);
    Object a3 = reader.getObject(3, Object.class);
    Object a4 = reader.getObject(4, Object.class);
    Object a5 = reader.getObject(5, Object.class);

    assertEquals("ABC", a0);
    assertEquals(1, a1);
    assertEquals(1.0, a2);
    assertEquals("duplicate", a3);
    assertEquals(1, a4);
    assertEquals(1.0, a5);

    assertTrue(reader.nextRow());
    Object b0 = reader.getObject(0, Object.class);
    Object b1 = reader.getObject(1, Object.class);
    Object b2 = reader.getObject(2, Object.class);
    Object b3 = reader.getObject(3, Object.class);
    Object b4 = reader.getObject(4, Object.class);
    Object b5 = reader.getObject(5, Object.class);

    assertEquals("ABC", b0);
    assertEquals(1, b1);
    assertEquals(1.0, b2);
    assertEquals("duplicate", b3);
    assertEquals(1, b4);
    assertEquals(1.0, b5);

    assertSame(a0, b0);
    assertSame(a1, b1);
    assertSame(a2, b2);
    assertSame(a3, b3);
    assertSame(a4, b4);
    assertSame(a5, b5);
  }


  private Records records(byte[] bytes) throws IOException, ReflectiveOperationException {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    Records reader = RecordReaders.createReader(bais);
    return reader;
  }

  private byte[] simpleToBytes() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    RecordWriter writer = RecordWriters.createWriter(baos, 1, DefaultCoderFactory.instance().forConfig("", "a", "b", "c"));

    writer.endRow();
    writer.write(0, "");
    writer.write(1, "foo");
    writer.endRow();
    writer.endRow();
    writer.close();
    return baos.toByteArray();
  }

  private byte[] simpleWithDuplicates() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    RecordWriter writer = RecordWriters.createWriter(baos, 1, DefaultCoderFactory.instance().forConfig("", "string1", "int1", "float1", "string2", "int2", "float2"));

    writer.write(0, "ABC");
    writer.write(1, 1);
    writer.write(2, 1.0);
    writer.write(3, "ignored");
    writer.write(3, new String("duplicate"));
    writer.write(4, 1);
    writer.write(5, 1.0);
    writer.endRow();
    writer.write(0, "ABC");
    writer.write(1, 1);
    writer.write(2, 1.0);
    writer.write(3, new String("duplicate"));
    writer.write(4, 1);
    writer.write(5, 1.0);
    writer.endRow();
    writer.close();
    return baos.toByteArray();
  }
}
