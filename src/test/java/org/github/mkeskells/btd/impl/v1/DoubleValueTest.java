package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

public class DoubleValueTest extends ValueTest {
  Writer<Double> writer = (data, value, scratch) -> Value.DOUBLE.write(data, value);

  @Test
  void encode() throws IOException {
    encode(0.0, writer, Value.DOUBLE.start + FloatingValue.ZERO);
    encode(1.0, writer, Value.DOUBLE.start + FloatingValue.ONE);
    encode(-1.0, writer, Value.DOUBLE.start + FloatingValue.MINUS_ONE);
    encode(Double.NaN, writer, Value.DOUBLE.start + FloatingValue.NAN);

    encode(1000.0d, writer, Value.DOUBLE.start + DoubleValue.FLOAT, 0x44, 0x7a, 0x00, 0x00);
    encode(5000.0d, writer, Value.DOUBLE.start + DoubleValue.FLOAT, 0x45, 0x9C, 0x40, 0x00);

    encode(Double.MIN_VALUE, writer, Value.DOUBLE.start + DoubleValue.DOUBLE, 0, 0, 0, 0, 0, 0, 0, 1);
    encode(Double.MAX_VALUE, writer, Value.DOUBLE.start + DoubleValue.DOUBLE, 0x7f, 0xef, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff);

  }

  @Test
  void roundTrip() throws IOException {
    roundTrip(Value.DOUBLE, 0.0, Double.class, writer);
    roundTrip(Value.DOUBLE, 1.0, Double.class, writer);
    roundTrip(Value.DOUBLE, -1.0, Double.class, writer);
    roundTrip(Value.DOUBLE, Double.NaN, Double.class, writer);
  }

  @Test
  void roundTripRandomDouble() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      roundTrip(Value.DOUBLE, r.nextDouble(), Double.class, writer);
    }
  }

}
