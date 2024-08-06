## Indra exercise

Project built on Java 17 with Gradle as its build system, using Jakarta, Hibernate, H2, log4j, Lombok, Mockito, AssertJ and Junit.

### Running

Required demonstration is prepared in main method, just run the main application and let the magic flow.

### How it's done

Instead of Mutexes, CountDownLatches, or other concurrency techniques, I decided to use plain FIFO BlockingQueue, which waits for data to become available in case of Consumer, or empty space to become available in case of Producer.

Commands themselves are designed with Command pattern in mind, implementing an interface, and executing on top of UserRepository itself.

Ending the program requires Producer to be done producing, and Producer sending a _PoisonPillCommand_, which tells Consumer to stop.
