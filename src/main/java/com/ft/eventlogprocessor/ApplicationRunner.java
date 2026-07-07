package com.ft.eventlogprocessor;

import com.ft.eventlogprocessor.reader.EventFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ApplicationRunner implements CommandLineRunner {

    private final EventFileReader eventFileReader;

    @Autowired
    public ApplicationRunner(EventFileReader eventFileReader) {
        this.eventFileReader = eventFileReader;
    }

    @Override
    public void run(String... args) throws Exception {

        Path path = Path.of(
                "src/main/resources/input/events.jsonl"
        );

        System.out.println();
        eventFileReader.readEvents(path)
                .forEach(System.out::println);

    }
}
