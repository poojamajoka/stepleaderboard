package com.stepthrone.stepcount.service;


import com.stepthrone.exception.customexception.InvalidDateException;
import com.stepthrone.exception.customexception.ProfileNotFoundException;
import com.stepthrone.exception.customexception.StepDataNotFoundException;
import com.stepthrone.stepcount.dto.*;
import com.stepthrone.stepcount.mapper.StepDataMapper;
import com.stepthrone.stepcount.model.DailyStepData;
import com.stepthrone.stepcount.repository.DailyStepRepository;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import com.stepthrone.userprofile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepServiceTest {

    @Mock
    private DailyStepRepository stepRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StepDataMapper stepDataMapper;

    @Mock
    private Principal principal;

    @InjectMocks
    private StepService stepService;

    private User testUser;
    private DailyStepRequest stepRequest;
    private DailyStepData stepData;
    private DailyStepResponse stepResponse;

    @BeforeEach
    void setUp() {
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john.doe");
        testUser.setUserProfile(profile);

        stepRequest = new DailyStepRequest(LocalDate.now(), 10000);

        stepData = new DailyStepData();
        stepData.setId(1L);
        stepData.setUser(testUser);
        stepData.setDate(LocalDate.now());
        stepData.setSteps(10000);
        stepData.setLastUpdated(Instant.now());

        stepResponse = new DailyStepResponse(
                1L, testUser.getId(),LocalDate.now()
                , 10000, 350.0, 7.5, Instant.now()
        );

    }

    @Test
    void submitDailySteps_ShouldCreateNewEntryWhenNotExists() {
        // Arrange
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(stepRepository.findByUserAndDate(testUser, stepRequest.getDate())).thenReturn(Optional.empty());
        when(stepRepository.save(any(DailyStepData.class))).thenReturn(stepData);
        when(stepDataMapper.dailyStepDatatoDailyStepResponse(stepData)).thenReturn(stepResponse);

        // Act
        DailyStepResponse response = stepService.submitDailySteps("john.doe", stepRequest);

        // Assert
        assertNotNull(response);
        assertEquals(10000, response.getSteps());
        verify(stepRepository).save(any(DailyStepData.class));
    }

    @Test
    void submitDailySteps_ShouldUpdateExistingEntryWhenExists() {
        // Arrange
        DailyStepData existingData = new DailyStepData();
        existingData.setSteps(5000);

        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(stepRepository.findByUserAndDate(testUser, stepRequest.getDate())).thenReturn(Optional.of(existingData));
        when(stepRepository.save(existingData)).thenReturn(stepData);
        when(stepDataMapper.dailyStepDatatoDailyStepResponse(stepData)).thenReturn(stepResponse);

        // Act
        DailyStepResponse response = stepService.submitDailySteps("john.doe", stepRequest);

        // Assert
        assertNotNull(response);
        assertEquals(10000, response.getSteps());
        verify(stepRepository).save(existingData);
    }

    @Test
    void submitDailySteps_ShouldThrowExceptionForFutureDate() {
        // Arrange
        DailyStepRequest futureRequest = new DailyStepRequest(LocalDate.now().plusDays(1), 10000);

        // Act & Assert
        assertThrows(InvalidDateException.class, () ->
                stepService.submitDailySteps("john.doe", futureRequest));
    }

    @Test
    void submitDailySteps_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProfileNotFoundException.class, () ->
                stepService.submitDailySteps("unknown.user", stepRequest));
    }

    @Test
    void getDailyLeaderboard_ShouldReturnCorrectRankings() {
        // Arrange
        LocalDate date = LocalDate.now();
        User user2 = new User();
        user2.setId(2L);
        UserProfile profile2 = new UserProfile();
        profile2.setFirstName("Jane");
        profile2.setLastName("Smith");
        user2.setUserProfile(profile2);

        DailyStepData data1 = new DailyStepData();
        data1.setUser(testUser);
        data1.setSteps(15000);

        DailyStepData data2 = new DailyStepData();
        data2.setUser(user2);
        data2.setSteps(12000);

        when(stepRepository.findAllByDateOrderByStepsDesc(date)).thenReturn(List.of(data1, data2));

        // Act
        DailyLeaderboardResponse response = stepService.getDailyLeaderboard(date);

        // Assert
        assertNotNull(response);
        assertEquals(date, response.getDate());
        assertEquals(2, response.getRankings().size());
        assertEquals(1, response.getRankings().get(0).getRank());
        assertEquals(15000, response.getRankings().get(0).getTotalSteps());
        assertEquals(2, response.getRankings().get(1).getRank());
        assertEquals(12000, response.getRankings().get(1).getTotalSteps());
    }

    @Test
    void getGlobalLeaderboard_ShouldReturnPaginatedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Object[] row1 = new Object[]{1L, "John", "Doe", 150000L};
        Object[] row2 = new Object[]{2L, "Jane", "Smith", 120000L};

        Page<Object[]> page = new PageImpl<>(List.of(row1, row2), pageable, 2);

        when(stepRepository.findUserTotalStepsRanking(pageable)).thenReturn(page);

        // Act
        GlobalLeaderboardResponse response = stepService.getGlobalLeaderboard(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRankings().size());
        assertEquals(1, response.getRankings().get(0).getRank());
        assertEquals(150000, response.getRankings().get(0).getTotalSteps());
        assertEquals(2, response.getRankings().get(1).getRank());
        assertEquals(120000, response.getRankings().get(1).getTotalSteps());
        assertEquals(2, response.getTotalElements());
    }

    @Test
    void getUserHistory_ShouldReturnPaginatedStepHistory() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<DailyStepData> stepPage = new PageImpl<>(List.of(stepData), pageable, 1);

        when(principal.getName()).thenReturn("john.doe");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(stepRepository.findAllByUserId(testUser.getId(), pageable)).thenReturn(stepPage);
        when(stepDataMapper.dailyStepDatatoDailyStepResponse(stepData)).thenReturn(stepResponse);

        // Act
        Page<DailyStepResponse> response = stepService.getUserHistory(principal, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(stepResponse, response.getContent().get(0));
    }

    @Test
    void getUserDailySteps_ShouldReturnDailySteps() {
        // Arrange
        LocalDate date = LocalDate.now();

        when(principal.getName()).thenReturn("john.doe");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(stepRepository.findByUserAndDate(testUser, date)).thenReturn(Optional.of(stepData));
        when(stepDataMapper.dailyStepDatatoDailyStepResponse(stepData)).thenReturn(stepResponse);

        // Act
        DailyStepResponse response = stepService.getUserDailySteps(principal, date);

        // Assert
        assertNotNull(response);
        assertEquals(10000, response.getSteps());
    }

    @Test
    void getUserDailySteps_ShouldThrowExceptionWhenDataNotFound() {
        // Arrange
        LocalDate date = LocalDate.now();

        when(principal.getName()).thenReturn("john.doe");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(stepRepository.findByUserAndDate(testUser, date)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StepDataNotFoundException.class, () ->
                stepService.getUserDailySteps(principal, date));
    }

    @Test
    void getUserDailySteps_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(principal.getName()).thenReturn("unknown.user");
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProfileNotFoundException.class, () ->
                stepService.getUserDailySteps(principal, LocalDate.now()));
    }
}