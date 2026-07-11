package com.ft.eventlogprocessor.statistics;

import com.ft.eventlogprocessor.model.Action;
import com.ft.eventlogprocessor.model.Event;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class StatisticsAggregatorTest {

    @Test
    void shouldIncreaseValidEventCountWhenProcessingEvent() {

        StatisticsAggregator aggregator = new StatisticsAggregator();

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.LOGIN,
                null,
                null,
                null
        );

        aggregator.process(event);

        assertThat(aggregator.getTotalValidEvents()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseInvalidEventCount() {

        StatisticsAggregator aggregator = new StatisticsAggregator();

        aggregator.updateInvalidCount();
        aggregator.updateInvalidCount();

        assertThat(aggregator.getTotalInvalidEvents())
                .isEqualTo(2);
    }

    @Test
    void shouldCountEventsPerUser() {

        StatisticsAggregator aggregator = new StatisticsAggregator();

        UUID userOne = UUID.randomUUID();
        UUID userTwo = UUID.randomUUID();

        Event firstEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userOne,
                Action.LOGIN,
                null,
                null,
                null
        );


        Event secondEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userOne,
                Action.VIEW,
                "article-1",
                null,
                null
        );


        Event thirdEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userTwo,
                Action.LOGOUT,
                null,
                null,
                null
        );

        aggregator.process(firstEvent);
        aggregator.process(secondEvent);
        aggregator.process(thirdEvent);

        assertThat(aggregator.getEventsPerUser())
                .containsEntry(userOne, 2)
                .containsEntry(userTwo, 1);

    }

    @Test
    void shouldCalculatePurchaseStatistics() {

        StatisticsAggregator aggregator = new StatisticsAggregator();

        UUID userId = UUID.randomUUID();


        Event firstPurchase = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userId,
                Action.PURCHASE,
                null,
                null,
                new BigDecimal("10.50")
        );


        Event secondPurchase = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userId,
                Action.PURCHASE,
                null,
                null,
                new BigDecimal("25.00")
        );


        aggregator.process(firstPurchase);
        aggregator.process(secondPurchase);


        assertThat(aggregator.getTotalPurchaseAmount())
                .isEqualByComparingTo("35.50");

        assertThat(aggregator.getLargestPurchase())
                .isEqualByComparingTo("25.00");

        assertThat(aggregator.getAveragePurchaseAmount())
                .isEqualByComparingTo("17.75");
    }

    @Test
    void shouldReturnMostActiveUser() {
        StatisticsAggregator aggregator = new StatisticsAggregator();

        UUID userOne = UUID.randomUUID();
        UUID userTwo = UUID.randomUUID();

        Event firstEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userOne,
                Action.LOGIN,
                null,
                null,
                null
        );


        Event secondEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userOne,
                Action.VIEW,
                "article-1",
                null,
                null
        );


        Event thirdEvent = new Event(
                Instant.now(),
                UUID.randomUUID(),
                userTwo,
                Action.LOGOUT,
                null,
                null,
                null
        );

        aggregator.process(firstEvent);
        aggregator.process(secondEvent);
        aggregator.process(thirdEvent);

        UserActivity result = aggregator.getMostActiveUser();

        assertThat(result.userId()).isEqualTo(userOne);
        assertThat(result.eventCount()).isEqualTo(2);

    }

    @Test
    void shouldReturnTopThreeMostActiveUsersInDescendingOrder() {
        StatisticsAggregator aggregator = new StatisticsAggregator();

        UUID userOne = UUID.randomUUID();
        UUID userTwo = UUID.randomUUID();
        UUID userThree = UUID.randomUUID();
        UUID userFour = UUID.randomUUID();

        List<Event> events = List.of(
                createEvent(userOne, Action.LOGIN),
                createEvent(userOne, Action.LOGOUT),
                createEvent(userOne, Action.LOGIN),
                createEvent(userTwo, Action.LOGIN),
                createEvent(userTwo, Action.LOGOUT),
                createEvent(userThree, Action.LOGIN),
                createEvent(userFour, Action.LOGOUT)
        );

        processEvent(aggregator, events);

        List<UserActivity> result = aggregator.getTopThreeMostActiveUsers();

        assertThat(result)
                .containsExactly(
                        new UserActivity(userOne, 3),
                        new UserActivity(userTwo, 2),
                        new UserActivity(userThree, 1)
                );

    }


    private static void processEvent(StatisticsAggregator aggregator, List<Event> events) {
        events.forEach(aggregator::process);
    }

    private Event createEvent(UUID userId, Action action) {
        return new Event(
                Instant.now(),
                UUID.randomUUID(),
                userId,
                action,
                null,
                null,
                null
        );
    }
}
