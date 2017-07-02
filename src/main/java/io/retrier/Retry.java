package io.retrier;


import io.retrier.handler.CompositeHandler;
import io.retrier.handler.ExceptionHandler;
import io.retrier.handler.RetryCountHandler;

public class Retry {

  public static Handler on(Handler... handlers) {
    return new CompositeHandler(handlers);
  }

  public static Handler withCount(int retryCount) {
    return new RetryCountHandler(retryCount);
  }

  public static Handler withExceptions(Class<Exception>... exceptionClasses) {
    return new ExceptionHandler(exceptionClasses);
  }

}
