package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.CategoryMenuDTO;
import com.hakibets.menu_dashboard.entity.CategoryMenu;
import com.hakibets.menu_dashboard.exception.ResourceNotFoundException;
import com.hakibets.menu_dashboard.repository.CategoryMenuRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryMenuServiceImpl implements CategoryMenuService {
    private final CategoryMenuRepository repository;

    public CategoryMenuServiceImpl(CategoryMenuRepository repository) {
        this.repository = repository;
    }

    /*.stream() turns the list into a flow for processing.
    .map(this::toDTO) transforms each entity into a CategoryMenuDTO using the toDTO method
    .collect(Collectors.toList()) gathers the results into a new list to return.
     */
    @Override
    public List<CategoryMenuDTO> getAll() {
        return repository.findAll(Sort.by("priority").ascending()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryMenuDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + id));
    }

    @Override
    public CategoryMenuDTO create(CategoryMenuDTO dto) {
        CategoryMenu entity = toEntity(dto);
        entity.setCreatedAt(LocalDate.now().atStartOfDay());
        entity.setUpdatedAt(LocalDateTime.now());
        return toDTO(repository.save(entity));
    }

    @Override
    public CategoryMenuDTO update(Long id, CategoryMenuDTO dto) {
        CategoryMenu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + id));
        BeanUtils.copyProperties(dto, entity, "id", "createdAt", "updatedAt");//Copies data from the DTO to the entity, skipping id, createdAt, and updatedAt to preserve original values.
        entity.setUpdatedAt(LocalDateTime.now());
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CategoryMenu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + id));
        repository.delete(entity);
    }

    private CategoryMenuDTO toDTO(CategoryMenu entity) {
        CategoryMenuDTO dto = new CategoryMenuDTO();
        BeanUtils.copyProperties(entity, dto);

        dto.setCreatedDate(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedDate(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        return dto;
    }

    private CategoryMenu toEntity(CategoryMenuDTO dto) {
        CategoryMenu entity = new CategoryMenu();
        BeanUtils.copyProperties(dto, entity);

        if (dto.getCreatedDate() != null) {
            entity.setCreatedAt(LocalDateTime.parse(dto.getCreatedDate()));
        }
        if (dto.getUpdatedDate() != null) {
            entity.setUpdatedAt(LocalDateTime.parse(dto.getUpdatedDate()));
        }
        return entity;
    }
}