# Java concurrency and parallelism: executors and thread pools

## Introduction

In small applications to execute each task (_Runnable_ object) is created a new thread (_Thread_ object). When the task is completed, the thread is terminated as well. But in large applications overhead of threads creation and termination can be significant. Such overhead can be reduced by reusing the same threads for the execution of many tasks. For that purpose are used _executors_ and _thread pools_. An _executor_ is a design pattern that provides API for task executions and hides its implementation. A _thread pool_ is one of the _executor_ implementations that uses a pool of threads for task execution.

In this article are provided practical examples of using _executors_ and _thread pools_ from the _java.util.concurrent_ package. Here are described the following classes and interfaces:

1) Executors

*   the _Executor_ interface
*   the _ExecutorService_ interface
*   the _ScheduledExecutorService_ interface
*   the _CompletionService_ interface

2) Thread pools

*   the _ThreadPoolExecutor_ class
*   the _ThreadFactory_ interface
*   the _ScheduledThreadPoolExecutor_ class
*   the _ExecutorCompletionService_ class
*   the _ForkJoinPool_ class

3) The _Executors_ class

The code examples are compiled with Java 9 and expected to be run on a computer with at least a 4-core processor.

## The Thread class

In general, the workflow of task execution using thread consists of the following steps:

1. task creation
2. task submission
3. thread creation
4. thread start
5. task execution
6. thread termination

There are two ways to manage threads during this workflow: explicit and implicit.

In an explicit way, a thread is created inextricably with a task, then the thread executes the submitted task, and finally, the thread is terminated after the task completion. In this way, the workflow of a task (_Runnable_ object) and a thread (_Thread_ object) is tightly connected.

```
Runnable runnable = new Runnable() { // task creation
   @Override
   public void run() {
       logger.info("run..."); // task execution
   }
};

Thread thread = new Thread(runnable); // task submission during thread creation
thread.start(); // thread start

thread.join(); // thread termination
```

In an implicit way, a thread is created, executes tasks, and is terminated independently from a task creation, submission, and execution. In this way, the workflow of the task (_Runnable_ object) and the thread (_Thread_ object) is loosely connected.

```
Executor executor = createExecutor(); // thread creation and thread start

Runnable task = createTask(); // task creation
Future future = executor.submit(task); // task submission
future.get(); // waiting until task is executed

executor.terminate(); // thread termination
```

## Executors

Executors are interfaces that provide API for threads creation, utilization, and termination for the rest of the application.

In the _java.util.concurrent_ package there are three executors interfaces:

*   _Executor_, that has a method to execute tasks
*   _ExecutorService_, that has methods to execute tasks with the ability to control their completion, and methods to manage executor termination
*   _ScheduledExecutorService_, that has methods to execute tasks once after a given delay, and methods to execute tasks periodically

### The Executor interface

The _Executor_ interface decouples the interface of tasks submitted from the implementation of task execution. Indeed tasks can be executed asynchronously (in another thread) as well as synchronously (in the caller’s thread).

The Executor interface has a single method:

*   _void execute(Runnable command)_

The return type of the method is _void_, so there is no information about the task completion and no ability to cancel the task.

##### Examples

Example of a synchronous _Executor_:

```
Runnable runnable = () -> logger.info("run...");

Executor executor = new Executor() {
   @Override
   public void execute(Runnable command) {
       command.run();
   }
};

executor.execute(runnable);
```

Example of an asynchronous _Executor_ that executes each task in a new _Thread_:

```
Runnable runnable = () -> logger.info("run...");

Executor executor = new Executor() {
   @Override
   public void execute(Runnable command) {
       new Thread(command).start();
   }
};

executor.execute(runnable);
```

Example of a predefined asynchronous _Executor_ that execute each task in a reused _Thread_:

```
Runnable runnable = () -> logger.info("run...");

Executor executor = Executors.newSingleThreadExecutor();

executor.execute(runnable);
```

### The ExecutorService interface

The _ExecutorService_ interface extends the _Executor_ interface and additionally has methods to execute tasks with the ability to control their completion, and methods to manage executor termination.

The executor methods to submit single _Runnable_ or _Callable_ tasks:

*   _Future&lt;?> submit(Runnable task)_
*   _&lt;T> Future&lt;T> submit(Runnable task, T result)_
*   _&lt;T> Future&lt;T> submit(Callable&lt;T> task)_

Unlike the _execute_ method, the _submit_ methods return _Future&lt;V>_ that has methods to control task completion: 

*   _V get​()_  - waits for the task completion
*   _V get​(long timeout, TimeUnit unit)_ - waits for the task completion for the given timeout
*   _boolean cancel​(boolean mayInterruptIfRunning)_ - attempts to cancel the task 
*   _boolean isDone​()_ - informs whether the task is completed or not
*   _boolean isCancelled​()_ - informs whether the task was completed normally or canceled

The executor methods to submit multiple _Callable_ tasks:

*   _&lt;T> List&lt;Future&lt;T>>	invokeAll(Collection&lt;? extends Callable&lt;T>> tasks)_
*   _&lt;T> T invokeAny(Collection&lt;? extends Callable&lt;T>> tasks)_

The _invokeAll, invokeAny_ methods have overloaded versions with the maximum allowed timeout.

The executor methods to control executor termination:

*   _void shutdown()_
*   _List&lt;Runnable> shutdownNow()_
*   _boolean awaitTermination(long timeout, TimeUnit unit)_
*   _boolean isShutdown()_
*   _boolean isTerminated()_

It’s important that an _ExecutorService_ instance isn’t terminated automatically if there are no tasks to execute. It continues to wait for new tasks until it is shut down manually.

##### Examples

Example of the _submit_ method with _Runnable_ parameter:

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

Runnable runnable = () -> System.out.println("run...");
Future<?> future = executorService.submit(runnable);
System.out.println("result: " + future.get()); // null

