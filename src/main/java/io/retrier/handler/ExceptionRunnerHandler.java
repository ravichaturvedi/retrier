package io.retrier.handler;


import io.retrier.Handler;
import io.retrier.Runner;

public class ExceptionRunnerHandler implements Handler {

  private final Exception exception;
  private final Runner runner;

  public ExceptionRunnerHandler(Exception exception, Runner runner) {
    this.exception = exception;
    this.runner = runner;
  }

  @Override
  public void handleException(Exception e) throws Exception {
    
  }
}
