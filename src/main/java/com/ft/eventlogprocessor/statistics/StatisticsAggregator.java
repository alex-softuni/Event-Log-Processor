package com.ft.eventlogprocessor.statistics;

import com.ft.eventlogprocessor.model.Action;
import com.ft.eventlogprocessor.model.Event;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        if (largestPurchase == null || event.getAmount().compareTo(largestPurchase) > 0) {
            largestPurchase = event.getAmount();
        }
    }

    public void updateInvalidCount() {
        totalInvalidEvents++;
    }
}
