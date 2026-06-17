package br.com.riverfy.controller;

import br.com.riverfy.dto.event.EventRequest;
import br.com.riverfy.dto.event.EventResponse;
import br.com.riverfy.model.enums.EventStatus;
import br.com.riverfy.service.EventService;
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

@Tag(name = "Events", description = "Endpoints for managing events and participants.")
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Create a new event", description = "Creates an event in the system and links the provided participant IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request data.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get events with pagination", description = "Retrieves a paginated list of events filtered by name, with customizable page size and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated events retrieved successfully.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Page<EventResponse>> findPaginated(
            @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
            @RequestParam(value = "status", required = false) EventStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Page<EventResponse> response = eventService.search(searchTerm, status, page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get event by ID", description = "Retrieves a specific event's details using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found successfully."),
            @ApiResponse(responseCode = "404", description = "Event not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id) {
        EventResponse response = eventService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an event", description = "Updates an existing event's details and its participant list by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request data."),
            @ApiResponse(responseCode = "404", description = "Event not found.")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an event", description = "Removes an event completely from the system based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Event not found.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
