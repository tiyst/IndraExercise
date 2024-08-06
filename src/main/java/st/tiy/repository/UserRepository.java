package st.tiy.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import st.tiy.domain.User;

import java.util.List;

public class UserRepository {

	private static UserRepository instance;
	private final EntityManagerFactory emf;
	private final EntityManager entityManager;

	private UserRepository() {
		this.emf = Persistence.createEntityManagerFactory("SUser");
		this.entityManager = emf.createEntityManager();
	}

	UserRepository(EntityManagerFactory emf, EntityManager entityManager) {
		this.emf = emf;
		this.entityManager = entityManager;
	}

	public static UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}

	public synchronized void addUser(User user) {
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
	}

	public synchronized void deleteAllUsers() {
		entityManager.getTransaction().begin();
		entityManager.createQuery("DELETE FROM User").executeUpdate();
		entityManager.getTransaction().commit();
	}

	public synchronized List<User> getAllUsers() {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}

	public void close() {
		this.entityManager.close();
		this.emf.close();
	}
}
