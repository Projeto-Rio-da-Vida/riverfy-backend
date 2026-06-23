package br.com.riverfy.dto.dashboard;

import br.com.riverfy.model.Notice;

import java.time.LocalDate;

public record RecentNoticeDTO(
        Long id,
        String name,
        LocalDate date
) {
    public static RecentNoticeDTO fromEntity(Notice notice) {
        return new RecentNoticeDTO(
                notice.getId(),
                notice.getName(),
                notice.getDate()
        );
    }
}
