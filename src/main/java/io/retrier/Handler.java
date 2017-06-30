package io.retrier;

public interface Handler {

  default void handlePreExec() {

  }

  default <T> void handlePostExec(T result) {

  }

  void handleException(Exception e) throws Exception;
}
