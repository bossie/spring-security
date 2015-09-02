package org.bossie.security.web;

import java.util.Set;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.bossie.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class CollectionController {

	@Autowired
	private SecurityService securityService;

	@RequestMapping(path="/collection", method=GET)
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public @ResponseBody Set<Collection> getOwnManagedCollections(@AuthenticationPrincipal User user) {
		return securityService.getManagedCollections(user);
	}

	@RequestMapping(path="/user/{userId}/collection", method=GET)
	@PreAuthorize("hasRole('ADMIN') || (hasRole('USER') && principal.id == #userId)")
	public @ResponseBody Set<Collection> getUsersManagedCollections(@PathVariable("userId") long userId) {
		return securityService.getUsersManagedCollections(userId);
	}

	@RequestMapping(path="/collection/{collectionId}", method=DELETE)
	@PreAuthorize("hasRole('ADMIN') || hasPermission(#collectionId, 'org.bossie.security.domain.Collection', 'delete')")
	public @ResponseBody void deleteCollection(@PathVariable("collectionId") long collectionId) {
		securityService.deleteCollection(collectionId);
	}

	@RequestMapping(path="/collection/{collectionId}", method=POST)
	public @ResponseBody void addItem(@PathVariable("collectionId") long collectionId, @RequestBody Object item) {
		securityService.addItem(collectionId, item);
	}
}
