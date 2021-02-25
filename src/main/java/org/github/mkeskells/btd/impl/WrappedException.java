package org.github.mkeskells.btd.impl;

public class WrappedException extends RuntimeException {
  public WrappedException(Throwable t) {
    super(t);
  }

  public static class WrappedReflectiveOperationException extends WrappedException {
    public WrappedReflectiveOperationException(ReflectiveOperationException t) {
      super(t);
    }

    public ReflectiveOperationException getCause() {
      return (ReflectiveOperationException) super.getCause();
    }
  }
}
