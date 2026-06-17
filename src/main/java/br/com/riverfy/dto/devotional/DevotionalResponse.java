package br.com.riverfy.dto.devotional;

import br.com.riverfy.model.Devotional;
import br.com.riverfy.model.enums.DevotionalStatus;

import java.util.List;
import java.util.stream.Collectors;

public record DevotionalResponse(
        Long id,
        String title,
        DevotionalStatus status,
        List<DevotionalPageResponse> pages
) {
    public static DevotionalResponse fromEntity(Devotional devotional) {
        List<DevotionalPageResponse> pageDTOs = devotional.getPages().stream()
                .map(DevotionalPageResponse::fromEntity)
                .collect(Collectors.toList());

        return new DevotionalResponse(
                devotional.getId(),
                devotional.getTitle(),
                devotional.getStatus(),
                pageDTOs
        );
    }
}
