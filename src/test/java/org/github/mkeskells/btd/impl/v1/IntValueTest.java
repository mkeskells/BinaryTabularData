package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

public class IntValueTest extends ValueTest {
  Writer<Integer> writer = (data, value, scratch) -> Value.INTEGER.write(data, value);
  Writer<Long> lwriter = (data, value, scratch) -> Value.INTEGER.write(data, value);

  @Test
  void encode() throws IOException {
    encode(0, writer, Value.INTEGER.start + 4);
    encode(1, writer, Value.INTEGER.start + 5);
    encode(-1, writer, Value.INTEGER.start + 6);

    encode(10, writer, Value.INTEGER.start + 0, 10);
    encode(-10, writer, Value.INTEGER.start + 0, -10);

    encode(127, writer, Value.INTEGER.start + 0, 127);
    encode(128, writer, Value.INTEGER.start + 1, 0, 128);
    encode(256, writer, Value.INTEGER.start + 1, 1, 0);
    encode(-128, writer, Value.INTEGER.start + 0, -128);
    encode(-129, writer, Value.INTEGER.start + 1, -1, -129);
    encode(-256, writer, Value.INTEGER.start + 1, -1, 0);

    encode(0x7fff, writer, Value.INTEGER.start + 1, 0x7f, 0xff);
    encode(0x8000, writer, Value.INTEGER.start + 2, 0, 0, 0x80, 0x00);
    encode(0x8001, writer, Value.INTEGER.start + 2, 0, 0, 0x80, 0x01);
    encode(-0x7fff, writer, Value.INTEGER.start + 1, 0x80, 0x01);
    encode(-0x8000, writer, Value.INTEGER.start + 1, 0x80, 0x00);
    encode(-0x8001, writer, Value.INTEGER.start + 2, -1, -1, 0x7F, 0xFF);

    encode(0x007fffffffL, lwriter, Value.INTEGER.start + 2, 0x7f, 0xff, 0xff, 0xff);
    encode(0x0080000000L, lwriter, Value.INTEGER.start + 3, 0, 0, 0, 0, 0x80, 0, 0, 0);
    encode(0x0080000001L, lwriter, Value.INTEGER.start + 3, 0, 0, 0, 0, 0x80, 0, 0, 1);
    encode(-0x007fffffffL, lwriter, Value.INTEGER.start + 2, 0x80, 0, 0, 1);
    encode(-0x0080000000L, lwriter, Value.INTEGER.start + 2, 0x80, 0, 0, 0);
    encode(-0x0080000001L, lwriter, Value.INTEGER.start + 3, -1, -1, -1, -1, 0x7F, 0xFF, 0xFF, 0xFF);

    encode(Long.MIN_VALUE + 1, lwriter, Value.INTEGER.start + 3, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01);
    encode(Long.MIN_VALUE + 0, lwriter, Value.INTEGER.start + 3, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
    encode(Long.MAX_VALUE - 1, lwriter, Value.INTEGER.start + 3, 0x7f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xfe);
    encode(Long.MAX_VALUE - 0, lwriter, Value.INTEGER.start + 3, 0x7f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff);
  }

  @Test
  void roundTrip() throws IOException {
    roundTrip(Value.INTEGER, 0, Integer.class, writer);
    roundTrip(Value.INTEGER, 1, Integer.class, writer);
    for (int i = Short.MIN_VALUE - 100; i < Short.MAX_VALUE + 100; i++)
      roundTrip(Value.INTEGER, i, Integer.class, writer);

    long[] edges = {
        Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2,//
        Integer.MIN_VALUE - 1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1,//
        Short.MIN_VALUE - 1, Short.MIN_VALUE, Short.MIN_VALUE + 1,//
        Byte.MIN_VALUE - 1, Byte.MIN_VALUE, Byte.MIN_VALUE + 1,//
        -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5,//
        Byte.MAX_VALUE - 1, Byte.MAX_VALUE, Byte.MAX_VALUE + 1,//
        Short.MAX_VALUE - 1, Short.MAX_VALUE, Short.MAX_VALUE + 1,//
        Integer.MAX_VALUE - 1, Integer.MAX_VALUE, Integer.MAX_VALUE + 1,//
        Long.MAX_VALUE - 2, Long.MAX_VALUE - 1, Long.MAX_VALUE};
    for (Long i : edges)
      roundTrip(Value.INTEGER, i, Long.class, lwriter);
  }

  @Test
  void roundTripRandomLong() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      roundTrip(Value.INTEGER, r.nextLong(), Long.class, lwriter);
    }
  }

  @Test
  void roundTripRandomInt() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      roundTrip(Value.INTEGER, r.nextInt(), Integer.class, writer);
    }
  }
}
