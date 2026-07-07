package com.ft.eventlogprocessor.service.processor;

import com.ft.eventlogprocessor.model.Event;
import com.ft.eventlogprocessor.parser.EventParser;
import com.ft.eventlogprocessor.reader.EventFileReader;
import com.ft.eventlogprocessor.validation.EventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;


@Service
public class EventLogProcessor {
    private static final String EVENTS_PATH = "src/main/resources/input/events.jsonl";
    private static int INVALID_EVENTS_COUNT = 0;

    private final EventParser eventParser;
    private final EventFileReader eventFileReader;
    private final EventValidator eventValidator;

    @Autowired
    public EventLogProcessor(EventParser eventParser, EventFileReader eventFileReader, EventValidator eventValidator, EventValidator eventValidator1) {
        this.eventParser = eventParser;
        this.eventFileReader = eventFileReader;

        this.eventValidator = eventValidator;
    }

    public void process() throws IOException {
        eventFileReader.readEvents(Path.of(EVENTS_PATH))
                .forEach(line -> {

                    try {
                        Event event = eventParser.parse(line);
                        eventValidator.validate(event);


                    } catch (Exception e) {

                        INVALID_EVENTS_COUNT++;
                        System.out.println(INVALID_EVENTS_COUNT);
                    }
                });
    }
}
