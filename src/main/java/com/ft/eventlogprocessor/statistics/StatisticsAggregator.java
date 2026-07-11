package com.ft.eventlogprocessor.statistics;

import com.ft.eventlogprocessor.model.Action;
import com.ft.eventlogprocessor.model.Event;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Data
@Component
public class StatisticsAggregator {

    private int totalValidEvents;
    private int totalInvalidEvents;
    private Map<UUID, Integer> eventsPerUser = new HashMap<>();
    private Map<Action, Integer> eventsPerAction = new EnumMap<>(Action.class);
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    private BigDecimal largestPurchase = BigDecimal.ZERO;
    private int purchaseCount;


    public void process(Event event) {

        updateValidEvents();
        updateEventsPerUser(event);
        updateEventsPerAction(event);
        updatePurchaseStatistics(event);


    }


    private void updateValidEvents() {
        totalValidEvents++;
    }

    private void updateEventsPerUser(Event event) {
        eventsPerUser.merge(event.getUserId(), 1, Integer::sum);
    }

    private void updateEventsPerAction(Event event) {
        eventsPerAction.merge(event.getAction(), 1, Integer::sum);
    }

    private void updatePurchaseStatistics(Event event) {

        if (!Action.PURCHASE.equals(event.getAction())) {
            return;
        }

        purchaseCount++;

        totalPurchaseAmount = totalPurchaseAmount.add(event.getAmount());

        if (event.getAmount().compareTo(largestPurchase) > 0) {
            largestPurchase = event.getAmount();
        }
    }

    public void updateInvalidCount() {
        totalInvalidEvents++;
    }

    public BigDecimal getAveragePurchaseAmount() {

        if (purchaseCount == 0) {
            return BigDecimal.ZERO;
        }

        return totalPurchaseAmount.divide(BigDecimal.valueOf(purchaseCount), 2, RoundingMode.HALF_UP);
    }

    public UserActivity getMostActiveUser() {

        return eventsPerUser.entrySet()
                .stream()
                .sorted(
                        Map.Entry.<UUID, Integer>comparingByValue()
                                .reversed()
                                .thenComparing(entry -> entry.getKey().toString())
                )
                .map(entry -> new UserActivity(entry.getKey(), entry.getValue()))
                .findFirst()
                .orElse(null);
    }

    public List<UserActivity> getMostActiveUsers() {

        return eventsPerUser.entrySet().stream()
                .sorted(
                        Map.Entry.<UUID,Integer>comparingByValue()
                                .reversed()
                                .thenComparing(entry -> entry.getKey().toString())
                )
                .map(entry -> new UserActivity(
                        entry.getKey(),
                        entry.getValue()
                )).toList();
    }

    public List<UserActivity> getTopThreeMostActiveUsers() {

        return eventsPerUser.entrySet().stream()
                .sorted(
                        Map.Entry.<UUID,Integer>comparingByValue()
                                .reversed()
                                .thenComparing(entry -> entry.getKey().toString())
                )
                .limit(3)
                .map(entry -> new UserActivity(
                        entry.getKey(),
                        entry.getValue()
                )).toList();
    }
}
