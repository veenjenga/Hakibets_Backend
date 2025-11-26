package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.MenuItemDTO;
import com.hakibets.menu_dashboard.entity.CategoryMenu;
import com.hakibets.menu_dashboard.entity.MenuItem;
import com.hakibets.menu_dashboard.exception.ResourceNotFoundException;
import com.hakibets.menu_dashboard.repository.CategoryMenuRepository;
import com.hakibets.menu_dashboard.repository.MenuItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository repository;
    private final CategoryMenuRepository categoryMenuRepository;

    public MenuItemServiceImpl(MenuItemRepository repository, CategoryMenuRepository categoryMenuRepository) {
        this.repository = repository;
        this.categoryMenuRepository = categoryMenuRepository;
    }

    @Override
    public List<MenuItemDTO> getAll() {
        return repository.findAll(Sort.by("priority").ascending()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public MenuItemDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
    }

    @Override
    public MenuItemDTO create(MenuItemDTO dto) {
        MenuItem entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public MenuItemDTO update(Long id, MenuItemDTO dto) {
        MenuItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        BeanUtils.copyProperties(dto, entity, "id", "createdAt", "updatedAt", "categoryMenu");
        if (dto.getCategory_menu_id() != null) {
            CategoryMenu categoryMenu = categoryMenuRepository.findById(dto.getCategory_menu_id())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + dto.getCategory_menu_id()));
            entity.setCategoryMenu(categoryMenu);
        }
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        MenuItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        repository.delete(entity);
    }

    private MenuItemDTO toDTO(MenuItem entity) {
        MenuItemDTO dto = new MenuItemDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setCreatedDate(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedDate(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        if (entity.getCategoryMenu() != null) dto.setCategory_menu_id(entity.getCategoryMenu().getId());
        return dto;
    }

    private MenuItem toEntity(MenuItemDTO dto) {
        MenuItem entity = new MenuItem();
        BeanUtils.copyProperties(dto, entity, "categoryMenuId");
        if (dto.getCreatedDate() != null) {
            entity.setCreatedAt(LocalDateTime.parse(dto.getCreatedDate()));
        }
        if (dto.getUpdatedDate() != null) {
            entity.setUpdatedAt(LocalDateTime.parse(dto.getUpdatedDate()));
        }
        if (dto.getCategory_menu_id() != null) {
            CategoryMenu categoryMenu = categoryMenuRepository.findById(dto.getCategory_menu_id())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + dto.getCategory_menu_id()));
            entity.setCategoryMenu(categoryMenu);
        }
        return entity;
    }
}