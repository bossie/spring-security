package org.bossie.security.config;

import java.io.Serializable;
import java.util.Map;

import org.bossie.security.domain.Collection;

import static org.bossie.security.domain.Authority.*;

import org.bossie.security.service.DomainService;
import org.bossie.security.service.SecurityService;
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

/**
 * Permission to access collection is derived from the user<->group<->collection situation as it exists in the domain DB,
 * so this is like a 'passive' permission evaluator.
 * Another option is to actively assign ACL's with permissions to specific domain objects for certain users/authorities (and
 * also inherit permissions from parent ACL's, for example). Check the Spring ACL module for an appropriate ACL permission
 * evaluator.
 */
@Component
class AccessToCollectionPermittedIfMemberOfOwningGroupPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private DomainService domainService;

	@Override
	public boolean hasPermission(Authentication authentication, Object target, Object permission) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		if (Collection.class.getName().equals(targetType)) {
			long collectionId = (long) targetId;

			return hasPermission(authentication, collectionId);
		} else if ("org.bossie.security.domain.Item".equals(targetType)) {
			long itemId = (long) targetId;
			Map<String, ?> item = domainService.getItem(itemId);
			long collectionId = (Long) item.get("collection_id");

			return hasPermission(authentication, collectionId);
		}

		return false;
	}

	private boolean hasPermission(Authentication authentication, long collectionId) {
		return authentication.getAuthorities().contains(ROLE_USER) &&
				securityService.isMemberOfGroupOwningCollection(authentication.getName(), collectionId);
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
		return new AccessToCollectionPermittedIfMemberOfOwningGroupPermissionEvaluator();
	}
}
