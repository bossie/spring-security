package org.bossie.security.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group {
	private final String name;
	private final Set<User> members = new HashSet<>();
	private final Set<Collection> collections = new HashSet<>();

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set<User> getMembers() {
		return Collections.unmodifiableSet(members);
	}

	public boolean addMember(User user) {
		boolean added = members.add(user);

		if (added) {
			user.addTo(this);
		}

		return added;
	}

	public Set<Collection> getCollections() {
		return Collections.unmodifiableSet(collections);
	}

	public void addCollection(Collection collection) {
		collections.add(collection);
	}

	@Override
	public String toString() {
		return name;
	}
}
