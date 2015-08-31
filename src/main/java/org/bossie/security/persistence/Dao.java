package org.bossie.security.persistence;

import static org.bossie.security.domain.Authority.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.Group;
import org.bossie.security.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {
	private final Set<User> users = new HashSet<>();

	{
		Group stubru = new Group("Studio Brussel");
		Group canvas = new Group("Canvas");

		User siska = new User(1L, "siska", "siska", EnumSet.of(USER));
		User chris = new User(2L, "chris", "chris", EnumSet.of(USER));
		User bert = new User(3L, "bert", "bert", EnumSet.of(USER));
		User adriaan = new User(4L, "adriaan", "adriaan", EnumSet.of(ADMIN));

		stubru.addMember(siska);
		stubru.addMember(bert);
		canvas.addMember(chris);
		canvas.addMember(bert);

		stubru.addCollection(new Collection(1L, "Music For Life"));
		canvas.addCollection(new Collection(2L, "Cobra's Classic Battle"));

		users.addAll(Arrays.asList(siska, chris, bert, adriaan));
	}

	public Set<User> getUsers() {
		System.out.println("Dao::getUsers");

		return Collections.unmodifiableSet(users);
	}

	public User getUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Dao::getUserByUsername");

		return users.stream()
				.filter(user -> username.equals(user.getUsername()))
				.findFirst()
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

	public Set<Collection> getUsersCollections(long userId) {
		return users.stream()
				.filter(user -> user.getId().equals(userId))
				.findFirst()
				.flatMap(user -> Optional.of(user.getCollections()))
				.orElseThrow(() -> new IllegalArgumentException(String.valueOf(userId)));
	}

	public void deleteCollection(long collectionId) {
		System.out.println("Dao::deleteCollection");
		// TODO: delete collection
	}

	public void addItem(long collectionId, Object item) {
		System.out.println("Dao::addItem");
		// TODO: add item to collection
	}
}
