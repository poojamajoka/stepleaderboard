package com.stepthrone.profile.repository;

import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import com.stepthrone.userprofile.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUserWhenExists() {
        // Given
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");

        User user = new User();
        user.setUsername("john.doe");
        user.setUserProfile(profile);
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByUsername("john.doe");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john.doe");
        assertThat(found.get().getUserProfile().getFirstName()).isEqualTo("John");
    }

    @Test
    void findByUsername_ShouldReturnEmptyWhenNotExists() {
        // When
        Optional<User> found = userRepository.findByUsername("nonexistent.user");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistUserWithProfile() {
        // Given
        UserProfile profile = new UserProfile();
        profile.setFirstName("Jane");
        profile.setLastName("Smith");

        User user = new User();
        user.setUsername("jane.smith");
        user.setUserProfile(profile);

        // When
        User saved = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // Then
        User retrieved = entityManager.find(User.class, saved.getId());
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getUsername()).isEqualTo("jane.smith");
        assertThat(retrieved.getUserProfile().getLastName()).isEqualTo("Smith");
    }

    @Test
    void update_ShouldModifyExistingUser() {
        // Given
        UserProfile profile = new UserProfile();
        profile.setFirstName("Original");

        User user = new User();
        user.setUsername("original.user");
        user.setUserProfile(profile);
        entityManager.persist(user);
        entityManager.flush();

        // When
        user.getUserProfile().setFirstName("Updated");
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // Then
        User updated = entityManager.find(User.class, user.getId());
        assertThat(updated.getUserProfile().getFirstName()).isEqualTo("Updated");
    }

    @Test
    void delete_ShouldRemoveUser() {
        // Given
        User user = new User();
        user.setUsername("to.delete");
        entityManager.persist(user);
        entityManager.flush();

        // When
        userRepository.delete(user);
        entityManager.flush();

        // Then
        assertThat(entityManager.find(User.class, user.getId())).isNull();
    }
}