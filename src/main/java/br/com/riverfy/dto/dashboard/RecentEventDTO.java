package br.com.riverfy.dto.dashboard;

import br.com.riverfy.model.Event;
import br.com.riverfy.model.enums.EventStatus;

import java.time.LocalDateTime;

public record RecentEventDTO(
        Long id,
        String name,
        LocalDateTime date,
        EventStatus eventStatus
) {
    public static RecentEventDTO fromEntity(Event event) {
        return new RecentEventDTO(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getEventStatus()
        );
    }
}
