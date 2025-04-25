package com.stepthrone.stepcount.repository;

import com.stepthrone.stepcount.model.DailyStepData;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DailyStepRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DailyStepRepository dailyStepRepository;

    private DailyStepData createDailyStepData(User user, Integer steps, LocalDate date) {
        DailyStepData data = new DailyStepData();
        data.setUser(user);
        data.setSteps(steps);
        data.setDate(date);
        return data;
    }

    @Test
    void findByUserAndDate_ShouldReturnDailyStepDataWhenExists() {
        // Given
        User user = new User();
        user.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user.setUserProfile(userProfile);
        entityManager.persist(user);

        LocalDate date = LocalDate.now();
        DailyStepData expected = createDailyStepData(user, 10000, date);
        entityManager.persist(expected);
        entityManager.flush();

        // When
        Optional<DailyStepData> actual = dailyStepRepository.findByUserAndDate(user, date);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().getSteps()).isEqualTo(10000);
        assertThat(actual.get().getUser()).isEqualTo(user);
        assertThat(actual.get().getDate()).isEqualTo(date);
    }

    @Test
    void findByUserAndDate_ShouldReturnEmptyWhenNotFound() {
        // Given
        User user = new User();
        user.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user.setUserProfile(userProfile);
        entityManager.persist(user);
        entityManager.persist(user);
        LocalDate date = LocalDate.now();

        // When
        Optional<DailyStepData> actual = dailyStepRepository.findByUserAndDate(user, date);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void findAllByDateOrderByStepsDesc_ShouldReturnOrderedResults() {
        // Given

        User user1 = new User();
        user1.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user1.setUserProfile(userProfile);


        User user2 = new User();
        user2.setUsername("john.doe");
        UserProfile userProfile2=new UserProfile();
        userProfile2.setFirstName("John");
        userProfile2.setLastName("Doe");
        user2.setUserProfile(userProfile2);


        entityManager.persist(user1);
        entityManager.persist(user2);

        LocalDate date = LocalDate.now();
        DailyStepData data1 = createDailyStepData(user1, 5000, date);
        DailyStepData data2 = createDailyStepData(user2, 8000, date);
        entityManager.persist(data1);
        entityManager.persist(data2);
        entityManager.flush();

        // When
        List<DailyStepData> result = dailyStepRepository.findAllByDateOrderByStepsDesc(date);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSteps()).isEqualTo(8000);
        assertThat(result.get(1).getSteps()).isEqualTo(5000);
    }

    @Test
    void findUserTotalStepsRanking_ShouldReturnPaginatedResults() {
        // Given

        User user1 = new User();
        user1.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user1.setUserProfile(userProfile);


        User user2 = new User();
        user2.setUsername("john.doe");
        UserProfile userProfile2=new UserProfile();
        userProfile2.setFirstName("John");
        userProfile2.setLastName("Doe");
        user2.setUserProfile(userProfile2);

        entityManager.persist(user1);
        entityManager.persist(user2);

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // User1: 5000 + 3000 = 8000 total steps
        entityManager.persist(createDailyStepData(user1, 5000, today));
        entityManager.persist(createDailyStepData(user1, 3000, yesterday));

        // User2: 6000 + 1000 = 7000 total steps
        entityManager.persist(createDailyStepData(user2, 6000, today));
        entityManager.persist(createDailyStepData(user2, 1000, yesterday));

        entityManager.flush();

        Page<Object[]> page = dailyStepRepository.findUserTotalStepsRanking(
                PageRequest.of(0, 10, Sort.unsorted())
        );

        // Then

        Object[] firstRank = page.getContent().get(0);
        assertThat(firstRank[0]).isEqualTo(user1.getId()); // user_id
        assertThat(firstRank[1]).isEqualTo("John"); // first_name
        assertThat(firstRank[2]).isEqualTo("Doe"); // last_name
        assertThat(((Number) firstRank[3]).intValue()).isEqualTo(8000); // Convert to int for comparison

        Object[] secondRank = page.getContent().get(1);
        assertThat(((Number) secondRank[3]).intValue()).isEqualTo(7000);
    }

    @Test
    void findAllByUserId_ShouldReturnPaginatedResults() {
        // Given

        User user = new User();
        user.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user.setUserProfile(userProfile);

        entityManager.persist(user);

        LocalDate date1 = LocalDate.now();
        LocalDate date2 = date1.minusDays(1);

        DailyStepData data1 = createDailyStepData(user, 10000, date1);
        DailyStepData data2 = createDailyStepData(user, 8000, date2);
        entityManager.persist(data1);
        entityManager.persist(data2);
        entityManager.flush();

        // When
        Page<DailyStepData> page = dailyStepRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"))
        );

        // Then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent().get(0).getDate()).isEqualTo(date1);
        assertThat(page.getContent().get(1).getDate()).isEqualTo(date2);
    }

    @Test
    void save_ShouldSetLastUpdatedAutomatically() {
        // Given
        User user = new User();
        user.setUsername("john.doe");
        UserProfile userProfile=new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user.setUserProfile(userProfile);
        entityManager.persist(user);

        DailyStepData data = new DailyStepData();
        data.setUser(user);
        data.setSteps(10000);
        data.setDate(LocalDate.now());

        // When
        DailyStepData saved = dailyStepRepository.save(data);
        entityManager.flush();

        // Then
        assertThat(saved.getLastUpdated()).isNotNull();
        assertThat(saved.getLastUpdated()).isBeforeOrEqualTo(Instant.now());
    }
}