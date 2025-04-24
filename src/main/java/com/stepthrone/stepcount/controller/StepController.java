package com.stepthrone.stepcount.controller;

import com.stepthrone.stepcount.dto.DailyLeaderboardResponse;
import com.stepthrone.stepcount.dto.DailyStepRequest;
import com.stepthrone.stepcount.dto.DailyStepResponse;
import com.stepthrone.stepcount.dto.GlobalLeaderboardResponse;
import com.stepthrone.stepcount.service.StepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/steps")
@RequiredArgsConstructor
public class StepController {
    private final StepService stepService;

    @PostMapping
    public ResponseEntity<DailyStepResponse> submitSteps(
            @RequestBody DailyStepRequest request,
            Principal principal) {
        DailyStepResponse response = stepService.submitDailySteps(principal.getName(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard/daily")
    public ResponseEntity<DailyLeaderboardResponse> getDailyLeaderboard(
            @Valid  @RequestParam(required = true)  @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date) {
        DailyLeaderboardResponse response = stepService.getDailyLeaderboard(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard/global")
    public ResponseEntity<GlobalLeaderboardResponse> getGlobalLeaderboard() {
        GlobalLeaderboardResponse response = stepService.getGlobalLeaderboard();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<DailyStepResponse>> getUserHistory(Principal principal) {
        return ResponseEntity.ok(stepService.getUserHistory(principal));
    }

    @GetMapping("/user/daily")
    public ResponseEntity<DailyStepResponse> getUserDailySteps(
            @Valid  @RequestParam(required = true)  @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date,
            Principal principal) {
        return ResponseEntity.ok(stepService.getUserDailySteps(principal, date));
    }
}
