package com.ft.eventlogprocessor.service.processor;

import com.ft.eventlogprocessor.exception.InvalidEventException;
import com.ft.eventlogprocessor.model.Event;
import com.ft.eventlogprocessor.parser.EventParser;
import com.ft.eventlogprocessor.reader.EventFileReader;
import com.ft.eventlogprocessor.statistics.StatisticsAggregator;
import com.ft.eventlogprocessor.statistics.StatisticsReport;
import com.ft.eventlogprocessor.validation.EventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;


@Service
public class EventLogProcessor {
    private static final String EVENTS_PATH = "src/main/resources/input/events.jsonl";

    private final EventParser parser;
    private final EventFileReader reader;
    private final EventValidator validator;
    private final StatisticsAggregator aggregator;
    private final StatisticsReport report;

    @Autowired
    public EventLogProcessor(EventParser parser, EventFileReader reader, EventValidator validator, StatisticsAggregator aggregator, StatisticsReport report) {
        this.parser = parser;
        this.reader = reader;
        this.validator = validator;
        this.aggregator = aggregator;
        this.report = report;
    }

    public void process() throws IOException {
        reader.readEvents(Path.of(EVENTS_PATH))
                .forEach(line -> {
                    if (line.isBlank()) {
                        return;
                    }

                    try {
                        Event event = parser.parse(line);
                        validator.validate(event);
                        aggregator.process(event);

                    } catch (InvalidEventException e) {

                        aggregator.updateInvalidCount();
                    }
                });

        report.generateReport(aggregator);
    }
}
