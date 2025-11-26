package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.CategoryMenuDTO;
import java.util.List;

public interface CategoryMenuService {
    List<CategoryMenuDTO> getAll();
    CategoryMenuDTO getById(Long id);
    CategoryMenuDTO create(CategoryMenuDTO dto);
    CategoryMenuDTO update(Long id, CategoryMenuDTO dto);
    void delete(Long id);
}