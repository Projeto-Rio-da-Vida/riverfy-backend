package br.com.riverfy.service;

import br.com.riverfy.config.RabbitMQConfig;
import br.com.riverfy.dto.event.EventRequest;
import br.com.riverfy.dto.event.EventResponse;
import br.com.riverfy.exception.ResourceNotFoundException;
import br.com.riverfy.model.Event;
import br.com.riverfy.model.User;
import br.com.riverfy.model.enums.EventStatus;
import br.com.riverfy.repository.EventRepository;
import br.com.riverfy.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public EventService(EventRepository eventRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        Event event = request.toEntity();

        if (request.participantIds() != null && !request.participantIds().isEmpty()) {
            List<User> participants = userRepository.findAllById(request.participantIds());
            event.setParticipants(participants);
        }

        Event savedEvent = eventRepository.save(event);

        sendNotificationForEmailMQ(savedEvent);

        return EventResponse.fromEntity(savedEvent);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> search(String searchTerm, EventStatus status, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> eventPage = eventRepository.searchActiveEvents(searchTerm, status, pageable);

        return eventPage.map(EventResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o ID: " + id));
        return EventResponse.fromEntity(event);
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request) {
        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o ID: " + id));

        request.updateEntity(event);

        if (request.participantIds() != null) {
            List<User> participants = userRepository.findAllById(request.participantIds());
            event.setParticipants(participants);
        }

        Event updatedEvent = eventRepository.save(event);
        return EventResponse.fromEntity(updatedEvent);
    }

    @Transactional
    public void delete(Long id) {
        Event event = eventRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o ID: " + id));

        event.setActive(false);
    }

    public long countEvent() {
        return eventRepository.count();
    }

    public List<EventResponse> getUpcomingEvents() {

        return eventRepository
                .findByActiveTrueAndDateGreaterThanEqualOrderByDateAsc(LocalDateTime.now())
                .stream()
                .map(EventResponse::fromEntity)
                .toList();
    }

    public Event getNextEvent() {

        return eventRepository.findNextEvent(
                        LocalDateTime.now(),
                        PageRequest.of(0, 1)
                ).stream()
                .findFirst()
                .orElse(null);
    }

    public List<Event> getTodayEvents() {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return eventRepository.findEventsToday(startOfDay, endOfDay);
    }

    @Transactional
    public EventResponse confirmAttendance(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndActiveTrue(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
            eventRepository.save(event);
        }

        return EventResponse.fromEntity(event);
    }

    private void sendNotificationForEmailMQ(Event savedEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String formattedDate = savedEvent.getDate() != null ? savedEvent.getDate().format(formatter) : "data a definir";

        String message = String.format("O evento %s foi marcado para %s. Contamos com a sua presença!",
                savedEvent.getName(), formattedDate);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
}
