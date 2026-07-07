package com.ft.eventlogprocessor.reader;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class EventFileReader {

    public Stream<String> readEvents(Path path) throws IOException {

        return Files.lines(path);
    }

}
