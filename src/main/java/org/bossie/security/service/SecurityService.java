package org.bossie.security.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.bossie.security.persistence.SecurityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	@Autowired
	private SecurityDao securityDao;

	public Optional<User> getUserByUsername(String username) {
		return securityDao.getUserByUsername(username);
	}

	public Set<Collection> getManagedCollections(User user) {
		Set<Collection> results = new HashSet<>();

		user.getGroups().forEach(group -> results.addAll(group.getCollections()));

		return results;
	}

	public Set<Collection> getUsersManagedCollections(long userId) {
		return securityDao.getUserById(userId)
				.map(user -> getManagedCollections(user))
				.orElse(Collections.emptySet());
	}

	public boolean isMemberOfGroupOwningCollection(String username, long collectionId) {
		return getUserByUsername(username)
				.map(user -> getManagedCollections(user).stream()
						.anyMatch(collection -> collection.getId() == collectionId))
				.orElse(false);
	}

	public void deleteCollection(long collectionId) {
		System.out.println("deleteCollection");
		// TODO: delete collection
	}

	public void addItem(long collectionId, Object item) {
		System.out.println("addItem");
		// TODO: add item to collection
	}
}
