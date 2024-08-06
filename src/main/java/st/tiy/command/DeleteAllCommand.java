package st.tiy.command;

import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.repository.UserRepository;

@ToString(exclude = "logger")
public class DeleteAllCommand implements Command {

	private final Logger logger = LogManager.getLogger(DeleteAllCommand.class);

	@Override
	public void execute() {
		UserRepository.getInstance().deleteAllUsers();
		logger.info("All users deleted!");
	}

}
