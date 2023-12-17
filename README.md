# Concurrency Library
This repository provides a robust set of tools to seamlessly manage concurrency and parallelism within your projects.
Whether you need thread pooling, single-threaded execution, or virtual thread support, this library has you covered.

### Maven
```
<dependency>
  <groupId>io.github.surajkumar</groupId>
  <artifactId>concurrency-library</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Gradle
`implementation "io.github.surajkumar:concurrency-library:1.0.0"`

## Execution Machines
Execution Machines are the backbone of this library, handling the execution of tasks efficiently.

The following machines come bundled by default:

1. **PooledExecutionMachine**: Utilizes a thread pool for task execution.
2. **SingleThreadedExecutionMachine**: Executes tasks on a single thread.
3. **ThreadPerTaskExecutionMachine**: Allocates a new thread for each task.
4. **VirtualThreadPerTaskExecutionMachine**: Runs tasks on virtual threads.

## Promises
Promises offer a concise way to manage asynchronous tasks. Create a promise and define its behavior:

`Promise<String> promise = new Promise(() -> "Hello, World!");`

Enhance promises with handlers:

```java
promise.onResolve(System.out::println);
promise.onReject(System.out::println);
```

Chain promises for streamlined execution:

```java
Promise<String> promise = new Promise(() -> "Hello, World!")
        .onResolve(System.out::println)
        .onReject(System.out::println);
```

Execute promises using the provided `Executor`:

```java
Executor executor = new Executor(new PooledExecutionMachine(new DynamicThreadPool()));
executor.run(promise);
```
`run` accepts an array of promises, and executor.join does the same but waits for their completion.

## Metrics
Track performance metrics for channels, promises, executions, and thread pools. Obtain metrics using the getMetrics() method:
```java
System.out.println(promise.getMetrics()); // To get metrics of a Promise
// > PromiseMetrics{start=140720275666100, end=140720275677900, success=true, executionTime=11800, memoryUsage=0, errorDetails='', stackTrace=[]}
```

## Pools
Tailor the behavior of ExecutionMachine instances with pool options. Choose from available pools like:

1. **DynamicThreadPool**
2. **FixedThreadPool**

## Channels
Facilitate safe communication between multiple threads using channels. Lightweight and versatile, channels enable seamless interaction:

```java
Channel<String> channel = new Channel(); // A channel for handling String messages

class User implements ChannelObserver<String> {
    @Override
    public void onMessageReceived(Channel<T> channel, Message<T> message) {
        // Handle the message
        channel.sendMessage(new Message("Hello back!", this));
    }
}

User alice = new User();
User bob = new User();

channel.register(alice);
channel.register(bob);

channel.sendMessage(new Message("Hello", bob)); // Send a message on the channel from bob
```

Improve the scalability and efficiency of your concurrent applications using this powerful concurrency library.