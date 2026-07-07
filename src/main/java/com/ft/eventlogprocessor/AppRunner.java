package com.ft.eventlogprocessor;

import com.ft.eventlogprocessor.parser.EventParser;
import com.ft.eventlogprocessor.reader.EventFileReader;
import com.ft.eventlogprocessor.service.processor.EventLogProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class AppRunner implements CommandLineRunner {

    private final EventFileReader eventFileReader;
    private final EventParser eventParser;
    private final EventLogProcessor eventLogProcessor;

    @Autowired
    public AppRunner(EventFileReader eventFileReader, EventParser eventParser, EventLogProcessor eventLogProcessor) {
        this.eventFileReader = eventFileReader;
        this.eventParser = eventParser;
        this.eventLogProcessor = eventLogProcessor;
    }

    @Override
    public void run(String... args) throws Exception {

        Path path = Path.of("src/main/resources/input/events.jsonl");
        eventLogProcessor.process();



        /* Test ensuring proper file reading
        eventFileReader.readEvents(path)
                .forEach(System.out::println);
         */


        /* Testing event parser
        eventFileReader.readEvents(path)
                .forEach(line -> {
                    Event event = eventParser.parse(line);

                    System.out.println(event);
                });

         */


    }
}
