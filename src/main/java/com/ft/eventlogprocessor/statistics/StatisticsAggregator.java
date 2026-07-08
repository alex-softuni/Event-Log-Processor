package com.ft.eventlogprocessor.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Component
public class StatisticsAggregator {

    private int totalValidEvents;
    private int totalInvalidEvents;
    private Map<UUID,Integer> eventsPerUser;
    private Map<UUID,Integer> eventsPerAction;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal largestPurchase;
    private int purchaseCount;




}
