package com.stepthrone.stepcount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepthrone.stepcount.dto.*;
import com.stepthrone.stepcount.service.StepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StepController.class)
class StepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StepService stepService;

    @MockitoBean
    private Principal principal;

    private DailyStepRequest dailyStepRequest;
    private DailyStepResponse dailyStepResponse;
    private DailyLeaderboardResponse dailyLeaderboardResponse;
    private GlobalLeaderboardResponse globalLeaderboardResponse;
    private Ranking ranking1, ranking2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        dailyStepRequest = new DailyStepRequest(LocalDate.now(), 10000);

        dailyStepResponse = new DailyStepResponse(1L,101L,LocalDate.now(),10000,350.5,7.5,Instant.now());

        ranking1 = new Ranking();
        ranking1.setUserId(101L);
        ranking1.setFirstName("John");
        ranking1.setLastName("Doe");
        ranking1.setTotalSteps(15000);
        ranking1.setRank(1);

        ranking2 = new Ranking();
        ranking2.setUserId(102L);
        ranking2.setFirstName("Jane");
        ranking2.setLastName("Smith");
        ranking2.setTotalSteps(12000);
        ranking2.setRank(2);

        dailyLeaderboardResponse = new DailyLeaderboardResponse(
                LocalDate.now(),
                List.of(ranking1, ranking2)
        );

        globalLeaderboardResponse = new GlobalLeaderboardResponse(
                List.of(ranking1, ranking2),
                0,
                10
        );

        Mockito.when(principal.getName()).thenReturn("user1");
    }

    @Test
    void submitSteps_ShouldReturnDailyStepResponse() throws Exception {
        Mockito.when(stepService.submitDailySteps("user1", dailyStepRequest))
                .thenReturn(dailyStepResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/steps")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dailyStepRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(101L))
                .andExpect(jsonPath("$.steps").value(10000))
                .andExpect(jsonPath("$.calories").value(350.5))
                .andExpect(jsonPath("$.distance").value(7.5));
    }

    @Test
    void getDailyLeaderboard_ShouldReturnLeaderboard() throws Exception {
        LocalDate date = LocalDate.of(2023, 10, 15);
        Mockito.when(stepService.getDailyLeaderboard(date))
                .thenReturn(dailyLeaderboardResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/steps/leaderboard/daily")
                        .param("date", "10-15-2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2023-10-15"))
                .andExpect(jsonPath("$.rankings[0].userId").value(101L))
                .andExpect(jsonPath("$.rankings[0].firstName").value("John"))
                .andExpect(jsonPath("$.rankings[0].totalSteps").value(15000))
                .andExpect(jsonPath("$.rankings[0].rank").value(1));
    }

    @Test
    void getGlobalLeaderboard_ShouldReturnPaginatedLeaderboard() throws Exception {
        Mockito.when(stepService.getGlobalLeaderboard(PageRequest.of(0, 10)))
                .thenReturn(globalLeaderboardResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/steps/leaderboard/global")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rankings[0].userId").value(101L))
                .andExpect(jsonPath("$.rankings[0].firstName").value("John"))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getUserHistory_ShouldReturnPaginatedHistory() throws Exception {
        Page<DailyStepResponse> page = new PageImpl<>(List.of(dailyStepResponse));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));

        Mockito.when(stepService.getUserHistory(principal, pageable))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/steps/history")
                        .principal(principal)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "date")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(101L))
                .andExpect(jsonPath("$.content[0].steps").value(10000))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getUserDailySteps_ShouldReturnDailySteps() throws Exception {
        LocalDate date = LocalDate.of(2023, 10, 15);
        Mockito.when(stepService.getUserDailySteps(principal, date))
                .thenReturn(dailyStepResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/steps/user/daily")
                        .principal(principal)
                        .param("date", "10-15-2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(101L))
                .andExpect(jsonPath("$.steps").value(10000))
                .andExpect(jsonPath("$.distance").value(7.5));
    }
}