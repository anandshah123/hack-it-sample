import org.apache.commons.lang3.math.NumberUtils;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;

import com.rspl.hackit.controller.IndexController;
import com.rspl.hackit.dto.Message;

public class bal extends BaseCommand {
	@Usage("prints balance map")
	@Command
	public Object main() {
		return IndexController.userToBalance;
	}
}