executorService.shutdown();
```

Example of the _submit_ method with _Runnable_ parameter and return value:

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

Runnable runnable = () -> System.out.println("run...");
Future<String> future = executorService.submit(runnable, "Alpha");
System.out.println("result: " + future.get());

executorService.shutdown();
```

Example of the _submit_ method with _Callable_ parameter:

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

Callable<String> callable = () -> "Bravo";
Future<String> future = executorService.submit(callable);
System.out.println("result: " + future.get());

executorService.shutdown();
```

Example of the _invokeAll_ method:

(if the overloaded method with timeout is used, and the timeout expires before all tasks are successfully completed, the late tasks are not completed)

```
ExecutorService executorService = Executors.newCachedThreadPool();

List<Callable<String>> callables = Arrays.asList(
       () -> sleepAndGet(2, "Bravo"),
       () -> sleepAndGet(1, "Alpha"),
       () -> sleepAndGet(3, "Charlie")
);

List<String> results = executorService.invokeAll(callables)
       .stream()
       .peek(future -> logger.info("is done: {}, is cancelled: {}",
               future.isDone(),
               future.isCancelled()))
       .map(future -> {
           try {
               return future.get();
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       })
       .collect(Collectors.toList());

logger.info("results: {}", results);

executorService.shutdown();
```

Example of the _invokeAny_ method:

(if the overloaded method with timeout is used, and the timeout expires before any task is successfully completed, a _java.util.concurrent.TimeoutException_ is thrown)

```
ExecutorService executorService = Executors.newCachedThreadPool();

List<Callable<String>> callables = Arrays.asList(
       () -> sleepAndGet(2, "Bravo"),
       () -> sleepAndGet(1, "Alpha"),
       () -> sleepAndGet(3, "Charlie")
);

String result = executorService.invokeAny(callables);
logger.info("result: {}", result);

executorService.shutdown();
```

Example of the _shutdown, isShutdown_ methods: 

(the _shutdown_ method starts an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted; the method does not wait for previously submitted tasks to complete execution)

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.submit(() -> sleepAndGet(1, "Alpha"));
executorService.submit(() -> sleepAndGet(1, "Bravo"));
executorService.submit(() -> sleepAndGet(1, "Charlie"));

logger.info("is shutdown: {}", executorService.isShutdown());

logDuration(
       "shutdown",
       () -> executorService.shutdown()
);

logger.info("is shutdown: {}", executorService.isShutdown());
```

Example of task rejection after calling _shutdown_ method:

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

logger.info("is shutdown: {}", executorService.isShutdown());
executorService.shutdown();
logger.info("is shutdown: {}", executorService.isShutdown());

executorService.submit(() -> "Alpha"); // java.util.concurrent.RejectedExecutionException
```

Example of the _shutdownNow, isTerminated_ methods:

(the _shutdownNow_ method tries to stop all actively executing tasks, halts the waiting tasks, and returns a list of the tasks that were awaiting execution; the method does not wait for actively executing tasks to terminate)

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.submit(() -> sleepAndGet(1, "Alpha"));
executorService.submit(() -> sleepAndGet(1, "Bravo"));
executorService.submit(() -> sleepAndGet(1, "Charlie"));

logger.info("is terminated: {}", executorService.isTerminated());

logDuration(
       "shutdownNow",
       () -> {
           List<Runnable> skippedTasks = executorService.shutdownNow();
           logger.info("count of tasks never commenced execution: {}", skippedTasks.size());
       }
);

logger.info("is terminated: {}", executorService.isTerminated());
```

Example of the _awaitTermination_ method:

(the _awaitTermination_ method waits until one of the events occurs:

*   all tasks have completed
*   the timeout expires
*   the current thread is interrupted)

```
ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.submit(() -> sleepAndGet(1, "Alpha"));
executorService.submit(() -> sleepAndGet(1, "Bravo"));
executorService.submit(() -> sleepAndGet(1, "Charlie"));

executorService.shutdown();

logDuration(
       "awaitTermination",
       () -> executorService.awaitTermination(60, TimeUnit.SECONDS)
);
```

Example of the the [recommended way](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/concurrent/ExecutorService.html) of shutting down an _ExecutorService_ instance:

```
logger.debug("executor service: shutdown started");
executorService.shutdown();
try {
   if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
       List<Runnable> skippedTasks = executorService.shutdownNow();
       logger.error("count of tasks never commenced execution: {}", skippedTasks.size());
       if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
           logger.error("executor service didn't terminate");
       }
   }
} catch (InterruptedException e) {
   logger.error("executor service is interrupted", e);
   executorService.shutdownNow();
   Thread.currentThread().interrupt();
}
logger.debug("executor service: shutdown finished");
```

### The ScheduledExecutorService interface

The _ScheduledExecutorService_ interface extends the _ExecutorService_ interface and additionally has methods to execute tasks once after a delay, and methods to execute tasks periodically until canceled.

Methods to schedule tasks to execute once after a delay:

*   _ScheduledFuture&lt;?> schedule(Runnable command, long delay, TimeUnit unit)_
*   _&lt;V> ScheduledFuture&lt;V> schedule(Callable&lt;V> callable, long delay, TimeUnit unit)_

The methods return _ScheduledFuture&lt;V>_ (that extends _Future&lt;V>_) that has a method to control task completion (delayed or periodical):

*   method _long getDelay​(TimeUnit unit)_ - returns the remaining delay associated with the task

Methods to schedule tasks to execute periodically:

*   _ScheduledFuture&lt;?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)_
*   _ScheduledFuture&lt;?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)_

It’s important that _ScheduledFuture_ that is returned from the _scheduleAtFixedRate, scheduleWithFixedDelay_ methods is never completed while the task is periodically executing. The sequence of task executions continues indefinitely until one of the events occurs:

*   the executor is terminated
*   the task is canceled by the returned _ScheduledFuture_
*   execution of the task throws an exception

##### Examples

Example of the _schedule_ method with _Runnable_ parameter:

