package org.bossie.security.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class User extends org.springframework.security.core.userdetails.User {

	private final Long id;
	private final Set<Group> groups = new HashSet<>();

	public User(Long id, String username, String password, java.util.Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public boolean addTo(Group group) {
		boolean added = groups.add(group);

		if (added) {
			group.addMember(this);
		}

		return added;
	}

	public Set<Collection> getCollections() {
		Set<Collection> results = new HashSet<>();

		groups.forEach(group -> results.addAll(group.getCollections()));

		return results;
	}

	@Override
	public void eraseCredentials() {
		// see caveat @ Javadoc of org.springframework.security.core.userdetails.User
	}
}
