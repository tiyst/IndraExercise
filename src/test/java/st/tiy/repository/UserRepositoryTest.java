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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	void addUserTest() {
		User user = new User();
		userRepository.addUser(user);

		verify(entityManagerMock).persist(user);
		verify(transactionMock).begin();
		verify(transactionMock).commit();
	}

	@Test
	void testDeleteAllUsers() {
		userRepository.deleteAllUsers();

		verify(transactionMock).begin();
		verify(entityManagerMock).createQuery("DELETE FROM User");
		verify(transactionMock).commit();
	}

	@Test
	void testGetAllUsers() {
		TypedQuery<User> query = mock(TypedQuery.class);
		when(entityManagerMock.createQuery("SELECT u FROM User u", User.class)).thenReturn(query);
		when(query.getResultList()).thenReturn(Collections.emptyList());

		List<User> users = userRepository.getAllUsers();

		verify(entityManagerMock).createQuery("SELECT u FROM User u", User.class);
		assertTrue(users.isEmpty());
	}

	@Test
	void testClose() {
		userRepository.close();

		verify(entityManagerMock).close();
		verify(emfMock).close();
	}

	@Test
	void testSingleton() {
		UserRepository instance1 = UserRepository.getInstance();
		UserRepository instance2 = UserRepository.getInstance();

		assertSame(instance1, instance2);
	}
}