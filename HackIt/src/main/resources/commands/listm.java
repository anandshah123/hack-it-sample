import org.apache.commons.lang3.math.NumberUtils;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;

import com.rspl.hackit.controller.IndexController;
import com.rspl.hackit.dto.Message;

public class listm extends BaseCommand {
	@Usage("lists all 20 messages from mq")
	@Command
	public Object main(
			@Usage("lists the message id") @Option(names = { "i", "id" }) String id) {
		Object toReturn = null;
		if (id != null) {
			for (Message message : IndexController.mq) {
				if (message.getId() == NumberUtils.toInt(id)) {
					toReturn = message;
				}
			}
		} else {
			toReturn = IndexController.mq;
		}
		return toReturn;
	}
}