package com.ft.eventlogprocessor.validation;

import com.ft.eventlogprocessor.exception.InvalidEventException;
import com.ft.eventlogprocessor.model.Action;
import com.ft.eventlogprocessor.model.Event;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EventValidatorTest {

    private final EventValidator validator = new EventValidator();

    @Test
    void shouldValidateValidLoginEvent() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.LOGIN,
                null,
                null,
                null
        );

        assertThatCode(() -> validator.validate(event))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenTimestampIsMissing() {

        Event event = new Event(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.LOGIN,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Event timestamp is missing");
    }

    @Test
    void shouldThrowExceptionWhenEventIdIsMissing() {

        Event event = new Event(
                Instant.now(),
                null,
                UUID.randomUUID(),
                Action.LOGIN,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Event ID is missing");
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsMissing() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                null,
                Action.LOGIN,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("User ID is missing");
    }

    @Test
    void shouldThrowExceptionWhenActionIsMissing() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Action is missing");
    }

    @Test
    void shouldThrowExceptionWhenViewEventHasNoArticleId() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.VIEW,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("View event requires article ID");
    }

    @Test
    void shouldThrowExceptionWhenClickEventHasNoTarget() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.CLICK,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Click event requires target");
    }

    @Test
    void shouldThrowExceptionWhenPurchaseEventHasNoAmount() {

        Event event = new Event(
                Instant.now(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Action.PURCHASE,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> validator.validate(event))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Purchase event requires amount");
    }
}
