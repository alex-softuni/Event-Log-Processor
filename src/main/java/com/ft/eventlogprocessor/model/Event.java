package com.ft.eventlogprocessor.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class Event {

    private Instant timestamp;
    private UUID eventId;
    private UUID userId;
    private Action action;

    private String articleId;
    private String target;
    private BigDecimal amount;


    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", action=" + action +
                ", articleId='" + articleId + '\'' +
                ", target='" + target + '\'' +
                ", amount=" + amount +
                '}';
    }
}
