package com.stepthrone.stepcount.mapper;

import com.stepthrone.stepcount.dto.DailyStepResponse;
import com.stepthrone.stepcount.model.DailyStepData;
import com.stepthrone.stepcount.service.StepperUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StepDataMapper {
    private final StepperUtility utility;

    public DailyStepResponse dailyStepDatatoDailyStepResponse(DailyStepData data) {
        return new DailyStepResponse(
                data.getId(),
                data.getUser().getId(),
                data.getDate(),
                data.getSteps(),
                utility.calculateCalories(data),
                utility.calculateDistance(data),
                data.getLastUpdated()
        );
    }
}