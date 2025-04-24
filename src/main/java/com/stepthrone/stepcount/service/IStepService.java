package com.stepthrone.stepcount.service;

import com.stepthrone.stepcount.dto.DailyLeaderboardResponse;
import com.stepthrone.stepcount.dto.DailyStepRequest;
import com.stepthrone.stepcount.dto.DailyStepResponse;
import com.stepthrone.stepcount.dto.GlobalLeaderboardResponse;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface IStepService {
    DailyStepResponse submitDailySteps(String username, DailyStepRequest request);

    public DailyLeaderboardResponse getDailyLeaderboard(LocalDate date);

    public GlobalLeaderboardResponse getGlobalLeaderboard();

    public List<DailyStepResponse> getUserHistory(Principal principal);

    public DailyStepResponse getUserDailySteps(Principal principal, LocalDate date);

}
