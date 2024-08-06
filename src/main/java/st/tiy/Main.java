package st.tiy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.command.Command;
import st.tiy.repository.UserRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);

	private static BlockingQueue<Command> queue;
	private static Thread producerThread;
	private static Thread consumerThread;

	public static void main(String[] args) {
		queue = new LinkedBlockingQueue<>();

		producerThread = new Thread(new Producer(queue));
		consumerThread = new Thread(new Consumer(queue));

		logger.info("Starting consumer thread");
		consumerThread.start();
		logger.info("Starting producer thread");
		producerThread.start();

		try {
			producerThread.join();
			consumerThread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			UserRepository.getInstance().close();
		}
	}

}