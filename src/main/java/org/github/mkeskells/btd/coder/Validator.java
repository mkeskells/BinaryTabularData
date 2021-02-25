package org.github.mkeskells.btd.coder;

public interface Validator {
  void validate(Object value);

  void validate(long value);

  void validate(double value);

  void validate(boolean value);

}
