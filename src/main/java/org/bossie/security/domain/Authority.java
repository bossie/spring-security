package org.bossie.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface Authority {
	GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
	GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
}
