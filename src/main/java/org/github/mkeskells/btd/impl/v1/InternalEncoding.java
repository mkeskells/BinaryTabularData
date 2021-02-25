package org.github.mkeskells.btd.impl.v1;

import org.github.mkeskells.btd.coder.Encoding;

import java.util.ArrayList;
import java.util.List;

class InternalCoodings {
  private final static List<Encoding<?>> all;

  static {
    List<Encoding<?>> temp = new ArrayList<>();
    temp.add(Value.STRING);

    all = List.copyOf(temp);
  }

  public static List<Encoding<?>> all() {
    return all;
  }
}