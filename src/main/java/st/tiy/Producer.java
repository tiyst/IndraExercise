package st.tiy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.command.*;
import st.tiy.domain.User;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

	private final Logger logger = LogManager.getLogger(Producer.class);

	private final BlockingQueue<Command> queue;

	public Producer(BlockingQueue<Command> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			this.sendCommand(new AddCommand(new User(1, "a1", "Robert")));
			this.sendCommand(new AddCommand(new User(2, "a2", "Martin")));
			this.sendCommand(new PrintAllCommand());
			this.sendCommand(new DeleteAllCommand());
			this.sendCommand(new PrintAllCommand());
			this.sendCommand(new PoisonPillCommand());
		} catch (InterruptedException e) {
			logger.error("Producer has experienced an error", e);
			Thread.currentThread().interrupt();
		}

		logger.info("Producer done!");
	}

	private void sendCommand(Command command) throws InterruptedException {
		logger.info("Sending command: {}", command);
		this.queue.put(command);
	}

}
