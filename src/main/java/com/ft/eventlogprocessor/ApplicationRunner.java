package com.ft.eventlogprocessor;

import com.ft.eventlogprocessor.model.Event;
import com.ft.eventlogprocessor.parser.EventParser;
import com.ft.eventlogprocessor.reader.EventFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private final EventFileReader eventFileReader;
    private final EventParser eventParser;

    @Autowired
    public ApplicationRunner(EventFileReader eventFileReader, EventParser eventParser) {
        this.eventFileReader = eventFileReader;
        this.eventParser = eventParser;
    }

    @Override
    public void run(String... args) throws Exception {

      Path path = Path.of("src/main/resources/input/events.jsonl");

        /* Test ensuring proper file reading
        eventFileReader.readEvents(path)
                .forEach(System.out::println);
         */

        eventFileReader.readEvents(path)
                .forEach(line -> {
                    Event event = eventParser.parse(line);

                    System.out.println(event);
                });



    }
}
