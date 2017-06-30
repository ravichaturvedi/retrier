package io.retrier;


import org.junit.Test;

public class TestRetrier {

  @Test
  public void testRetrier() throws Exception {
    Retrier retrier = new Retrier();
    retrier.retry(e -> {}, () -> {
      System.out.println("Hello");
    });
  }
}
