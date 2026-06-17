package br.com.riverfy.dto.event;

import br.com.riverfy.model.Event;
import br.com.riverfy.model.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record EventRequest(
        @NotBlank(message = "O nome do evento é obrigatório")
        String name,

        @NotBlank(message = "A descrição do evento é obrigatório")
        String description,

        @NotNull(message = "A data do evento é obrigatória")
        LocalDateTime date,

        @NotNull(message = "O status do evento é obrigatório")
        EventStatus eventStatus,

        List<Long> participantIds
) {
        public Event toEntity() {
                Event event = new Event();
                event.setName(this.name());
                event.setDescription(this.description());
                event.setDate(this.date());
                event.setEventStatus(this.eventStatus());
                return event;
        }

        public void updateEntity(Event event) {
                event.setName(this.name());
                event.setDescription(this.description());
                event.setDate(this.date());
                event.setEventStatus(this.eventStatus());
        }
}
