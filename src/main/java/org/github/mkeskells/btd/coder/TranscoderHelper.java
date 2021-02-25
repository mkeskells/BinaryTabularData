package org.github.mkeskells.btd.coder;

public class TranscoderHelper {
  private TranscoderHelper() {
  }

  @SuppressWarnings("unchecked")
  public static <T> T convert(Class<T> to, Object value) {
    if (to.isInstance(value))
      return (T) value;
    if (to == String.class)
      return (T) value.toString();
    if (Number.class.isAssignableFrom(to)) {
      // we should be able to check for Number conversion
      //but its hard to do without loss of accuracy
      //so we parse ...
      String s = value.toString();
      if (to == Integer.class || to == Long.class)
        return parseWholeNumber(to, s);
      if (to == Double.class || to == Float.class) {
        return parseOtherNumber(to, value);
      }
      if (to == Number.class) {
        try {
          return parseWholeNumber(to, s);
        } catch (NumberFormatException nfe) {
          return parseOtherNumber(to, value);
        }
      }
    }
    if (to == Boolean.class) {
      String s = value.toString();
      if ("true".equalsIgnoreCase(s))
        return (T) Boolean.TRUE;
      if ("false".equalsIgnoreCase(s))
        return (T) Boolean.FALSE;
    }
    if (to != Enum.class && value instanceof String && Enum.class.isAssignableFrom(to)) {
      return (T) Enum.valueOf(to.asSubclass(Enum.class), (String) value);
    }
    throw new UnsupportedOperationException("cant convert to a " + to + " from a " + value.getClass() + " <" + value + ">");
  }

  @SuppressWarnings("unchecked")
  private static <T> T parseOtherNumber(Class<T> to, Object value) {
    double d = Double.parseDouble(value.toString());
    if (to == Double.class) {
      return (T) Double.valueOf(d);
    }
    //not perfect maybe loss of detail, but not sign & top bits
    if (to == Float.class) {
      return (T) Float.valueOf((float) d);
    }
    // to == Number.class
    return (T) Double.valueOf(d);
  }

  @SuppressWarnings("unchecked")
  private static <T> T parseWholeNumber(Class<T> to, String s) {
    long v = Long.parseLong(s);
    if (to == Integer.class) {
      if ((int) v == v)
        return (T) Integer.valueOf((int) v);
      else
        throw new NumberFormatException("cant convert " + v + " to a " + to + " without loss of precision");
    }
    if ((int) v == v && to.isAssignableFrom(Integer.class)) {
      return (T) Integer.valueOf((int) v);
    }
    return (T) Long.valueOf(v);
  }

}
