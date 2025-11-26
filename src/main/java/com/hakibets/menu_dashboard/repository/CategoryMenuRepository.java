package com.hakibets.menu_dashboard.repository;

import com.hakibets.menu_dashboard.entity.CategoryMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMenuRepository extends JpaRepository<CategoryMenu, Long> {
}
