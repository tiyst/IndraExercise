package st.tiy.command;

import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.repository.UserRepository;

@ToString(exclude = "logger")
public class PrintAllCommand implements Command {

	private final Logger logger = LogManager.getLogger(PrintAllCommand.class);

	@Override
	public void execute() {
		logger.info("Printing all users:");
		UserRepository.getInstance().getAllUsers().forEach(logger::info);
	}

}
