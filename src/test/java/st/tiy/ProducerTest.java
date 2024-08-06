package st.tiy;

import org.junit.jupiter.api.Test;
import st.tiy.command.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProducerTest {

	static BlockingQueue<Command> queue;
	static Producer producer;

	@Test
	void queueFillingTest() throws InterruptedException {
		queue = spy(new LinkedBlockingQueue<>());
		producer = spy(new Producer(queue));

		assertThat(queue.size()).isZero();
		Thread producerThread = new Thread(producer);
		producerThread.start();

		producerThread.join();
		assertThat(queue.size()).isEqualTo(6);

		verify(producer, times(1)).run();
		verify(queue, times(2)).put(any(AddCommand.class));
		verify(queue, times(2)).put(any(PrintAllCommand.class));
		verify(queue, times(1)).put(any(DeleteAllCommand.class));
		verify(queue, times(1)).put(any(PoisonPillCommand.class));
	}

	@Test
	void producerFailsOnInterupptedException() throws InterruptedException {
		queue = mock(LinkedBlockingQueue.class);
		producer = new Producer(queue);

		doThrow(new InterruptedException()).when(queue).put(any(Command.class));

		producer.run();

		assertTrue(Thread.currentThread().isInterrupted());
	}
}