# Retrier

Java library to simplify retries of any function or code block (piece of code wrapped in lambda expression) without 
changing the execution flow from developer perspective. They can rely on the return value of the function/code block in retry, etc..

## Get Started

```java
import static io.retrier.Retry.*;

// Retrying on some piece of code which may return value or may throw exception.
// Result will be of same type as returned by the lambda expression.
Map<String, String> result = retry(() -> {
    // some operation which may throw exception
    return new HashMap<String, String>();
});


// Retrying on some piece of code which doesn't return any value but can throw exception
retry(() -> {
    // some operation which may throw exception
});
```

* `Retry.retry` is using the default `Retrier` with Retry Count `3`, Exponential Backoff initial Delay of `1 second` and retry timeout duration of `15 second`. 

## Background
Generally, we call an external system/service using some RPC mechanism (REST/GRPC/etc..) but we immediately inform about the failure to the client/user.
But in case if it would have been some transient failure (network issue/connectivity issue/etc..) then we could have retried and get success next time.
That would be a much nice experience to the client/user calling your service.

These kind of situation occurs in distributed system, microservice, mobile application and even within the same process space if some method/code block is throwing exception transiently.

There are many strategies to deal with retries and generally due to the induced complexity in the code, developer tends not to do it in while writing the first version of the program/software. 
This library fills that gap by providing a library, which in turn provides an elegant abstraction to deal with retries of any function/code-block.

The whole idea behind building thins library is to encourage developers to build more resilient systems.

## Features
* Retry on any function/code-block which returns value (of any type!) and may throw RuntimeException not declared as method signature.
* Retry on any function/code-block which does not returns any value and may throw RuntimeException not declared as method signature.
* Retries can capture the context by using lambda expression so function can have any number of arguments, passed in by the captured context in lambda expression.
* Retry upto number of times.
* Retry upto the timeout expires.
* Retry with exponential backoff delay.
* Retry with exponential backoff with max backoff delay.
* Retry on any Exception type so if the provided exception type `isAssignableFrom` the thrown exception then retry will happen.
* Retry but execute some piece of code when specific exception occurs (check is based on `isAssignableFrom`), like populate the data etc.
* Retry on nested exception wrapped within the other exception (as returned by getCause() method).
* Retry can have constraint from all the above permutation/combination.
* Retry returns back the exact same exception as thrown inside the retry block.

## Drawbacks
* Returned exception will be captured into `Exception` type, so user need to handle that if required.
But as the actual exception gets propogated so should be easily taken care of.

