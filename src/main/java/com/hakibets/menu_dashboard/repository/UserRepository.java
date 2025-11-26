package com.hakibets.menu_dashboard.repository;

import com.hakibets.menu_dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername (String username);
}
