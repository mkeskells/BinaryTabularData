package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class NullValueTest extends ValueTest {

  @Test
  void encode() throws IOException {
    encode(Boolean.FALSE, (data, value, scratch) -> Value.BOOLEAN.write(data, value), 1);
    encode(Boolean.TRUE, (data, value, scratch) -> Value.BOOLEAN.write(data, value), 2);
  }

  @Test
  void roundTrip() throws IOException {
    roundTrip(Value.BOOLEAN, Boolean.FALSE, Boolean.class, (data, value, scratch) -> Value.BOOLEAN.write(data, value));
    roundTrip(Value.BOOLEAN, Boolean.TRUE, Boolean.class, (data, value, scratch) -> Value.BOOLEAN.write(data, value));
  }

}
