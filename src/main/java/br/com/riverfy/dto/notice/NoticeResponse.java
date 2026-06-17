package br.com.riverfy.dto.notice;

import br.com.riverfy.model.Notice;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        String name,
        String description,
        LocalDate date
) {
    public static NoticeResponse fromEntity(Notice notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getName(),
                notice.getDescription(),
                notice.getDate()
        );
    }
}
