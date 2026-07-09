package com.ft.eventlogprocessor.service.processor;

import com.ft.eventlogprocessor.exception.InvalidEventException;
import com.ft.eventlogprocessor.model.Event;
import com.ft.eventlogprocessor.parser.EventParser;
import com.ft.eventlogprocessor.reader.EventFileReader;
import com.ft.eventlogprocessor.statistics.StatisticsAggregator;
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

    @Autowired
    public EventLogProcessor(EventParser parser, EventFileReader reader, EventValidator validator, StatisticsAggregator aggregator) {
        this.parser = parser;
        this.reader = reader;
        this.validator = validator;
        this.aggregator = aggregator;
    }

    public void process() throws IOException {
        reader.readEvents(Path.of(EVENTS_PATH))
                .forEach(line -> {

                    try {
                        Event event = parser.parse(line);
                        validator.validate(event);
                        aggregator.process(event);


                    } catch (InvalidEventException e) {
                        aggregator.updateInvalidCount();
                    }
                });
    }
}
