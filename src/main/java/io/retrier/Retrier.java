package io.retrier;

public class Retrier {

  <T> T retry(Handler handler, Provider<T> provider) throws Exception {
    while (true) {
      try {
        handler.handlePreExec();
        T result = provider.provide();
        handler.handlePostExec(result);
        return result;
      } catch (Exception e) {
        handler.handleException(e);
      }
    }
  }

  void retry(Handler handler, Runner runner) throws Exception {
    retry(handler, () -> {
      runner.run();
      return null;
    });
  }
}
