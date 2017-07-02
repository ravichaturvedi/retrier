package io.retrier.handler;


import io.retrier.Handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompositeHandler implements Handler {

  private final List<Handler> handlers;

  public CompositeHandler(Handler... handlers) {
    this.handlers = Collections.unmodifiableList(Arrays.asList(handlers));
  }

  @Override
  public void handleException(Exception e) throws Exception {
    // Try to get the exception handled by all the retry handler and short-circuit on the first successful handling.
    for (Handler handler : handlers) {
      try {
        handler.handleException(e);
        return;
      } catch (Exception ex) {
        // If exception instance is not same as passed to handleException then raise it.
        if (ex != e) {
          throw ex;
        }
      }
    }

    // If none of the retry handle handles the exception then propogate it.
    throw e;
  }
}
