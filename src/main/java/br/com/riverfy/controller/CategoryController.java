package br.com.riverfy.controller;

import br.com.riverfy.dto.category.CategoryRequest;
import br.com.riverfy.dto.category.CategoryResponse;
import br.com.riverfy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categories", description = "Endpoints for managing user categories and ministries.")
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category", description = "Creates a new category or ministry segment in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request data.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Search categories with pagination", description = "Retrieves a paginated list of active categories filtered by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated categories retrieved successfully.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponse>> search(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Page<CategoryResponse> response = categoryService.search(searchTerm, page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves specific category details using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found successfully."),
            @ApiResponse(responseCode = "404", description = "Category not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        CategoryResponse response = categoryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a category", description = "Updates an existing category's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data."),
            @ApiResponse(responseCode = "404", description = "Category not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a category (Soft Delete)", description = "Logically removes a category from the system by setting its active state to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Category not found.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
