package com.hakibets.menu_dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_game_items")
@Data
public class MenuGameItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_name", nullable = false)
    private String name;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "priority", nullable = true)
    private Integer priority;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "route", nullable = true)
    private String route;

    @Column(name = "created_at", nullable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_menu_id", nullable = true)
    private CategoryMenu categoryMenu;

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = true)
    private MenuItem menuItem;

    public MenuGameItem() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}