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
import org.springframework.stereotype.Repository;

@Repository
public class SecurityDao {
	private final Set<User> users = new HashSet<>();

	{
		Group stubru = new Group("Studio Brussel");
		Group canvas = new Group("Canvas");

		User siska = new User(1L, "siska", "siska", EnumSet.of(ROLE_USER));
		User chris = new User(2L, "chris", "chris", EnumSet.of(ROLE_USER));
		User bert = new User(3L, "bert", "bert", EnumSet.of(ROLE_USER));
		User adriaan = new User(4L, "adriaan", "adriaan", EnumSet.of(ROLE_ADMIN));

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

	public Optional<User> getUserById(long userId) {
		System.out.println("Dao::getUserById");

		return users.stream()
				.filter(user -> user.getId() == userId)
				.findFirst();
	}

	public Optional<User> getUserByUsername(String username) {
		System.out.println("Dao::getUserByUsername");

		return users.stream()
				.filter(user -> username.equals(user.getUsername()))
				.findFirst();
	}
}
