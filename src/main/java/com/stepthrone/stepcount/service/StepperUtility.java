package com.stepthrone.stepcount.service;

import com.stepthrone.stepcount.model.DailyStepData;
import org.springframework.stereotype.Component;

@Component
public class StepperUtility {

    public double calculateCalories(DailyStepData data) {
        // Simple calculation - adjust as needed
        return data.getSteps() * data.getUser().getUserProfile().getWeight() * 0.0005;
    }

    public double calculateDistance(DailyStepData data) {
        // Assuming average step length of 0.8 meters
        return data.getSteps() * 0.8 / 1000; // return in kilometers
    }
}
