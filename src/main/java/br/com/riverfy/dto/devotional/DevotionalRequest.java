package br.com.riverfy.dto.devotional;

import br.com.riverfy.model.Devotional;
import br.com.riverfy.model.enums.DevotionalStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DevotionalRequest(
        @NotBlank(message = "Devotional title is required")
        String title,

        @NotNull(message = "Devotional status is required")
        DevotionalStatus status,

        @NotEmpty(message = "Devotional must have at least one page")
        List<@Valid DevotionalPageRequest> pages
) {
    public Devotional toEntity() {
        Devotional devotional = new Devotional();
        devotional.setTitle(this.title());
        devotional.setStatus(this.status());
        return devotional;
    }

    public void updateEntity(Devotional devotional) {
        devotional.setTitle(this.title());
        devotional.setStatus(this.status());
    }
}
