package st.tiy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import st.tiy.command.*;
import st.tiy.repository.UserRepository;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.State.TERMINATED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConsumerTest {

	static BlockingQueue<Command> queue;
	static Consumer consumer;

	@BeforeEach
	void setUp() {
		queue = new LinkedBlockingQueue<>();
		consumer = new Consumer(queue);
	}

	@Test
	void verifyCommandExecuteTest() throws InterruptedException {
		try (MockedStatic<UserRepository> mockedStatic = mockStatic(UserRepository.class)) {
			UserRepository repositoryMock = mock(UserRepository.class);
			when(repositoryMock.getAllUsers()).thenReturn(Collections.emptyList());
			mockedStatic.when(UserRepository::getInstance).thenReturn(repositoryMock);

			UserRepository instance = UserRepository.getInstance();
			assertEquals(Collections.emptyList(), instance.getAllUsers());

			queue.put(new PrintAllCommand());
			queue.put(new PoisonPillCommand());

			Thread consumerThread = new Thread(consumer);
			consumerThread.start();
			consumerThread.join();

			verify(repositoryMock, times(1)).getAllUsers();
		}
	}

	@Test
	void verifyConsumerExecutionTest() throws InterruptedException {
		PrintAllCommand printCommand = mockCommand(PrintAllCommand.class);
		AddCommand addCommand = mockCommand(AddCommand.class);
		DeleteAllCommand deleteAllCommand = mockCommand(DeleteAllCommand.class);
		PoisonPillCommand poisonPillCommand = mockCommand(PoisonPillCommand.class);

		queue.put(printCommand);
		queue.put(addCommand);
		queue.put(deleteAllCommand);
		queue.put(poisonPillCommand);

		assertThat(queue.size()).isEqualTo(4);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		consumerThread.join();
		assertThat(consumerThread.getState()).isEqualTo(TERMINATED);
		assertThat(queue.size()).isZero();

		verify(printCommand, times(1)).execute();
		verify(addCommand, times(1)).execute();
		verify(deleteAllCommand, times(1)).execute();
		verify(poisonPillCommand, times(1)).execute();
	}

	<T extends Command> T mockCommand(Class<T> type) {
		T command = mock(type);
		doNothing().when(command).execute();
		return command;
	}
}