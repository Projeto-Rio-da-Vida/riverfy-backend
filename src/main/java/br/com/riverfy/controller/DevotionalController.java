package br.com.riverfy.controller;

import br.com.riverfy.dto.devotional.DevotionalRequest;
import br.com.riverfy.dto.devotional.DevotionalResponse;
import br.com.riverfy.model.enums.DevotionalStatus;
import br.com.riverfy.service.DevotionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Devotionals", description = "Endpoints for managing devotionals and their multi-page content.")
@RestController
@RequestMapping("/devotionals")
public class DevotionalController {

    private final DevotionalService devotionalService;

    public DevotionalController(DevotionalService devotionalService) {
        this.devotionalService = devotionalService;
    }

    @Operation(summary = "Create a new devotional", description = "Creates a devotional along with its ordered pages. Page 1 must contain a title.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Devotional successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request data or page validation failed.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<DevotionalResponse> create(@Valid @RequestBody DevotionalRequest request) {
        DevotionalResponse response = devotionalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Search devotionals with pagination and status", description = "Retrieves a paginated list of active devotionals filtered by title and an optional status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated devotionals retrieved successfully.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Page<DevotionalResponse>> search(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "status", required = false) DevotionalStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "created_at") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        Page<DevotionalResponse> response = devotionalService.search(searchTerm, status, page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get devotional by ID", description = "Retrieves specific devotional details, including all its pages sorted by page number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devotional found successfully."),
            @ApiResponse(responseCode = "404", description = "Devotional not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<DevotionalResponse> findById(@PathVariable Long id) {
        DevotionalResponse response = devotionalService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a devotional", description = "Updates an existing devotional's details and replaces its pages by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devotional updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data or page validation failed."),
            @ApiResponse(responseCode = "404", description = "Devotional not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<DevotionalResponse> update(@PathVariable Long id, @Valid @RequestBody DevotionalRequest request) {
        DevotionalResponse response = devotionalService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a devotional (Soft Delete)", description = "Logically removes a devotional from the system by setting its active state to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Devotional deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Devotional not found.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        devotionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
