package org.bossie.security.web;

import java.util.Set;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.bossie.security.persistence.Dao;
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
	private Dao dao;

	@RequestMapping(path="/collection", method=GET)
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public @ResponseBody Set<Collection> getOwnCollections(@AuthenticationPrincipal User user) {
		return user.getCollections();
	}

	@RequestMapping(path="/user/{userId}/collection", method=GET)
	@PreAuthorize("hasAuthority('ADMIN') || (hasAuthority('USER') && principal.id == #userId)")
	public @ResponseBody Set<Collection> getUsersCollections(@PathVariable("userId") long userId) {
		return dao.getUsersCollections(userId);
	}

	@RequestMapping(path="/collection/{collectionId}", method=DELETE)
	@PreAuthorize("hasAuthority('ADMIN') || hasPermission(#collectionId, 'org.bossie.security.domain.Collection', 'delete')")
	public @ResponseBody void deleteCollection(@PathVariable("collectionId") long collectionId) {
		dao.deleteCollection(collectionId);
	}

	@RequestMapping(path="/collection/{collectionId}", method=POST)
	public @ResponseBody void addItem(@PathVariable("collectionId") long collectionId, @RequestBody Object item) {
		dao.addItem(collectionId, item);
	}
}
