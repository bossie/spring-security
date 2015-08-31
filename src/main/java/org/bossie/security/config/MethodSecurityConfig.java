package org.bossie.security.config;

import java.io.Serializable;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.bossie.security.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
class SomePermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private Dao dao;

	@Override
	public boolean hasPermission(Authentication authentication, Object target, Object permission) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		if (Collection.class.getName().equals(targetType)) {
			long targetCollectionId = (long) targetId;
			User user = dao.getUserByUsername(authentication.getName());

			return user.getCollections().stream()
					.anyMatch(collection -> collection.getId() == targetCollectionId);
		}

		return false;
	}
}

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(permissionEvaluator());
		return expressionHandler;
	}

	@Bean
	PermissionEvaluator permissionEvaluator() {
		return new SomePermissionEvaluator();
	}
}
