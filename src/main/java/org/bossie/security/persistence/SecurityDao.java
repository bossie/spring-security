package org.bossie.security.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.bossie.security.domain.Collection;
import org.bossie.security.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Optional<User> getUserById(long userId) {
		return fromSession(session -> {
			User user = (User) session.get(User.class, userId);
			return Optional.ofNullable(user);
		});
	}

	public Optional<User> getUserByUsername(String username) {
		return fromSession(session -> {
			User user = (User) session.createQuery("from User user where user.username = :username")
				.setString("username", username)
				.uniqueResult();

			return Optional.ofNullable(user);
		});
	}

	public Set<Collection> getManagedCollections(String username) {
		return fromSession(session -> {
			@SuppressWarnings("unchecked")
			List<Collection> collections = session.getNamedQuery("getManagedCollections")
				.setString("username", username)
				.list();

			return new HashSet<>(collections);
		});
	}

	public boolean isMemberOfGroupOwningCollection(String username, long collectionId) {
		return fromSession(session -> {
			long count = (long) session.getNamedQuery("isMemberOfGroupOwningCollection")
				.setString("username", username)
				.setLong("collectionId", collectionId)
				.uniqueResult();

			return count > 0;
		});
	}

	private <R> R fromSession(Function<Session, R> function) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			R result = function.apply(session);
			tx.commit();

			return result;
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
}
