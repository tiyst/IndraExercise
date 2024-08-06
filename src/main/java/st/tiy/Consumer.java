package st.tiy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.command.Command;
import st.tiy.command.PoisonPillCommand;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

	private final Logger logger = LogManager.getLogger(Consumer.class);

	private final BlockingQueue<Command> queue;

	public Consumer(BlockingQueue<Command> queue) {
		this.queue = queue;
	}

	@Override
	public void run()  {
		boolean isRunning = true;

		try {
			while (isRunning) {
				Command command = queue.take();
				logger.info("Executing command {}", command);
				command.execute();

				if (command instanceof PoisonPillCommand) {
					isRunning  = false;
				}
			}
		} catch (InterruptedException e) {
			logger.error("Consumer has been interrupted: ", e);
			Thread.currentThread().interrupt();
		}

		logger.info("Consumer done!");
	}

}
