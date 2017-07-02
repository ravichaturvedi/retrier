package io.retrier.handler;


import io.retrier.Handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExceptionHandler implements Handler {

  private final Set<Class<Exception>> exceptionClasses;

  public ExceptionHandler(Class<Exception>... exceptionClasses) {
    this.exceptionClasses = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(exceptionClasses)));
  }

  @Override
  public void handleException(Exception e) throws Exception {

  }
}
