package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooleanValueTest extends ValueTest {

  private Writer<Boolean> writer = (data, value, scratch) -> Value.BOOLEAN.write(data, value);


  @Test
  void encode() throws IOException {
    encode(Boolean.FALSE, writer, 1);
    encode(Boolean.TRUE, writer, 2);
  }

  @Test
  void roundTrip() throws IOException {
    roundTrip(Value.BOOLEAN, Boolean.FALSE, Boolean.class, writer);
    roundTrip(Value.BOOLEAN, Boolean.TRUE, Boolean.class, writer);
  }

  @Test
  void roundTripString() throws IOException {
    assertEquals("false", roundTripToString(Value.BOOLEAN, Boolean.FALSE, writer));
    assertEquals("true", roundTripToString(Value.BOOLEAN, Boolean.TRUE, writer));
  }

}
