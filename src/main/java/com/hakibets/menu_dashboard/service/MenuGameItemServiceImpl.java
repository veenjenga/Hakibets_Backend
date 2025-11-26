package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.MenuGameItemDTO;
import com.hakibets.menu_dashboard.entity.CategoryMenu;
import com.hakibets.menu_dashboard.entity.MenuGameItem;
import com.hakibets.menu_dashboard.entity.MenuItem;
import com.hakibets.menu_dashboard.exception.ResourceNotFoundException;
import com.hakibets.menu_dashboard.repository.CategoryMenuRepository;
import com.hakibets.menu_dashboard.repository.MenuGameItemRepository;
import com.hakibets.menu_dashboard.repository.MenuItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGameItemServiceImpl implements MenuGameItemService {
    private final MenuGameItemRepository repository;
    private final CategoryMenuRepository categoryMenuRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuGameItemServiceImpl(MenuGameItemRepository repository, CategoryMenuRepository categoryMenuRepository, MenuItemRepository menuItemRepository) {
        this.repository = repository;
        this.categoryMenuRepository = categoryMenuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuGameItemDTO> getAll() {
        return repository.findAll(Sort.by("priority").ascending()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public MenuGameItemDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO).orElseThrow(() -> new ResourceNotFoundException("MenuGameItem not found with id " + id));
    }

    @Override
    public MenuGameItemDTO create(MenuGameItemDTO dto) {
        MenuGameItem entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    @Override
    public MenuGameItemDTO update(Long id, MenuGameItemDTO dto) {
        MenuGameItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuGameItem not found with id " + id));
        BeanUtils.copyProperties(dto, entity, "id", "createdAt", "updatedAt", "categoryMenu", "menuItem");
        if (dto.getCategory_menu_id() != null) {
            CategoryMenu categoryMenu = categoryMenuRepository.findById(dto.getCategory_menu_id())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + dto.getCategory_menu_id()));
            entity.setCategoryMenu(categoryMenu);
        }
        if (dto.getMenu_item_id() != null) {
            MenuItem menuItem = menuItemRepository.findById(dto.getMenu_item_id())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + dto.getMenu_item_id()));
            entity.setMenuItem(menuItem);
        }
        return toDTO(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        MenuGameItem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuGameItem not found with id " + id));
        repository.delete(entity);
    }

    private MenuGameItemDTO toDTO(MenuGameItem entity) {
        MenuGameItemDTO dto = new MenuGameItemDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setCreatedDate(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedDate(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        if (entity.getCategoryMenu() != null) dto.setCategory_menu_id(entity.getCategoryMenu().getId());
        if (entity.getMenuItem() != null) dto.setMenu_item_id(entity.getMenuItem().getId());
        return dto;
    }

    private MenuGameItem toEntity(MenuGameItemDTO dto) {
        MenuGameItem entity = new MenuGameItem();
        BeanUtils.copyProperties(dto, entity, "categoryMenuId", "menuItemId");
        if (dto.getCreatedDate() != null) { // Updated field name
            entity.setCreatedAt(LocalDateTime.parse(dto.getCreatedDate()));
        }
        if (dto.getUpdatedDate() != null) { // Updated field name
            entity.setUpdatedAt(LocalDateTime.parse(dto.getUpdatedDate()));
        }
        if (dto.getCategory_menu_id() != null) {
            CategoryMenu categoryMenu = categoryMenuRepository.findById(dto.getCategory_menu_id())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMenu not found with id " + dto.getCategory_menu_id()));
            entity.setCategoryMenu(categoryMenu);
        }
        if (dto.getMenu_item_id() != null) {
            MenuItem menuItem = menuItemRepository.findById(dto.getMenu_item_id())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + dto.getMenu_item_id()));
            entity.setMenuItem(menuItem);
        }
        return entity;
    }
}