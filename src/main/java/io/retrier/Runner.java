package io.retrier;


/**
 * Runner runs the code block wrapped within the lambda expression.
 */
@FunctionalInterface
public interface Runner {

  /**
   * Run the implementation
   * @throws Exception If underlying implementation throws.
   */
  void run() throws Exception;
}