## When to use and not to use
Ensure you are have an [idempotent](https://en.wikipedia.org/wiki/Idempotence) operation to put it in retry block.

Example of `idempotent` operation:
* Put value in some store corresponding to a key without any condition.
* map.put(1, 2)
* get operations -- list or single item

Example of `non-idempotent` operation:
* Increment of a counter
* counter++
* map.putIfAbsent(1, 2)

## Usages 
Specifying below some typical retry use cases covered by the Retrier.

Assuming there are methods named `foo` and `bar` that throws `Exception` and following imports are in place:

```java
import static io.retrier.Retriers.*;
import static io.retrier.Retry.*;

void foo() throws Exception;
<T> T bar(T) throws Exception;
```

1. Retry number of times
```java
Retrier retrier = create(withRetryCount(3));

// Retring in lambda that doesn't return anything
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns integer. 
int result = retrier.retry(on(Exception.class), () -> bar(2));
```

2. Retry number of times until timeout expires
```java
Retrier retrier = create(withRetryCount(3), 
                        withTimeout(Duration.of(15, ChronoUnit.SECONDS)));
                        
// Retring in lambda that doesn't return anything                        
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns Object. 
Object result = retrier.retry(on(Exception.class), () -> bar(new Object()));
```

3. Retry unlimited number of times until timeout expires.
```java
Retrier retrier = create(withTimeout(Duration.of(15, ChronoUnit.SECONDS)));

// Retring in lambda that doesn't return anything 
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns a map. 
Map<String, String> result = retrier.retry(on(Exception.class), () -> {
    return new HashMap<String, String>();
});
```

4. Retry unlimited number of times with exponential backoff delay.
```java
Retrier retrier = create(withExpBackoff(Duration.of(3, ChronoUnit.SECONDS))));

// Retring in lambda that doesn't return anything 
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns a long. 
long result = retrier.retry(on(Exception.class), () -> {
    return 2L;
});
```

5. Retry unlimited number of times with exponential backoff delay and max backoff delay.
```java
Retrier retrier = create(withExpBackoff(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.SECONDS)));

// Retring in lambda that doesn't return anything 
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns a long. 
long result = retrier.retry(on(Exception.class), () -> {
    return 2L;
});
```

6. Retry unlimited number of times with exponential backoff delay and max backoff delay but on nested exception.
```java
Retrier retrier = create(withExpBackoff(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.SECONDS)));

// Retring in lambda that doesn't return anything 
retrier.retry(onNested(IllegalArgumentException.class), () -> foo());

// Retrying on lambda that returns a long. 
long result = retrier.retry(onNested(IllegalArgumentException.class), () -> {
    return 2L;
});
```

7. Retry number of times with timeout and exponential backoff with initial delay.
```java
Retrier retrier = create(withRetryCount(3), 
                        withTimeout(Duration.of(15, ChronoUnit.SECONDS)),
                        withExpBackoff(Duration.of(3, ChronoUnit.SECONDS)));
              
// Retring in lambda that doesn't return anything                         
retrier.retry(on(Exception.class), () -> foo());

// Retrying on lambda that returns a long.
long result = retrier.retry(on(Exception.class), () -> bar(2L));
```

8. Retry number of times with timeout, exponential backoff with initial delay and max backoff delay.
```java
Retrier retrier = create(withRetryCount(3), 
                        withTimeout(Duration.of(15, ChronoUnit.SECONDS)),
                        withExpBackoff(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.SECONDS)));
                        
retrier.retry(on(Exception.class), () -> foo());
Map<String, String> result = retrier.retry(on(Exception.class), () -> bar(new HashMap<String, String>()));
```

9. Retry number of times with timeout, exponential backoff with initial delay, max backoff delay and trace to see how retrier itself is working.

   Since withTrace accepts any lambda which takes `java.lang.String` as only parameter, so can be hooked with any type of logging infrastructure.
```java
Retrier retrier = create(withRetryCount(3), 
                        withTimeout(Duration.of(15, ChronoUnit.SECONDS)),
                        withExpBackoff(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.SECONDS)),
                        withTrace(System.out::println));
                        
retrier.retry(on(Exception.class), () -> foo());
Set<String> result = retrier.retry(on(Exception.class), () -> bar(new HashSet<String>()));
```

10. Retry number of times with timeout, exponential backoff with initial delay, max backoff delay and trace to see how retrier itself is working.
   Also if specific exception occurs then execute some piece of code.

   Since withTrace accepts any lambda which takes `java.lang.String` as only parameter, so can be hooked with any type of logging infrastructure.
```java
Retrier retrier = create(withRetryCount(3), 
                        withTimeout(Duration.of(15, ChronoUnit.SECONDS)),
                        withExpBackoff(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.SECONDS)),
                        withTrace(System.out::println));
                        
retrier.retry(on(IllegalArgumentException.class, ()-> {
    // Some handling of exception
}), () -> {
    foo()
});
retrier.retry(on(IllegalArgumentException.class, ()-> {
    // Some handling of exception
}), () -> {
    return bar()
});
```

## Utility Classes

`Exceptions` provides static method to `wrap` the exception thrown by the provided lambda expression into the runtime exception 
or keep the same runtime exception of thrown. This way it helps to convert a method which throws an exception to a non exception throwing method.

```java
Exceptions.wrap(() -> {
    throw new Exception()
})
```

```java
long result = Exceptions.wrap(() -> {
    // if some contition
    throw new Exception("bla bla")
    return 2L;
})
```


## Credits
* This library is inspired by my previous experience in dealing with microservices and handling failures.