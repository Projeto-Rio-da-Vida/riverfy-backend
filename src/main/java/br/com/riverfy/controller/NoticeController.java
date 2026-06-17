package br.com.riverfy.controller;

import br.com.riverfy.dto.notice.NoticeRequest;
import br.com.riverfy.dto.notice.NoticeResponse;
import br.com.riverfy.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notices", description = "Endpoints for managing system notices and announcements.")
@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "Create a new notice", description = "Creates a new announcement or notice in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notice successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request data.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<NoticeResponse> create(@Valid @RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Search notices with pagination", description = "Retrieves a paginated list of active notices filtered by name or description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated notices retrieved successfully.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Page<NoticeResponse>> search(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        Page<NoticeResponse> response = noticeService.search(searchTerm, page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get notice by ID", description = "Retrieves specific notice details using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notice found successfully."),
            @ApiResponse(responseCode = "404", description = "Notice not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> findById(@PathVariable Long id) {
        NoticeResponse response = noticeService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a notice", description = "Updates an existing notice's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notice updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data."),
            @ApiResponse(responseCode = "404", description = "Notice not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> update(@PathVariable Long id, @Valid @RequestBody NoticeRequest request) {
        NoticeResponse response = noticeService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a notice (Soft Delete)", description = "Logically removes a notice from the system by setting its active state to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notice deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Notice not found.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
