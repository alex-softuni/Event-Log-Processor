package com.ft.eventlogprocessor.validation;

import com.ft.eventlogprocessor.exception.InvalidEventException;
import com.ft.eventlogprocessor.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventValidator {

    public void validate(Event event) {

        validateRequiredFields(event);
        validateActionSpecificFields(event);
    }

    private void validateRequiredFields(Event event) {

        if (event.getTimestamp() == null) {
            throw new InvalidEventException("Event timestamp is missing");
        }

        if (event.getEventId() == null) {
            throw new InvalidEventException("Event ID is missing");
        }

        if (event.getUserId() == null) {
            throw new InvalidEventException("User ID is missing");
        }

        if (event.getAction() == null) {
            throw new InvalidEventException("Action is missing");
        }

    }


    private void validateActionSpecificFields(Event event) {

        switch (event.getAction()) {
            case VIEW -> validateView(event);
            case CLICK -> validateClick(event);
            case PURCHASE -> validatePurchase(event);
        }
    }

    private void validateView(Event event) {

        if (event.getArticleId() == null || event.getArticleId().isBlank()) {
            throw new InvalidEventException("View event requires article ID");
        }
    }

    private void validateClick(Event event) {

        if (event.getTarget() == null || event.getTarget().isBlank()) {
            throw new InvalidEventException("Click event requires target");
        }
    }

    private void validatePurchase(Event event) {

        if (event.getAmount() == null) {
            throw new InvalidEventException("Purchase event requires amount");
        }
    }
}
