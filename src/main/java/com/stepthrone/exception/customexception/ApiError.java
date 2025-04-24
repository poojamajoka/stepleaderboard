package com.stepthrone.exception.customexception;

import java.time.Instant;

public record ApiError(
        String message,
        String path,
        Integer statusCode,
        Instant timeStamp,
        String error
) {}
