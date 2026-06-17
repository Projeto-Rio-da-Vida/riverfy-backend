package br.com.riverfy.dto.event;

import br.com.riverfy.dto.user.UserResponse;
import br.com.riverfy.model.Event;
import br.com.riverfy.model.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record EventResponse(
        Long id,
        String name,
        String description,
        LocalDateTime date,
        EventStatus eventStatus,
        List<UserResponse> participants
) {
    public static EventResponse fromEntity(Event event) {
        List<UserResponse> userDTOs = null;
        if (event.getParticipants() != null) {
            userDTOs = event.getParticipants().stream()
                    .map(UserResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getEventStatus(),
                userDTOs
        );
    }
}
