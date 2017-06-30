package io.retrier;


/**
 * Provider provides the value after executing the code block wrapped within the lambda expression.
 * @param <V> Type of the returned value.
 */
@FunctionalInterface
public interface Provider<V> {

  /**
   * Provide the value after executing the implementation.
   * @throws Exception If underlying implementation throws.
   */
  V provide() throws Exception;
}
