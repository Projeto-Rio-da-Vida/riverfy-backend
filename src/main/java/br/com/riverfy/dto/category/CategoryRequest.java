package br.com.riverfy.dto.category;

import br.com.riverfy.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name is required")
        String name,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
    public Category toEntity() {
        Category category = new Category();
        category.setName(this.name());
        category.setDescription(this.description());
        return category;
    }

    public void updateEntity(Category category) {
        category.setName(this.name());
        category.setDescription(this.description());
    }
}
