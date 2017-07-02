package io.retrier.handler;


import io.retrier.Handler;

public class RetryCountHandler implements Handler {

  private final int retryCount;

  public RetryCountHandler(int retryCount) {
    if (retryCount < 0) {
      throw new IllegalArgumentException("Retry count should be positive.");
    }

    this.retryCount = retryCount;
  }

  @Override
  public void handleException(Exception e) throws Exception {
    
  }
}
