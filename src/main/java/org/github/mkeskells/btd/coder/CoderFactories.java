package org.github.mkeskells.btd.coder;

import org.github.mkeskells.btd.impl.WrappedException;

/**
 * A factory of {@link Coder}
 * <p>
 * All operations are threadsafe
 */
public class CoderFactories {
  private CoderFactories() {
  }

  public static CoderFactory getCoderFactory(String className) throws ReflectiveOperationException {
    if (className == null || className.isEmpty())
      return DefaultCoderFactory.instance();
    return getCoderFactory(Class.forName(className).asSubclass(CoderFactory.class));
  }

  private static ClassValue<CoderFactory> lookup = new ClassValue<CoderFactory>() {
    @Override
    protected CoderFactory computeValue(Class<?> type) {
      try {
        return type.asSubclass(CoderFactory.class).getDeclaredConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
        throw new WrappedException.WrappedReflectiveOperationException(e);
      }
    }
  };

  public static CoderFactory getCoderFactory(Class<? extends CoderFactory> clazz) throws ReflectiveOperationException {
    try {
      return lookup.get(clazz);
    } catch (WrappedException.WrappedReflectiveOperationException e) {
      throw e.getCause();
    }
  }
}
