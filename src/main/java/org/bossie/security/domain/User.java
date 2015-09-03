package org.bossie.security.domain;

public class User {
	private Long id;

	private String username;

	public User(String username) {
		this.username = username;
	}

	protected User() {}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	@SuppressWarnings("unused")
	private void setUsername(String username) {
		this.username = username;
	}
}