```
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

Runnable runnable = () -> logger.info("finished");

logger.info("started");
ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(runnable, 3000, TimeUnit.MILLISECONDS);

TimeUnit.MILLISECONDS.sleep(1000);

long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
logger.info("remaining delay: {} millisecond(s)", remainingDelay);

logger.info("result: {}", scheduledFuture.get());

shutdown(scheduledExecutorService);
```

Example of the _schedule_ method with _Callable_ parameter:

```
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

Callable<String> callable = () -> {
   logger.info("finished");
   return "Alpha";
};

logger.info("started");
ScheduledFuture<String> scheduledFuture = scheduledExecutorService.schedule(callable, 3000, TimeUnit.MILLISECONDS);

TimeUnit.MILLISECONDS.sleep(1000);

long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
logger.info("remaining delay: {}  millisecond(s)", remainingDelay);

logger.info("result: {}", scheduledFuture.get());

shutdown(scheduledExecutorService);
```

Example of the _scheduleAtFixedRate_ method:

(If any execution of the task takes longer than its period, then subsequent executions may start late, but will not concurrently execute)

```
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

LocalTime start = LocalTime.now();

Runnable runnable = () -> logger.info("duration from start: {} second(s)", Duration.between(start, LocalTime.now()).getSeconds());
int initialDelay = 3;
int period = 1;
ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS);

Runnable canceller = () -> {
   scheduledFuture.cancel(true);
   scheduledExecutorService.shutdown();
};
scheduledExecutorService.schedule(canceller, 10, TimeUnit.SECONDS);
```

Example of the _scheduleAtFixedDelay_ method:

```
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

LocalTime start = LocalTime.now();

Runnable runnable = () -> {
   sleep(2);
   logger.info("duration from start: {} second(s)", Duration.between(start, LocalTime.now()).getSeconds());
};
int initialDelay = 3;
int delay = 1;
ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.SECONDS);

Runnable canceller = () -> {
   scheduledFuture.cancel(true);
   scheduledExecutorService.shutdown();
};
scheduledExecutorService.schedule(canceller, 10, TimeUnit.SECONDS);
```

### The CompletionService interface

The _CompletionService_ interface decouples the production of new tasks from the consumption of the results of completed tasks. This interface intended to mitigate absence in _Future_ completion callbacks, making it difficult to wait for the completion of many Futures simultaneously.

##### Examples

Example of the _ExecutorService.submit_ method:

(completed tasks are consumed by their _submission_ order)

```
ExecutorService executorService = Executors.newCachedThreadPool();

List<Future<String>> futures = new ArrayList<>();
futures.add(executorService.submit(() -> sleepAndGet(2, "Bravo")));
futures.add(executorService.submit(() -> sleepAndGet(1, "Alpha")));
futures.add(executorService.submit(() -> sleepAndGet(3, "Charlie")));

for (Future<String> future : futures) {
   logger.info("result: {}", future.get()); // Bravo, Alpha, Charlie
}

executorService.shutdown();
```

Example of the _ExecutorCompletionService.submit_ method:

(completed tasks are taken by their _competition_ order)

```
ExecutorService executorService = Executors.newCachedThreadPool();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

List<Future<String>> futures = new ArrayList<>();
futures.add(completionService.submit(() -> sleepAndGet(2, "Bravo")));
futures.add(completionService.submit(() -> sleepAndGet(1, "Alpha")));
futures.add(completionService.submit(() -> sleepAndGet(3, "Charlie")));

for (int i = 0; i < futures.size(); i++) {
   logger.info("result: {}", completionService.take().get()); // Alpha, Bravo, Charlie
}

executorService.shutdown();
```

Methods to submit tasks:

*   _Future&lt;V> submit​(Runnable task, V result)_
*   _Future&lt;V> submit​(Callable&lt;V> task)_

It’s important that Futures returned from these methods should be used only to cancel tasks. To consume tasks results should be used Futures returned from the methods, described below.

Methods to consume completed tasks:

*   _Future&lt;V> take​()_ - consumes the Future representing the next completed task, waiting if none are yet present.
*   _Future&lt;V> poll​()_ - consumes the Future representing the next completed task, or null if none are present
*   _Future&lt;V> poll​(long timeout, TimeUnit unit)_ - consumes the Future representing the next completed task, waiting if necessary up to the specified timeout, or null if none are present

It’s important that Futures returned from the methods are already completed,

## Thread pools

Thread pools are implementations that hide details of threads creation, utilization, and termination from the rest of the application.

In the _java.util.concurrent_ package there are three thread pools implementations:

*   _ThreadPoolExecutor_ - an implementation of the _ExecutorService_ interface
*   _ScheduledThreadPoolExecutor_ - an implementation of the _ScheduledExecutorService_ interface
*   _ForkJoinPool_ - a thread pool to execute tasks that can be recursively broken down into smaller subtasks

### The ThreadPoolExecutor class

The _ThreadPoolExecutor_ class is an implementation of the _ExecutorService_ interface which consists of the following parts:

*   a thread pool
*   a queue to transfer submitted tasks to threads
*   a thread factory to create new threads

The class has overloaded constructors with mandatory and optional parameters.

Mandatory constructors’ parameters:

*   _corePoolSize_ - the number of threads to keep in the pool, even if they are idle (unless _allowCoreThreadTimeOut_ is _true_)
*   _maximumPoolSize_ - the maximum number of threads to allow in the pool
*   _keepAliveTime_ - when the number of threads is greater than _corePoolSize_, this is the maximum time that excess idle threads will wait for new tasks before terminating
*   _unit_ - the time unit for the _keepAliveTime_ parameter
*   _workQueue_ - the queue to use for holding tasks before they are executed

Optional constructors’ parameters:

*   _threadFactory_ - the factory to use when the executor creates a new thread (if not specified, _Executors.defaultThreadFactory_ is used)
*   _handler_ - the handler to use when execution is blocked because the thread bounds and queue capacities are reached (if not specified, _ThreadPoolExecutor.AbortPolicy_ is used)

#### Threads creation

