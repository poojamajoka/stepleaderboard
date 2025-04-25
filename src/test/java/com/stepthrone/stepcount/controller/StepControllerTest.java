package com.stepthrone.stepcount.controller;

import com.stepthrone.stepcount.dto.*;
import com.stepthrone.stepcount.service.StepService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepControllerTest {

    @Mock
    private StepService stepService;

    @Mock
    private Principal principal;

    @InjectMocks
    private StepController stepController;

    private DailyStepRequest dailyStepRequest;
    private DailyStepResponse dailyStepResponse;
    private DailyLeaderboardResponse dailyLeaderboardResponse;
    private GlobalLeaderboardResponse globalLeaderboardResponse;
    private Ranking ranking1, ranking2;

    @BeforeEach
    void setUp() {
        dailyStepRequest = new DailyStepRequest(LocalDate.now(), 10000);
        dailyStepResponse = new DailyStepResponse(1L, 101L, LocalDate.now(), 10000, 350.5, 7.5, Instant.now());

        ranking1 = new Ranking(101L, "John", "Doe", 15000, 1);
        ranking2 = new Ranking(102L, "Jane", "Smith", 12000, 2);

        dailyLeaderboardResponse = new DailyLeaderboardResponse(LocalDate.now(), List.of(ranking1, ranking2));
        globalLeaderboardResponse = new GlobalLeaderboardResponse(List.of(ranking1, ranking2), 0, 1);

      //  when(principal.getName()).thenReturn("user1");
    }

    @Test
    void submitSteps_ShouldReturnDailyStepResponse() {
        when(principal.getName()).thenReturn("user1");
        when(stepService.submitDailySteps("user1", dailyStepRequest)).thenReturn(dailyStepResponse);

        var response = stepController.submitSteps( dailyStepRequest,principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dailyStepResponse, response.getBody());
    }

    @Test
    void getDailyLeaderboard_ShouldReturnLeaderboard() {

        LocalDate date = LocalDate.of(2023, 10, 15);
        when(stepService.getDailyLeaderboard(date)).thenReturn(dailyLeaderboardResponse);
        LocalDate date2 = LocalDate.of(2023, 10, 15);
        var response = stepController.getDailyLeaderboard(date2);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dailyLeaderboardResponse, response.getBody());
    }

    @Test
    void getGlobalLeaderboard_ShouldReturnPaginatedLeaderboard() {
        Pageable pageable = PageRequest.of(0, 10);
        when(stepService.getGlobalLeaderboard(pageable)).thenReturn(globalLeaderboardResponse);

        var response = stepController.getGlobalLeaderboard(0, 10);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(globalLeaderboardResponse, response.getBody());
    }

    @Test
    void getUserHistory_ShouldReturnPaginatedHistory() {

        Page<DailyStepResponse> page = new PageImpl<>(List.of(dailyStepResponse));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));

        when(stepService.getUserHistory(principal, pageable)).thenReturn(page);

        var response = stepController.getUserHistory(principal, 0, 10, "date", "desc");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(dailyStepResponse, response.getBody().getContent().get(0));
    }

    @Test
    void getUserDailySteps_ShouldReturnDailySteps() {

        LocalDate date = LocalDate.of(2023, 10, 15);
        when(stepService.getUserDailySteps(principal, date)).thenReturn(dailyStepResponse);
        LocalDate date2 = LocalDate.of(2023, 10, 15);
        var response = stepController.getUserDailySteps( date2,principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dailyStepResponse, response.getBody());
    }
}
