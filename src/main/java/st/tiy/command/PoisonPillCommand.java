package st.tiy.command;

import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ToString(exclude = "logger")
public class PoisonPillCommand implements Command {

	private final Logger logger = LogManager.getLogger(PoisonPillCommand.class);

	@Override
	public void execute() {
		logger.info("PoisonPill received, terminating consumer.");
	}
}
