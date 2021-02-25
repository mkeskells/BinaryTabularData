package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

public class FloatValueTest extends ValueTest {
  Writer<Float> writer = (data, value, scratch) -> Value.FLOAT.write(data, value);

  @Test
  void encode() throws IOException {

    encode(0.0f, writer, Value.FLOAT.start + FloatingValue.ZERO);
    encode(1.0f, writer, Value.FLOAT.start + FloatingValue.ONE);
    encode(-1.0f, writer, Value.FLOAT.start + FloatingValue.MINUS_ONE);
    encode(Float.NaN, writer, Value.FLOAT.start + FloatingValue.NAN);

    encode(1000.0f, writer, Value.FLOAT.start + FloatValue.FLOAT, 0x44, 0x7A, 0x00, 0x00);
    encode(5000.0f, writer, Value.FLOAT.start + FloatValue.FLOAT, 0x45, 0x9C, 0x40, 0x00);

    encode(Float.MIN_VALUE, writer, Value.FLOAT.start + FloatValue.FLOAT, 0, 0, 0, 1);
    encode(Float.MAX_VALUE, writer, Value.FLOAT.start + FloatValue.FLOAT, 0x7f, 0x7f, 0xff, 0xff);

  }

  @Test
  void roundTrip() throws IOException {
    roundTrip(Value.FLOAT, 0.0f, Float.class, writer);
    roundTrip(Value.FLOAT, 1.0f, Float.class, writer);
    roundTrip(Value.FLOAT, -1.0f, Float.class, writer);
    roundTrip(Value.FLOAT, Float.NaN, Float.class, writer);
  }

  @Test
  void roundTripRandomFloat() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      roundTrip(Value.FLOAT, r.nextFloat(), Float.class, writer);
    }
  }

}
