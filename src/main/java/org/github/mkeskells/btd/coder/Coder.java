package org.github.mkeskells.btd.coder;

import java.util.List;

/**
 * the layout and rules for the tabular data
 */
public abstract class Coder {
  private CoderParams config;

  protected Coder(CoderParams config) {
    this.config = config;
  }

  /**
   * the configuration of the coder. This is transmitted in the tabular data, so that the configuration can be rehydrated
   */
  public abstract Class<? extends CoderFactory> getFactoryClass();

  /**
   * the configuration of the coder. This is transmitted in the tabular data, so that the configuration can be rehydrated
   */
  public CoderParams getConfig() {
    return config;
  }

  public abstract List<Column> getColumns();

}
