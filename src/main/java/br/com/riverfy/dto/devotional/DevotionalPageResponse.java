package br.com.riverfy.dto.devotional;

import br.com.riverfy.model.DevotionalPage;

public record DevotionalPageResponse(
        Long id,
        String title,
        String text,
        int pageNumber
) {
    public static DevotionalPageResponse fromEntity(DevotionalPage page) {
        return new DevotionalPageResponse(
                page.getId(),
                page.getTitle(),
                page.getText(),
                page.getPageNumber()
        );
    }
}
