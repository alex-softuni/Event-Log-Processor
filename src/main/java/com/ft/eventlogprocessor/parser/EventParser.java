package com.ft.eventlogprocessor.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.eventlogprocessor.exception.InvalidEventException;
import com.ft.eventlogprocessor.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventParser {

    private final ObjectMapper mapper;

    @Autowired
    public EventParser(ObjectMapper mapper) {
        this.mapper = mapper;

    }

    public Event parse(String line) {
        try {

            return mapper.readValue(line, Event.class);
        } catch (JsonProcessingException e) {

            throw new InvalidEventException("Invalid JSON");
        }
    }
}
