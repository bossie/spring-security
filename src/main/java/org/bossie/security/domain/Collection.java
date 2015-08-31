package org.bossie.security.domain;

public class Collection {
	private final Long id;
	private final String name;

	public Collection(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}
}
