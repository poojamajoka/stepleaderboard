package com.stepthrone.stepcount.service;

import com.stepthrone.stepcount.dto.DailyLeaderboardResponse;
import com.stepthrone.stepcount.dto.DailyStepRequest;
import com.stepthrone.stepcount.dto.DailyStepResponse;
import com.stepthrone.stepcount.dto.GlobalLeaderboardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;

public interface IStepService {
    DailyStepResponse submitDailySteps(String username, DailyStepRequest request);

    public DailyLeaderboardResponse getDailyLeaderboard(LocalDate date);

    GlobalLeaderboardResponse getGlobalLeaderboard(Pageable pageable);

    public Page<DailyStepResponse> getUserHistory(Principal principal, Pageable pageable);

    public DailyStepResponse getUserDailySteps(Principal principal, LocalDate date);

}
