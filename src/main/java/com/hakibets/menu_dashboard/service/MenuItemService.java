package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.MenuItemDTO;
import java.util.List;

public interface MenuItemService {
    List<MenuItemDTO> getAll();
    MenuItemDTO getById(Long id);
    MenuItemDTO create(MenuItemDTO dto);
    MenuItemDTO update(Long id, MenuItemDTO dto);
    void delete(Long id);
}