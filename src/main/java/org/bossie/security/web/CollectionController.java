package org.bossie.security.web;

import java.util.Optional;
import java.util.Set;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.bossie.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
	public @ResponseBody Set<Collection> getOwnManagedCollections(@AuthenticationPrincipal UserDetails user) {
		return securityService.getManagedCollections(user.getUsername());
	}

	@RequestMapping(path="/collection/{collectionId}", method=DELETE)
	@PreAuthorize("hasRole('ADMIN') || hasPermission(#collectionId, 'org.bossie.security.domain.Collection', 'delete')")
	public @ResponseBody void deleteCollection(@PathVariable("collectionId") long collectionId) {
		securityService.deleteCollection(collectionId);
	}

	@RequestMapping(path="/collection/{collectionId}/item", method=POST)
	public ResponseEntity<Void> addItem(@PathVariable("collectionId") long collectionId, @RequestBody Object item) {
		securityService.addItem(collectionId, item);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(path="/collection/{collectionId}/item/{itemId}", method=PUT)
	@PreAuthorize("hasRole('ADMIN') || hasPermission(#itemId, 'org.bossie.security.domain.Item', 'write')")
	public @ResponseBody void updateItem(@PathVariable long itemId, @RequestBody Object item) {
		securityService.updateItem(itemId, item);
	}

	@RequestMapping(path="/user/{username}", method=GET)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		Optional<User> user = securityService.getUserByUsername(username);

		return user.isPresent()
				? new ResponseEntity<User>(user.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
