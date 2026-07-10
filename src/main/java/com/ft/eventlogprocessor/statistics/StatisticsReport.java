package com.ft.eventlogprocessor.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsReport {

    public void generateReport(StatisticsAggregator aggregator) {

        System.out.println("\n===== Event Log Statistics Report =====\n");

        System.out.println("1. Total Valid Events: " + aggregator.getTotalValidEvents());

        System.out.println("2. Total Invalid Lines: " + aggregator.getTotalInvalidEvents());

        printEventCountPerUser(aggregator);
        printPurchaseStatistics(aggregator);
        printMostActiveUser(aggregator);
        printTop3MostActiveUsers(aggregator.getMostActiveUsers());
        printEventCountPerAction(aggregator);


        System.out.println("\n======================================");

    }

    private String eventLabel(int count) {
        return count == 1 ? "event" : "events";
    }

    private void printEventCountPerAction(StatisticsAggregator aggregator) {
        System.out.println("\n7. Event Count Per Action");

        aggregator.getEventsPerAction()
                .forEach((action, count) ->
                        System.out.println(action + ": " + count));
    }

    private void printTop3MostActiveUsers(List<UserActivity> users) {
        System.out.println("\n6. Top 3 Most Active Users");

        for (int i = 0; i < 3; i++) {
            UserActivity user = users.get(i);

            System.out.printf(
                    "%d. %s - %d %s%n",
                    i + 1,
                    user.userId(),
                    user.eventCount(),
                    eventLabel(user.eventCount())
            );
        }
    }

    private void printMostActiveUser(StatisticsAggregator aggregator) {
        UserActivity user = aggregator.getMostActiveUser();

        System.out.println("\n5. Most Active User");

        if (user != null) {
            System.out.printf("%s : %d %s%n", user.userId(), user.eventCount(), eventLabel(user.eventCount()));

        }
    }

    private void printEventCountPerUser(StatisticsAggregator aggregator) {
        System.out.println("\n3. Event Count Per User");

        aggregator.getMostActiveUsers()
                .forEach(user -> System.out.println(user.userId() + ": " + user.eventCount() + " " + eventLabel(user.eventCount())));
    }

    private void printPurchaseStatistics(StatisticsAggregator aggregator) {
        System.out.println("\n4. Purchase Statistics");

        System.out.println("Total purchase amount: "
                + aggregator.getTotalPurchaseAmount());

        System.out.println("Average purchase amount: "
                + aggregator.getAveragePurchaseAmount());

        System.out.println("Largest purchase: "
                + aggregator.getLargestPurchase());
    }
}
