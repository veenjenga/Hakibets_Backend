package com.hakibets.menu_dashboard.service;

import com.hakibets.menu_dashboard.dto.MenuGameItemDTO;
import java.util.List;

public interface MenuGameItemService {
    List<MenuGameItemDTO> getAll();
    MenuGameItemDTO getById(Long id);
    MenuGameItemDTO create(MenuGameItemDTO dto);
    MenuGameItemDTO update(Long id, MenuGameItemDTO dto);
    void delete(Long id);
}