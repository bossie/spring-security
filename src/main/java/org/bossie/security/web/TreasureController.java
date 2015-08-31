package org.bossie.security.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/treasure")
public class TreasureController {

	@RequestMapping(method=GET)
	@Secured("ROLE_USER")
	public @ResponseBody String getTreasureChest() {
		return "The chest is locked.";
	}

	@RequestMapping(method=GET, path="/open")
	@Secured("ROLE_LOCKSMITH")
	@PreAuthorize("hasPermission(null, null)")
	public @ResponseBody String openTreasureChest() {
		return "Ooh! Shiny!";
	}
}