Threads can be created beforehand during thread pool creation and on-demand during task submission.

Algorithm of threads creation during thread pool creation:

1. the thread pool is created without idle threads
2. if the _prestartCoreThread​_ method is called, one core thread is started to wait for tasks
3. if the _prestartAllCoreThreads_ method is called, all core threads are started to wait for tasks

Algorithm of threads creation during tasks submission:

I) if the number of threads is less than _corePoolSize_, then a new thread is created

II) else, if the number of threads is equals or more than _corePoolSize_:

1. if the queue is not full, then the task is added to the queue
2. else, if the queue is full _and_ the number of threads is less than _maximumPoolSize_, then a new thread is created
3. else, the task is rejected

There are corner cases of threads creation:

*   if _corePoolSize_ is equal to _maximumPoolSize_, then it’s the fixed-size thread pool
*   if _corePoolSize_ is equal to _Integer.MAX_VALUE_, then the thread pool is essentially unbounded

#### Threads termination

If a thread has been idle more than _keepAliveTime_, then the thread can be terminated:

1. if _allowCoreThreadTimeOut_ is false (by default) _and_ the number of threads is more than _corePoolSize_ then the thread is terminated
2. if _allowCoreThreadTimeOut_ is true, then the thread is certainly terminated

There are corner cases of threads termination:

*   if _keepAliveTime_ is equal to _0_ then threads are terminated immediately after executing a task
*   if _keepAliveTime_ is equal to _Long.MAX_VALUE TimeUnit.NANOSECONDS_ then threads are effectively never terminated before the thread pool is shut down

#### Tasks queueing

Tasks are added to the queue according to the used _BlockingQueue_ implementation:

I) if a _direct handoff queue_ (e.g. _SynchronousQueue_) is used, which is essentially always full:

*   if the number of threads is less than _maximumPoolSize_, then a new thread is created
*   else, the task is rejected

II) if an _unbounded queue_ (e.g. _LinkedBlockingQueue_ without a predefined capacity) is used, which is never full:

*    the task is always added to the queue

III) if a _bounded queue_ (e.g. _ArrayBlockingQueue_ with a predefined capacity) is used:

*   if the queue is not full, then the task is added to the queue
*   else, if the queue is full _and_ the number of threads is less than _maximumPoolSize_, then a new thread is created
*   else, the task is rejected

It’s important that a _ThreadPoolExecutor_ instance never creates a new thread if a task can be added to the queue.

#### Tasks rejection

A task can be rejected in one of the cases:

*   the thread pool has been shut down
*   number of threads equals to _maximumPoolSize_
*   the queue is full (_remainingCapacity_ is _0_)

The rejected task can be handled with one of the predefined handler policies:

*   if the _ThreadPoolExecutor.AbortPolicy_ is used, the task is rejected and _RejectedExecutionException_ is thrown (by default)
*   if the _ThreadPoolExecutor.CallerRunsPolicy_ is used, the task is being run not in one of the thread pool threads, but in the caller’s thread
*   if the _ThreadPoolExecutor.DiscardPolicy_ is used, the task is silently dropped
*   if the _ThreadPoolExecutor.DiscardOldestPolicy_ is used, the oldest task is dropped (the task from the head of the queue)

It’s possible to use a custom handler that implements the _RejectedExecutionHandler_ interface.

#### The ThreadFactory interface

The _ThreadFactory_ interface has a single method:

*   _Thread newThread​(Runnable command)_

Thread factory is used to create threads for a thread pool with necessary priority, name, daemon status, _Thread.UncaughtExceptionHandler_, etc.

##### Examples

Examples of the _getCorePoolSize, getMaximumPoolSize_ methods:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>());

logger.info("core pool size: {}", threadPoolExecutor.getCorePoolSize()); // 2
logger.info("maximum pool size: {}", threadPoolExecutor.getMaximumPoolSize()); // 4
```

Examples of the _getActiveCount, getPoolSize, getLargestPoolSize_ methods

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
       1L, TimeUnit.SECONDS,
       new LinkedBlockingQueue<Runnable>(2));

for (int i = 0; i < 10; i++) {
   threadPoolExecutor.submit(() -> sleep(1));
}

for (int i = 0; i < 10; i++) {
   sleep(1);

   logThreadPoolSize(threadPoolExecutor);

   if (threadPoolExecutor.isTerminated()) {
       logThreadPoolSize(threadPoolExecutor);
       break;
   }
}

threadPoolExecutor.shutdown();
...
private static void logThreadPoolSize(ThreadPoolExecutor threadPoolExecutor) {
   logger.info("thread pool size (active/current/maximum): {}/{}/{}",
           threadPoolExecutor.getActiveCount(),
           threadPoolExecutor.getPoolSize(),
           threadPoolExecutor.getLargestPoolSize()
   );
}
```

Examples of the _getTaskCount, getCompletedTaskCount_ methods:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>());

for (int i = 0; i < 10; i++) {
   threadPoolExecutor.submit(() -> sleep(1));
}

for (int i = 0; i < 10; i++) {
   sleep(1);

   logTasksCount(threadPoolExecutor);

   if (threadPoolExecutor.isTerminated()) {
       logTasksCount(threadPoolExecutor);
       break;
   }
}

threadPoolExecutor.shutdown();
...
private static void logTasksCount(ThreadPoolExecutor threadPoolExecutor) {
   logger.info("tasks count (all/completed): {}/{}",
           threadPoolExecutor.getTaskCount(),
           threadPoolExecutor.getCompletedTaskCount());
}
```

Example of the _keepAliveTime_ field (0 seconds):

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
       0L, TimeUnit.SECONDS,
       new LinkedBlockingQueue<Runnable>(1));

for (int i = 0; i < 10; i++) {
   threadPoolExecutor.submit(() -> sleep(1));
}

threadPoolExecutor.shutdown();

for (int i = 0; i < 10; i++) {
   sleep(1);

   logThreadPoolSize(threadPoolExecutor);

   if (threadPoolExecutor.isTerminated()) {
       logThreadPoolSize(threadPoolExecutor);
       break;
   }
}
```

