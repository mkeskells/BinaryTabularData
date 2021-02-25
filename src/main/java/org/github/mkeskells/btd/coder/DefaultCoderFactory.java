package org.github.mkeskells.btd.coder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultCoderFactory extends CoderFactory {
  private DefaultCoderFactory() {
  }

  private final static DefaultCoderFactory instance = new DefaultCoderFactory();

  public static CoderFactory instance() {
    return instance;
  }

  @Override
  protected Coder createForConfig(CoderParams config) {
    return new DefaultCoder(config);
  }

  private static class DefaultCoder extends Coder {
    private final List<Column> columns;

    DefaultCoder(CoderParams config) {
      super(config);
      this.columns = config.columns.stream().map(name -> new Column(name, false, false, true,
          Optional.empty(), Integer.MAX_VALUE, Collections.emptyList(), Collections.emptyList(), true)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Class<? extends CoderFactory> getFactoryClass() {
      return DefaultCoderFactory.class;
    }

    @Override
    public List<Column> getColumns() {
      return columns;
    }
  }
}
