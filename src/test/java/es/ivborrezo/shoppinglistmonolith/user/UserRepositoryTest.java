package es.ivborrezo.shoppinglistmonolith.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	private User elyoya;
	private User myrwn;
	Pageable pageable;

	@BeforeEach
	public void setupTestData() {
		// Arrange: Create and persist a couple of user using EntityManager
		LocalDate dateEloya = LocalDate.of(2000, 3, 18);
		LocalDate dateMyrwn = LocalDate.of(2001, 3, 18);

		elyoya = User.builder().userName("Elyoya").email("elyoya@gmail.com").firstName("El").lastName("Yoya")
				.password("asd").dateOfBirth(dateEloya).phoneNumber("928374650").build();

		myrwn = User.builder().userName("Myrwn").email("myrwn@gmail.com").firstName("Myr").lastName("Wn")
				.password("asd").dateOfBirth(dateMyrwn).build();

		pageable = PageRequest.of(0, 10);

		entityManager.persistAndFlush(elyoya);
		entityManager.persistAndFlush(myrwn);

	}

	@Test
	public void UserRepository_FindAll_FilterUserName() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<User> pageUsers = userRepository.findAll(UserSpecifications.likeUserName("yoya"), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(myrwn);
	}

	@Test
	public void UserRepository_FindAll_FilterEmail() {
		Page<User> pageUsers = userRepository.findAll(UserSpecifications.likeEmail("myr"), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(elyoya);
	}

	@Test
	public void UserRepository_FindAll_FilterFirstName() {
		Page<User> pageUsers = userRepository.findAll(UserSpecifications.likeFirstName("myr"), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(elyoya);
	}

	@Test
	public void UserRepository_FindAll_FilterLastName() {
		Page<User> pageUsers = userRepository.findAll(UserSpecifications.likeLastName("yoya"), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(myrwn);
	}

	@Test
	public void UserRepository_FindAll_FilterDateOfBirthGreaterThan() {
		Page<User> pageUsers = userRepository
				.findAll(UserSpecifications.byDateOfBirthGreaterThan(LocalDate.of(2000, 3, 18)), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(elyoya);
	}

	@Test
	public void UserRepository_FindAll_FilterDateOfBirthLessThan() {
		Page<User> pageUsers = userRepository
				.findAll(UserSpecifications.byDateOfBirthLessThan(LocalDate.of(2001, 3, 18)), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(myrwn);
	}

	@Test
	public void UserRepository_FindAll_FilterPhoneNumber() {
		Page<User> pageUsers = userRepository.findAll(UserSpecifications.likePhoneNumber("9"), pageable);

		assertThat(pageUsers.getTotalElements()).isGreaterThanOrEqualTo(1);
		assertThat(pageUsers.getContent()).doesNotContain(myrwn);
	}

	@Test
	public void UserRepository_FindById_ReturnUser() {
		// Act: Retrieve the user by ID from the repository
		Optional<User> optionalUser = userRepository.findById(elyoya.getUserId());
		// Assert: Verify that the user is found and matches the expected attributes
		assertThat(optionalUser).isPresent();
		assertThat(optionalUser).contains(elyoya);
	}

	@Test
	public void UserRepository_Save_ReturnSavedUser() {
		// Arrange
		LocalDate dateAlvaro = LocalDate.of(2000, 5, 6);

		User alvaro = User.builder().userName("Alvaro").email("alvaro@gmail.com").firstName("Ganchito")
				.lastName("Dalvarito").password("asd").dateOfBirth(dateAlvaro).phoneNumber("928374651").build();

		// Act
		User savedUser = userRepository.save(alvaro);

		// Assert
		assertThat(entityManager.find(User.class, savedUser.getUserId())).isEqualTo(alvaro);
		assertThat(savedUser).isEqualTo(alvaro);
	}

	@Test
	public void UserRepository_Delete_UserDeleted() {
		// Act: Delete the product from the repository
		userRepository.delete(elyoya);

		// Assert: Verify that the product is deleted
		assertThat(entityManager.find(User.class, elyoya.getUserId())).isNull();
	}
}
