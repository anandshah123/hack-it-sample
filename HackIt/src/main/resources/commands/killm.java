import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;

import com.rspl.hackit.controller.IndexController;
import com.rspl.hackit.dto.Message;

public class killm extends BaseCommand {
	@Usage("removes given message id from mq")
	@Command
	public Object main(
			@Usage("the message id") @Option(names = { "i", "id" }) String id,
			@Usage("clears all messages in mq") @Option(names = { "a", "all" }) String all) {
		String out = "Operation failed";
		if (id != null) {
			Message toRemove = null;
			for (Message message : IndexController.mq) {
				if (message.getId() == NumberUtils.toInt(id)) {
					toRemove = message;
				}
			}
			if (toRemove != null)
				out = "Message with id - " + id + " was "
						+ (IndexController.mq.remove(toRemove) ? "" : "Not ")
						+ "Removed.";
		}
		if (StringUtils.isNotBlank(all)) {
			IndexController.mq.clear();
			out = "All messages cleared";
		}
		return out;
	}
}