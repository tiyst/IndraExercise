package st.tiy.repository;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import st.tiy.domain.User;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

	@Mock
	private EntityManagerFactory emfMock;

	@Mock
	private EntityManager entityManagerMock;

	@Mock
	private EntityTransaction transactionMock;

	@Mock
	private Query queryMock;

	@InjectMocks
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userRepository = UserRepository.getInstance();
		when(emfMock.createEntityManager()).thenReturn(entityManagerMock);
		when(entityManagerMock.getTransaction()).thenReturn(transactionMock);
		when(entityManagerMock.createQuery(anyString())).thenReturn(queryMock);

		userRepository = new UserRepository(emfMock, entityManagerMock);
	}

	@Test
	void addNewUserTest() {
		User user = new User();
		userRepository.addUser(user);

		verify(entityManagerMock).persist(user);
		verify(transactionMock).begin();
		verify(transactionMock).commit();
	}

	@Test
	void repositoryDeletesAllUsersTest() {
		userRepository.deleteAllUsers();

		verify(transactionMock).begin();
		verify(entityManagerMock).createQuery("DELETE FROM User");
		verify(transactionMock).commit();
	}

	@Test
	void repositoryQueriesForUsersTest() {
		TypedQuery<User> query = mock(TypedQuery.class);
		when(entityManagerMock.createQuery("SELECT u FROM User u", User.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(Collections.emptyList());

		List<User> users = userRepository.getAllUsers();

		verify(entityManagerMock).createQuery("SELECT u FROM User u", User.class);
		assertTrue(users.isEmpty());
	}

	@Test
	void newUserNotSavedWhenDuplicateIdTest() {
		userRepository = UserRepository.getInstance();
		User user1 = new User(1, "John", "Doe");
		User user2 = new User(1, "Jane", "Doe");

		userRepository.addUser(user1);
		assertThrows(EntityExistsException.class, () -> userRepository.addUser(user2));
	}

	@Test
	void repositoryClosesEntityManagerTest() {
		userRepository.close();

		verify(entityManagerMock).close();
		verify(emfMock).close();
	}

	@Test
	void singletonReturnsSameInstanceTest() {
		UserRepository instance1 = UserRepository.getInstance();
		UserRepository instance2 = UserRepository.getInstance();

		assertSame(instance1, instance2);
	}
}