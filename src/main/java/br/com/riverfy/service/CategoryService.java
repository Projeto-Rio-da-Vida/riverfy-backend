package br.com.riverfy.service;

import br.com.riverfy.dto.category.CategoryRequest;
import br.com.riverfy.dto.category.CategoryResponse;
import br.com.riverfy.exception.BadRequestException;
import br.com.riverfy.exception.ResourceNotFoundException;
import br.com.riverfy.model.Category;
import br.com.riverfy.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Category category = request.toEntity();
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> search(String searchTerm, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoryPage = categoryRepository.searchActiveCategories(searchTerm, pageable);

        return categoryPage.map(CategoryResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        if (id == 1) {
            throw new BadRequestException("The default category (ID 1) cannot be modified.");
        }

        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        request.updateEntity(category);
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updatedCategory);
    }

    @Transactional
    public void delete(Long id) {
        if (id == 1) {
            throw new BadRequestException("The default category (ID 1) cannot be deleted or deactivated.");
        }

        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        category.setActive(false);
    }
}
