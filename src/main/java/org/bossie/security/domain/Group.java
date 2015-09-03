package org.bossie.security.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group {
	private Long id;
	private String name;
	private Set<User> members = new HashSet<>();
	private Set<Collection> collections = new HashSet<>();

	public Group(String name) {
		this.name = name;
	}

	protected Group() {}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	public Set<User> getMembers() {
		return Collections.unmodifiableSet(members);
	}

	public void addMember(User user) {
		members.add(user);
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
