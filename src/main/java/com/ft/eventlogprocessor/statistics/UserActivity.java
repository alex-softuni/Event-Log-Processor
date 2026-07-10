package com.ft.eventlogprocessor.statistics;

import java.util.UUID;

public record UserActivity(
        UUID userId,
        int eventCount
) {
}
