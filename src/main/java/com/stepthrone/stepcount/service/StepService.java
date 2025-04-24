package com.stepthrone.stepcount.service;

import com.stepthrone.exception.customexception.InvalidDateException;
import com.stepthrone.exception.customexception.ProfileNotFoundException;
import com.stepthrone.exception.customexception.StepDataNotFoundException;
import com.stepthrone.stepcount.dto.*;
import com.stepthrone.stepcount.mapper.StepDataMapper;
import com.stepthrone.stepcount.model.DailyStepData;
import com.stepthrone.stepcount.repository.DailyStepRepository;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class StepService implements IStepService {
    private final DailyStepRepository stepRepository;
    private final UserRepository userRepository;
    private final StepDataMapper stepDataMapper;

    @Override
    public DailyStepResponse submitDailySteps(String username,  DailyStepRequest request) {
        // Find the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        LocalDate date = request.getDate();
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Cannot submit steps for future dates");
        }
        // Try to find existing step data for this user and date
        Optional<DailyStepData> existingData = stepRepository.findByUserAndDate(user, date);

        DailyStepData stepData;

        if (existingData.isPresent()) {
            // If entry exists, add the new steps to the existing count
            stepData = existingData.get();
            stepData.setSteps(stepData.getSteps() + request.getSteps());
        } else {
            // If no entry exists, create a new one with the submitted steps
            stepData = new DailyStepData();
            stepData.setUser(user);
            stepData.setDate(date);
            stepData.setSteps(request.getSteps());
        }

        // Update the lastUpdated timestamp
        stepData.setLastUpdated(Instant.now());

        // Save the updated/new entry
        DailyStepData savedData = stepRepository.save(stepData);
        return stepDataMapper.dailyStepDatatoDailyStepResponse(savedData);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyLeaderboardResponse getDailyLeaderboard(LocalDate date) {
        List<DailyStepData> steps = stepRepository.findAllByDateOrderByStepsDesc(date);

        List<Ranking> rankings = IntStream.range(0, steps.size())
                .mapToObj(i -> new Ranking(
                        steps.get(i).getUser().getId(),
                        steps.get(i).getUser().getUserProfile().getFirstName(),
                        steps.get(i).getUser().getUserProfile().getLastName(),
                        steps.get(i).getSteps(),
                        i + 1
                ))
                .collect(Collectors.toList());

        return new DailyLeaderboardResponse(date, rankings);
    }

    @Override
    @Transactional(readOnly = true)
    public GlobalLeaderboardResponse getGlobalLeaderboard() {
        List<Object[]> userRankings = stepRepository.findUserTotalStepsRanking();

        List<Ranking> rankings = IntStream.range(0, userRankings.size())
                .mapToObj(i -> {
                    Object[] row = userRankings.get(i);
                    return new Ranking(
                            (Long) row[0],       // userId
                            (String) row[1],     // firstName
                            (String) row[2],     // lastName
                            ((Number) row[3]).intValue(),  // totalSteps
                            i + 1                // rank
                    );
                })
                .collect(Collectors.toList());

        return new GlobalLeaderboardResponse(rankings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyStepResponse> getUserHistory(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ProfileNotFoundException(principal.getName()));
        return stepRepository.findAllByUserId(user.getId()).stream()
                .map(stepDataMapper::dailyStepDatatoDailyStepResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DailyStepResponse getUserDailySteps(Principal principal, LocalDate date) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ProfileNotFoundException(principal.getName()));
        DailyStepData stepData = stepRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new StepDataNotFoundException(date + ""));
        return stepDataMapper.dailyStepDatatoDailyStepResponse(stepData);
    }


}
