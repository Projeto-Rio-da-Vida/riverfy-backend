package br.com.riverfy.dto.devotional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DevotionalPageRequest(
        String title,

        @NotBlank(message = "Page text is required")
        String text,

        @NotNull(message = "Page number is required")
        Integer pageNumber
) { }
