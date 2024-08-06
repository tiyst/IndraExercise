package st.tiy.command;

import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.tiy.domain.User;
import st.tiy.repository.UserRepository;

@ToString(exclude = "logger")
public class AddCommand implements Command {

	private final Logger logger = LogManager.getLogger(AddCommand.class);

	private final User user;

	public AddCommand(User user) {
		this.user = user;
	}

	@Override
	public void execute() {
		UserRepository.getInstance().addUser(user);
		logger.info("Added user: {}", user);
	}

}
