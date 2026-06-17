package br.com.riverfy.dto.notice;

import br.com.riverfy.model.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NoticeRequest(
        @NotBlank(message = "Notice name is required")
        String name,

        @NotBlank(message = "Notice description is required")
        String description,

        @NotNull(message = "Notice date is required")
        LocalDate date
) {
    public Notice toEntity() {
        Notice notice = new Notice();
        notice.setName(this.name());
        notice.setDescription(this.description());
        notice.setDate(this.date());
        return notice;
    }

    public void updateEntity(Notice notice) {
        notice.setName(this.name());
        notice.setDescription(this.description());
        notice.setDate(this.date());
    }
}