Example of the _keepAliveTime_ field (5 seconds):

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
       5L, TimeUnit.SECONDS,
       new LinkedBlockingQueue<Runnable>(1));

for (int i = 0; i < 10; i++) {
   threadPoolExecutor.submit(() -> sleep(1));
}

threadPoolExecutor.shutdown();

for (int i = 0; i < 10; i++) {
   sleep(1);

   logThreadPoolSize(threadPoolExecutor);

   if (threadPoolExecutor.isTerminated()) {
       logThreadPoolSize(threadPoolExecutor);
       break;
   }
}
```

Example of a direct handoff queue (_SynchronousQueue_):

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new SynchronousQueue<Runnable>());

System.out.println(threadPoolExecutor.getQueue().remainingCapacity());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of an unbounded queue (_LinkedBlockingQueue_):

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>());

System.out.println(threadPoolExecutor.getQueue().remainingCapacity());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of an bounded queue (_ArrayBlockingQueue_):

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new ArrayBlockingQueue<Runnable>(3));

System.out.println(threadPoolExecutor.getQueue().remainingCapacity());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of the _ThreadPoolExecutor.AbortPolicy_ rejecting policy:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>(1),
       new ThreadPoolExecutor.AbortPolicy());

try {
   threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
   threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
   threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie")); // java.util.concurrent.RejectedExecutionException
   threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
   threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));
} finally {
   threadPoolExecutor.shutdown();
}
```

Example of the _ThreadPoolExecutor.CallerRunsPolicy_ rejecting policy:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>(1),
       new ThreadPoolExecutor.CallerRunsPolicy());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of the _ThreadPoolExecutor.DiscardPolicy_ rejecting policy:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>(1),
       new ThreadPoolExecutor.DiscardPolicy());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of the _ThreadPoolExecutor.DiscardOldestPolicy_ rejecting policy:

```
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
       0L, TimeUnit.MILLISECONDS,
       new LinkedBlockingQueue<Runnable>(1),
       new ThreadPoolExecutor.DiscardOldestPolicy());

threadPoolExecutor.submit(() -> sleepAndGet(3, "Alpha"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Bravo"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Charlie"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Delta"));
threadPoolExecutor.submit(() -> sleepAndGet(3, "Echo"));

threadPoolExecutor.shutdown();
```

Example of the overridden _beforeExecute, afterExecute, terminated_ methods:

```
ThreadPoolExecutor threadPoolExecutor = new ExtendedThreadPoolExecutor();

threadPoolExecutor.submit(() -> sleep(1));

for (int i = 0; i < 10; i++) {
   sleep(1);

   logTasksCount(threadPoolExecutor);

   if (threadPoolExecutor.isTerminated()) {
       logTasksCount(threadPoolExecutor);
       break;
   }
}

threadPoolExecutor.shutdown();
...
private static class ExtendedThreadPoolExecutor extends ThreadPoolExecutor {

   private ExtendedThreadPoolExecutor() {
       super(1, 1,
               0L, TimeUnit.MILLISECONDS,
               new LinkedBlockingQueue<Runnable>());
   }

   @Override
   protected void beforeExecute(Thread t, Runnable r) {
       super.beforeExecute(t, r);
       logger.info("before task execution: thread {}, task {}", t, r);
   }

   @Override
   protected void afterExecute(Runnable r, Throwable t) {
       super.afterExecute(r, t);
       logger.info("after task execution: task {}, exception {}", r, t);
   }

   @Override
   protected void terminated() {
       super.terminated();
       logger.info("is terminated");
   }
}
```

### The ScheduledThreadPoolExecutor class

The _ScheduledThreadPoolExecutor_ class is an implementation of the _ScheduledExecutorService_ interface and a subclass of the _ThreadPoolExecutor_ class.

The class has overloaded constructors with mandatory and optional parameters.

Mandatory constructors’ parameters:

*   _corePoolSize_ - the number of threads to keep in the pool, even if they are idle (unless _allowCoreThreadTimeOut_ is _true_)

Optional constructors’ parameters:

*   _threadFactory_ - the factory to use when the executor creates a new thread (if not specified, _Executors.defaultThreadFactory_ is used)
*   _handler_ - the handler to use when execution is blocked because the thread bounds and queue capacities are reached (if not specified, _ThreadPoolExecutor.AbortPolicy_ is used)

##### Examples

Example of the _schedule_ method with _Runnable_ parameter:

```
ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

Runnable runnable = () -> logger.info("finished");

logger.info("started");
ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.schedule(runnable, 3000, TimeUnit.MILLISECONDS);

TimeUnit.MILLISECONDS.sleep(1000);

long remainingDelay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
logger.info("remaining delay: {} millisecond(s)", remainingDelay);

logger.info("result: {}", scheduledFuture.get());

shutdown(scheduledThreadPoolExecutor);
```

The _ScheduledThreadPoolExecutor_ class has methods of its superinterface _ScheduledExecutorService_ and its superclass _ThreadPoolExecutor_ that are already described previously.

### The ExecutorCompletionService class

The _ExecutorCompletionService_ class is an implementation of the _CompletionService_ interface that consists of an _Executor_ to execute submitted tasks and a _BlockingQueue_ to hold completed tasks. It’s a lightweight class that wraps submitted tasks into a special _Future_ that adds itself in the queue after tasks completion - the order in which tasks are added to the queue is the order in which they are completed.

The class has overloaded constructors with mandatory and optional parameters.

Mandatory constructors’ parameter:

*   _executor_ - an _Executor_ to execute submitted tasks

Optional constructors’ parameter:

*   _completionQueue_ - a _BlockingQueue_ to hold completed tasks (if not specified, an unbounded _LinkedBlockingQueue_ is used)

##### Examples

Example of the _submit_ method with _Runnable_ parameter and return value:

