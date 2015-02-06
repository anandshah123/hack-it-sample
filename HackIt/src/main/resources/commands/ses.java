import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;

import com.rspl.hackit.controller.IndexController;

public class ses extends BaseCommand {
	@Usage("prints sessions map")
	@Command
	public Object main(
			@Usage("kills the session and logs user out") @Option(names = {
					"k", "kill" }) String kill,
			@Usage("forces reload page to all users") @Option(names = { "r",
					"reload" }) String reload) {
		if (kill != null) {
			int teamId = NumberUtils.toInt(kill);
			List<String> keysToRemove = new ArrayList<>();
			for (Entry<String, HttpSession> entry : IndexController.activeSessions
					.entrySet()) {
				if (StringUtils.startsWith(entry.getKey(), "team-" + teamId)) {
					entry.getValue().invalidate();
					keysToRemove.add(entry.getKey());
				}
			}
			if(!keysToRemove.isEmpty()) {
				for (String key : keysToRemove) {
					IndexController.activeSessions.remove(key);
				}
			}
		}
		if (reload != null) {
			for (Entry<String, HttpSession> entry : IndexController.activeSessions
					.entrySet()) {
				entry.getValue().setAttribute("reloadRequired", true);

			}
		}
		return IndexController.activeSessions;
	}
}