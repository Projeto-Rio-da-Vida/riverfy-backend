package br.com.riverfy.service;

import br.com.riverfy.dto.notice.NoticeRequest;
import br.com.riverfy.dto.notice.NoticeResponse;
import br.com.riverfy.exception.ResourceNotFoundException;
import br.com.riverfy.model.Notice;
import br.com.riverfy.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public NoticeResponse create(NoticeRequest request) {
        Notice notice = request.toEntity();
        Notice savedNotice = noticeRepository.save(notice);
        return NoticeResponse.fromEntity(savedNotice);
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponse> search(String searchTerm, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Notice> noticePage = noticeRepository.searchActiveNotices(searchTerm, pageable);

        return noticePage.map(NoticeResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public NoticeResponse findById(Long id) {
        Notice notice = noticeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with ID: " + id));
        return NoticeResponse.fromEntity(notice);
    }

    @Transactional
    public NoticeResponse update(Long id, NoticeRequest request) {
        Notice notice = noticeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with ID: " + id));

        request.updateEntity(notice);

        Notice updatedNotice = noticeRepository.save(notice);
        return NoticeResponse.fromEntity(updatedNotice);
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found with ID: " + id));

        notice.setActive(false);
    }

    public long countNotice() {
        return noticeRepository.count();
    }
}