(a _Future_ returned from the _submit_ method is ignored, a _Future_ returned from the _take_ method is used to get task result)

```
ExecutorService executorService = Executors.newSingleThreadExecutor();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

completionService.submit(() -> {
   logger.info("started");
   sleep(1);
   logger.info("finished");
}, "Alpha");

logger.info("result: {}", completionService.take().get());

executorService.shutdown();
```

Example of the _submit_ method with _Callable_ parameter:

(a _Future_ returned from the _submit_ method is ignored, a _Future_ returned from the _take_ method is used to get task result)

```
ExecutorService executorService = Executors.newSingleThreadExecutor();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

completionService.submit(() -> sleepAndGet(1, "Alpha"));

logger.info("result: {}", completionService.take().get());

executorService.shutdown();
```

Example of the _take_ method:

(the _take_ method is blocked until the next task is completed, the _Future_ returned from the method is already completed)

```
ExecutorService executorService = Executors.newCachedThreadPool();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

List<Callable<String>> callables = Arrays.asList(
       () -> sleepAndGet(2, "Bravo"),
       () -> sleepAndGet(1, "Alpha"),
       () -> sleepAndGet(3, "Charlie")
);

for (Callable<String> callable : callables) {
   completionService.submit(callable);
}

for (int i = 0; i < callables.size(); i++) {
   logDuration("future " + (i + 1),
           () -> {
               logger.info("result: {}", completionService.take().get());
           }
   );
}

executorService.shutdown();
```

Example of the _poll_ method:

(the _poll_ method doesn’t block and return the next completed _Future_ if there is a completed task of _null_ if there are no completed tasks yet)

```
ExecutorService executorService = Executors.newCachedThreadPool();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

List<Callable<String>> callables = Arrays.asList(
       () -> sleepAndGet(2, "Bravo"),
       () -> sleepAndGet(1, "Alpha"),
       () -> sleepAndGet(3, "Charlie")
);

for (Callable<String> callable : callables) {
   completionService.submit(callable);
}

for (int i = 0; i < callables.size(); i++) {
   logDuration("future " + (i + 1),
           () -> {
               Future<String> future;
               do {
                   future = completionService.poll();
                   if (future != null) {
                       logger.info("result: {}", future.get());
                   } else {
                       logger.info("no result yet");
                       sleep(1);
                   }
               } while (future == null);
           }
   );
}

executorService.shutdown();
```

Example of the _poll_ method with timeout:

(the _poll_ method blocks for the given timeout and return the next completed _Future_ if there is a completed task of _null_ if there are no completed tasks yet)

```
ExecutorService executorService = Executors.newCachedThreadPool();
CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

List<Callable<String>> callables = Arrays.asList(
       () -> sleepAndGet(2, "Bravo"),
       () -> sleepAndGet(1, "Alpha"),
       () -> sleepAndGet(3, "Charlie")
);

for (Callable<String> callable : callables) {
   completionService.submit(callable);
}

for (int i = 0; i < callables.size(); i++) {
   logDuration("future " + (i + 1),
           () -> {
               Future<String> future;
               do {
                   future = completionService.poll(1, TimeUnit.SECONDS);
                   if (future != null) {
                       logger.info("result: {}", future.get());
                   } else {
                       logger.info("no result yet");
                   }
               } while (future == null);
           }
   );
}

executorService.shutdown();
```

### The ForkJoinPool class

The _ForkJoinPool_ class is a thread pool to execute tasks that can be recursively broken down into smaller subtasks. The _ForkJoinPool_ class differs from other kinds of thread pools by using the work-stealing algorithm: threads in the pool attempt to execute tasks submitted to the pool or spawned by other tasks. This enables efficient processing when many small tasks are submitted to the thread pool _or_ most tasks spawn other subtasks.

The _ForkJoinTask_ class is an abstract class for tasks that run within a _ForkJoinPool_. A _ForkJoinTask_ is an object that is much lighter weight than a normal thread. Large numbers of tasks may be executed by a small number of threads in a _ForkJoinPool_, at the price of some restrictions:

