package br.com.riverfy.service;

import br.com.riverfy.dto.devotional.DevotionalPageRequest;
import br.com.riverfy.dto.devotional.DevotionalRequest;
import br.com.riverfy.dto.devotional.DevotionalResponse;
import br.com.riverfy.model.Devotional;
import br.com.riverfy.model.DevotionalPage;
import br.com.riverfy.model.enums.DevotionalStatus;
import br.com.riverfy.repository.DevotionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevotionalService {

    private final DevotionalRepository devotionalRepository;

    public DevotionalService(DevotionalRepository devotionalRepository) {
        this.devotionalRepository = devotionalRepository;
    }

    @Transactional
    public DevotionalResponse create(DevotionalRequest request) {
        validatePagesRules(request.pages());

        Devotional devotional = request.toEntity();

        List<DevotionalPage> pages = request.pages().stream()
                .map(pageReq -> createPageEntity(pageReq, devotional))
                .collect(Collectors.toList());

        devotional.setPages(pages);

        Devotional savedDevotional = devotionalRepository.save(devotional);
        return DevotionalResponse.fromEntity(savedDevotional);
    }

    @Transactional(readOnly = true)
    public Page<DevotionalResponse> search(String searchTerm, DevotionalStatus status, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Devotional> devotionalPage = devotionalRepository.searchActiveDevotionals(searchTerm, status, pageable);

        return devotionalPage.map(DevotionalResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public DevotionalResponse findById(Long id) {
        Devotional devotional = devotionalRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Devotional not found with ID: " + id));
        return DevotionalResponse.fromEntity(devotional);
    }

    @Transactional
    public DevotionalResponse update(Long id, DevotionalRequest request) {
        validatePagesRules(request.pages());

        Devotional devotional = devotionalRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Devotional not found with ID: " + id));

        request.updateEntity(devotional);

        devotional.getPages().clear();
        List<DevotionalPage> updatedPages = request.pages().stream()
                .map(pageReq -> createPageEntity(pageReq, devotional))
                .collect(Collectors.toList());

        devotional.getPages().addAll(updatedPages);

        Devotional updatedDevotional = devotionalRepository.save(devotional);
        return DevotionalResponse.fromEntity(updatedDevotional);
    }

    @Transactional
    public void delete(Long id) {
        Devotional devotional = devotionalRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Devotional not found with ID: " + id));

        devotional.setActive(false);
    }

    private void validatePagesRules(List<DevotionalPageRequest> pageRequests) {
        DevotionalPageRequest pageOne = pageRequests.stream()
                .filter(p -> p.pageNumber() == 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Page number 1 is required."));

        if (pageOne.title() == null || pageOne.title().isBlank()) {
            throw new IllegalArgumentException("Title is required for page 1.");
        }
    }

    private DevotionalPage createPageEntity(DevotionalPageRequest pageReq, Devotional devotional) {
        DevotionalPage page = new DevotionalPage();
        page.setTitle(pageReq.title());
        page.setText(pageReq.text());
        page.setPageNumber(pageReq.pageNumber());
        page.setDevotional(devotional);
        return page;
    }
}
