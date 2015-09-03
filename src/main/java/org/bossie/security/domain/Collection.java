package org.bossie.security.domain;

public class Collection {
	private Long id;
	private String name;

	public Collection(String name) {
		this.name = name;
	}

	protected Collection() {}

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

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