*   tasks should be used for calculating [pure functions](https://en.wikipedia.org/wiki/Pure_function)
*   tasks should access variables that are independent of those accessed by other tasks
*   tasks should avoid synchronized methods/blocks and should minimize other blocking synchronization apart from joining other tasks
*   tasks should not perform blocking I/O

There are abstract subclasses of the abstract _ForkJoinTask_ class that support a particular style of fork/join tasks:

*   _RecursiveAction_ - for tasks that do not return results
*   _RecursiveTask_ - for tasks returns results
*   _CountedCompleter_ - for tasks in which completed actions trigger other actions

The thread pool methods to submit _ForkJoinTask_:

*   _void execute(ForkJoinTask&lt;?> task)_ - arranges for asynchronous execution of the given task
*   _&lt;T> T invoke(ForkJoinTask&lt;T> task)_ - performs the given task, returning its result upon completion
*   _&lt;T> List&lt;Future&lt;T>> invokeAll(Collection&lt;? extends Callable&lt;T>> tasks)_ - executes the given tasks, returning a list of Futures holding their status and results when all complete
*   _&lt;T> ForkJoinTask&lt;T>	submit(ForkJoinTask&lt;T> task)_ - submits the given task for execution

The thread pool methods to monitor threads:

*   _int getParallelism()_ - returns the targeted parallelism level of this thread pool
*   _int getPoolSize()_ - returns the number of threads that have started but not yet terminated
*   _int getActiveThreadCoun()_ - returns an estimate of the number of threads that are currently stealing or executing tasks
*   _int getRunningThreadCount()_ - returns an estimate of the number of threads that are not blocked waiting to join tasks or for other managed synchronization
*   _boolean isQuiescent()_ - returns true if all threads are currently idle

The thread pool methods to monitor tasks:

*   _int getQueuedSubmissionCount()_ - returns an estimate of the number of tasks submitted to this thread pool that have not yet begun executing
*   _long getQueuedTaskCount()_ - returns an estimate of the total number of tasks currently held in queues by threads (but not including tasks submitted to the thread pool that have not begun executing)
*   _long getStealCount()_ - returns an estimate of the total number of tasks stolen from one thread's work queue by another
*   _boolean hasQueuedSubmissions()_ - returns _true_ if there are any tasks submitted to this thread pool that have not yet begun executing

##### Examples

Example of the _RecursiveTask_ class:

(the class is used to find the count of prime numbers from 1 to 100000 by the [trial division](https://en.wikipedia.org/wiki/Trial_division) algorithm - there are 9592 of them)

```
public class PrimeNumbersCountRecursiveTask extends RecursiveTask<Long> {

   private final int start;
   private final int end;
   private final int threshold;

   public PrimeNumbersCountRecursiveTask(int start, int end, int threshold) {
       this.start = start;
       this.end = end;
       this.threshold = threshold;
   }

   @Override
   protected Long compute() {
       if (((end + 1) - start) > threshold) {
           return ForkJoinTask.invokeAll(getSubTasks())
                   .stream()
                   .mapToLong(ForkJoinTask::join)
                   .sum();
       } else {
           return findPrimeNumbersCount();
       }
   }

   private List<PrimeNumbersCountRecursiveTask> getSubTasks() {
       List<PrimeNumbersCountRecursiveTask> tasks = new ArrayList<>();
       for (int i = 1; i <= end / threshold; i++) {
           int end = i * threshold;
           int start = (end - threshold) + 1;
           tasks.add(new PrimeNumbersCountRecursiveTask(start, end, threshold));
       }
       return tasks;
   }

   private long findPrimeNumbersCount() {
       long numbers = 0;
       for (int n = start; n <= end; n++) {
           if (isPrimeNumber(n)) {
               numbers++;
           }
       }
       return numbers;
   }

   private boolean isPrimeNumber(int n) {
       if (n == 2) {
           return true;
       }

       if (n == 1 || n % 2 == 0) {
           return false;
       }

       int divisors = 0;
       for (int i = 1; i <= n; i++) {
           if (n % i == 0) {
               divisors++;
           }
       }
       return divisors == 2;
   }
}
```

Example of the _execute_ method:

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

forkJoinPool.execute(task);

do {
   sleep(100);
} while (!task.isDone());

System.out.println("count: " + task.getRawResult()); // 9592

forkJoinPool.shutdown();
```

Example of the _invoke_ method:

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

Long count = forkJoinPool.invoke(task);
System.out.println("count: " + count); // 9592

forkJoinPool.shutdown();
```

Example of the _submit_ method:

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

ForkJoinTask<Long> forkJoinTask = forkJoinPool.submit(task);
Long count = forkJoinTask.get();
System.out.println("count: " + count); // 9592

forkJoinPool.shutdown();
```

Example of the _getParallelism, getPoolSize, getActiveThreadCount, getRunningThreadCount, isQuiescent_ methods:

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

forkJoinPool.execute(task);

do {
   logger.info("getParallelism={}, getPoolSize={}, getActiveThreadCount={}, getRunningThreadCount={}, isQuiescent={}",
           forkJoinPool.getParallelism(),
           forkJoinPool.getPoolSize(),
           forkJoinPool.getActiveThreadCount(),
           forkJoinPool.getRunningThreadCount(),
           forkJoinPool.isQuiescent()
   );
   Thread.sleep(100);
} while (!task.isDone());

System.out.println("count: " + task.getRawResult()); // 9592

forkJoinPool.shutdown();
```

Example of the _getQueuedSubmissionCount, getQueuedTaskCount, getStealCount, hasQueuedSubmissions_ methods:

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

forkJoinPool.execute(task);

do {
   logger.info("getQueuedSubmissionCount={}, getQueuedTaskCount={}, getStealCount={}, hasQueuedSubmissions={}",
           forkJoinPool.getQueuedSubmissionCount(),
           forkJoinPool.getQueuedTaskCount(),
           forkJoinPool.getStealCount(),
           forkJoinPool.hasQueuedSubmissions()
   );
   Thread.sleep(100);
} while (!task.isDone());

System.out.println("count: " + task.getRawResult()); // 9592

forkJoinPool.shutdown();
```

## The _Executors_ class

The Executors class has factory methods for the _ExecutorService, ScheduledExecutorService, ThreadFactory, Callable_ classes with commonly useful configuration settings. 

### Factory methods for _ExecutorService_ instances

Methods to create _ExecutorService_ instances:

*   _static ExecutorService newCachedThreadPool()_
*   _static ExecutorService newFixedThreadPool(int nThreads)_
*   _static ExecutorService newSingleThreadExecutor()_

There are overloaded versions of these methods that have an additional _ThreadFactory_ parameter.

Sources of the _newCachedThreadPool_ method:

```
public static ExecutorService newCachedThreadPool() {
   return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                 60L, TimeUnit.SECONDS,
                                 new SynchronousQueue<Runnable>());
}
```

This executor is effectively unbounded (_maximumPoolSize=Integer.MAX_VALUE_) and terminates all threads (_corePoolSize=0_ and _allowCoreThreadTimeOut=false_) that have been idle for 60 seconds. The _direct handoff queue_ (_SynchronousQueue_) doesn’t hold submitted tasks and each submitted task starts a new thread or uses an idle one. This executor can be useful for executing many short-lived tasks.

Sources of the _newFixedThreadPool_ method:

```
public static ExecutorService newFixedThreadPool(int nThreads) {
   return new ThreadPoolExecutor(nThreads, nThreads,
                                 0L, TimeUnit.MILLISECONDS,
                                 new LinkedBlockingQueue<Runnable>());
}
```

This executor can create no more than the fixed amount of threads. Once created, threads aren’t terminated until the thread pool is shut down. The _unbounded queue_ (_LinkedBlockingQueue_ without capacity) is used, so If additional tasks are submitted when all threads are busy, the tasks will wait in the queue until threads are available. Because neither extended threads (_corePoolSize_=_maximumPoolSize_) nor core threads (_allowCoreThreadTimeOut=false_) can’t be terminated by inactivity, _keepAliveTime_ doesn’t matter here.

Sources of the _newSingleThreadExecutor_ method:

```
public static ExecutorService newSingleThreadExecutor() {
   return new FinalizableDelegatedExecutorService
       (new ThreadPoolExecutor(1, 1,
                               0L, TimeUnit.MILLISECONDS,
                               new LinkedBlockingQueue<Runnable>()));
}
```

This thread pool can create no more than 1 thread. The thread pool and the queue behavior are identical to those described previously. Tasks are guaranteed to execute sequentially, and no more than one task will be active at any given time. The executor is decorated with a non-reconfigurable wrapper, so it cannot be reconfigured after creation. 

### Factory methods for _ScheduledExecutorService_ instances

Methods to create _ScheduledExecutorService_ instances

*   _static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)_
*   _static ScheduledExecutorService newSingleThreadScheduledExecutor()_

There are overloaded versions of these methods that have an additional _ThreadFactory_ parameter.

Sources of the _newScheduledThreadPool_ method:

```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ScheduledThreadPoolExecutor(corePoolSize);
}
```

Sources of the _newSingleThreadScheduledExecutor_ method:

```
public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
    return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1));
}
```

The executor is decorated with a non-reconfigurable wrapper, so it cannot be reconfigured after creation. 

### Factory methods for non-reconfigurable executors

Methods to create non-reconfigurable _ExecutorService, ScheduledExecutorService_ instances: 

*   _static ExecutorService unconfigurableExecutorService(ExecutorService executor)_
*   _static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor)_

##### Examples

Example of the _unconfigurableExecutorService_ method:

(the method returns an object that delegates all defined _ExecutorService_ methods to the given executor, but not any other methods that might otherwise be accessible using cast)

```
ExecutorService executorService = Executors.unconfigurableExecutorService(
       new ThreadPoolExecutor(1, 2,
               0L, TimeUnit.MILLISECONDS,
               new LinkedBlockingQueue<Runnable>())
);

ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService; // java.lang.ClassCastException
```

Example of the unconfigurableScheduledExecutorService method:

(the method returns an object that delegates all defined ScheduledExecutorService methods to the given executor, but not any other methods that might otherwise be accessible using cast)

```
ScheduledExecutorService scheduledExecutorService = Executors.unconfigurableScheduledExecutorService(
       new ScheduledThreadPoolExecutor(1)
);

ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) scheduledExecutorService; // java.lang.ClassCastException
```

### Factory methods for work-stealing executors

Methods to create work-stealing _ExecutorService_ instances:

*   _static ExecutorService newWorkStealingPool()_
*   _static ExecutorService newWorkStealingPool(int parallelism)_

##### Examples

```
ForkJoinTask<Long> task = new PrimeNumbersCountRecursiveTask(1, 100000, 10);
ForkJoinPool forkJoinPool = (ForkJoinPool) Executors.newWorkStealingPool();

