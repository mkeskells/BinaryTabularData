package org.github.mkeskells.btd.coder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * a factory for Coders
 * A {@link Coder} is specified by a config, and for the same config his factory will generate the same Coder
 * unless it has been removed via {@link #forConfig(CoderParams)} or {@link #forgetAllConfig()}
 * <p>
 * All operations a threadsafe
 */
public abstract class CoderFactory {
  protected final ConcurrentHashMap<CoderParams, Coder> knownConfigs = new ConcurrentHashMap<>();

  /**
   * return a cached, or freshly created a {@link Coder} for the specified config
   */
  public Coder forConfig(String config, String... columns) {
    return forConfig(new CoderParams(columns, config));
  }

  /**
   * return a cached, or freshly created a {@link Coder} for the specified config
   */
  public Coder forConfig(CoderParams config) {
    return knownConfigs.computeIfAbsent(config, this::createForConfig);
  }

  /**
   * create a {@link Coder} for the specified config
   * The result is cached
   * <p>
   * The generated {@link Coder} must retain the config, and return that when {@link Coder#getConfig()} is called
   */
  protected abstract Coder createForConfig(CoderParams config);

  /**
   * remove the specified config from the cache
   */
  public void forgetConfig(CoderParams config) {
    knownConfigs.remove(config);
  }

  public void forgetAllConfig() {
    knownConfigs.clear();
  }
}
