package com.hakibets.menu_dashboard.repository;

import com.hakibets.menu_dashboard.entity.MenuGameItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGameItemRepository extends JpaRepository<MenuGameItem, Long> {
}