Long count = forkJoinPool.invoke(task);
System.out.println("count: " + count); // 9592
```

### Factory methods for _ThreadFactory_ instances

Methods to create _ThreadFactory_ instances:

*   _static ThreadFactory defaultThreadFactory()_

Sources of the _defaultThreadFactory_ method:

```
public static ThreadFactory defaultThreadFactory() {
   return new DefaultThreadFactory();
}

private static class DefaultThreadFactory implements ThreadFactory {
   private static final AtomicInteger poolNumber = new AtomicInteger(1);
   private final ThreadGroup group;
   private final AtomicInteger threadNumber = new AtomicInteger(1);
   private final String namePrefix;

   DefaultThreadFactory() {
       SecurityManager s = System.getSecurityManager();
       group = (s != null) ? s.getThreadGroup() :
                             Thread.currentThread().getThreadGroup();
       namePrefix = "pool-" +
                     poolNumber.getAndIncrement() +
                    "-thread-";
   }

   public Thread newThread(Runnable r) {
       Thread t = new Thread(group, r,
                             namePrefix + threadNumber.getAndIncrement(),
                             0);
       if (t.isDaemon())
           t.setDaemon(false);
       if (t.getPriority() != Thread.NORM_PRIORITY)
           t.setPriority(Thread.NORM_PRIORITY);
       return t;
   }
}
```

This thread factory is implicitly used in the _ThreadPoolExecutor, ScheduledThreadPoolExecutor_ constructors.

### Factory methods for _Callable_ instances

Methods to create _Callable_ instances:

*   _static Callable&lt;Object> callable(Runnable task)_
*   _static &lt;T> Callable&lt;T> callable(Runnable task, T result)_

##### Examples

Example of the method callable with _Runnable_ parameter:

```
Runnable runnable = () -> System.out.println("running ...");
Callable<Object> callable = Executors.callable(runnable);
System.out.println("result: " + callable.call()); // null
```

Example of the method callable with _Runnable_ parameter and result:

```
Runnable runnable = () -> System.out.println("running ...");
Callable<Integer> callable = Executors.callable(runnable, 1);
System.out.println("result: " + callable.call()); // 1
```

## Conclusion

The optimum size of a thread pool depends on the nature of the tasks and the number of available processors. 

For CPU-bound tasks on an N-processor system, it’s possible to achieve maximum CPU utilization with a thread pool of N or N+1 threads. However, if more threads are used, performance degrades because of the additional thread context switching overhead. 

For I/O-bound tasks it’s reasonable to increase the pool size beyond the number of available processors because not all threads will be working at all times. On an N-processor system, it’s reasonable to have approximately N*(1+waiting_time/computing_time) threads to keep maximum processors utilization. If fewer threads are used, threads block on I/O, wasting idle CPU cores when there are tasks to be done.

Code examples are available in the [GitHub repository](https://github.com/aliakh/demo-java-concurrency-parallelism/tree/master/src/main/java/demo/part11_executors).	
