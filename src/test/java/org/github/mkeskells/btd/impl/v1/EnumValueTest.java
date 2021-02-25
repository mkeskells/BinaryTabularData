package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

enum A {A, B, C}

enum B {A, B, C}

public class EnumValueTest extends ValueTest {

  EnumValue.LocalData writePrivateData;

  @BeforeEach
  void resetPrivateData() {
    writePrivateData = Value.ENUM.generatePrivateData();
  }

  Writer<Enum> writer = (out, value, scratch) -> Value.ENUM.encodeValue(out, value, scratch, writePrivateData);

  @Test
  void encode() throws IOException {
    encode(A.A, writer, Value.ENUM.start + EnumValue.DECLARE_CLASS,
        //string class name "org.github.mkeskells.btd.impl.v1.A" mapped to id 0
        Value.STRING.start + StringValue.SHORT_BYTE_ENCODED,
        "org.github.mkeskells.btd.impl.v1.A".length(),
        'o', 'r', 'g', '.', 'g', 'i', 't', 'h', 'u', 'b', '.', 'm', 'k', 'e', 's', 'k', 'e', 'l', 'l', 's',
        '.', 'b', 't', 'd', '.', 'i', 'm', 'p', 'l', '.', 'v', '1', '.', 'A',
        //string enum name A.A mapped to id 0
        Value.STRING.start + StringValue.BYTES_1, 'A'
    );
    encode(A.A, writer, Value.ENUM.start + EnumValue.VALUE_1,
        // id 0
        0);
    encode(A.C, writer, Value.ENUM.start + EnumValue.DECLARE_VALUE_1,
        //class id for  org.github.mkeskells.btd.impl.v1.A
        0,
        //string enum name A.C mapped to id 1
        Value.STRING.start + StringValue.BYTES_1, 'C');
    encode(B.B, writer, Value.ENUM.start + EnumValue.DECLARE_CLASS,
        //string class name "org.github.mkeskells.btd.impl.v1.B" mapped to id 1
        Value.STRING.start + StringValue.SHORT_BYTE_ENCODED,
        "org.github.mkeskells.btd.impl.v1.B".length(),
        'o', 'r', 'g', '.', 'g', 'i', 't', 'h', 'u', 'b', '.', 'm', 'k', 'e', 's', 'k', 'e', 'l', 'l', 's',
        '.', 'b', 't', 'd', '.', 'i', 'm', 'p', 'l', '.', 'v', '1', '.', 'B',
        //string enum name A.B mapped to id 2
        Value.STRING.start + StringValue.BYTES_1, 'B'
    );
    encode(A.B, writer, Value.ENUM.start + EnumValue.DECLARE_VALUE_1,
        //class id for  org.github.mkeskells.btd.impl.v1.A
        0,
        //string enum name A.B mapped to id 3
        Value.STRING.start + StringValue.BYTES_1, 'B');
    encode(B.A, writer, Value.ENUM.start + EnumValue.DECLARE_VALUE_1,
        //class id for  org.github.mkeskells.btd.impl.v1.B
        1,
        //string enum name B.A mapped to id 4
        Value.STRING.start + StringValue.BYTES_1, 'A');
    encode(A.A, writer, Value.ENUM.start + EnumValue.VALUE_1,
        // id 0
        0);
    encode(B.A, writer, Value.ENUM.start + EnumValue.VALUE_1,
        // id 4
        4);

  }

  @Test
  void roundTrip() throws IOException {
    Set<Enum> all = new HashSet<>();
    all.addAll(Arrays.asList(A.values()));
    all.addAll(Arrays.asList(B.values()));

    for (Enum e : all) {
      //registration
      roundTrip(Value.ENUM, e, e.getClass().asSubclass(Enum.class), writer);
      //reuse
      roundTrip(Value.ENUM, e, e.getClass().asSubclass(Enum.class), writer);
    }
  }

  @Test
  void roundTripString() throws IOException {
    Set<Enum> all = new HashSet<>();
    all.addAll(Arrays.asList(A.values()));
    all.addAll(Arrays.asList(B.values()));

    for (Enum e : all) {
      //registration
      assertEquals(e.toString(), roundTripToString(Value.ENUM, e, writer));
      //reuse
      assertEquals(e.toString(), roundTripToString(Value.ENUM, e, writer));
    }
  }
}
