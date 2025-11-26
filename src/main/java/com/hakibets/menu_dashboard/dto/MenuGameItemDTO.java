package com.hakibets.menu_dashboard.dto;

import lombok.Data;

@Data
public class MenuGameItemDTO {
    private Long id;
    private String name;
    private Integer priority;
    private String status;
    private String icon;
    private String route;
    private String createdDate;
    private String updatedDate;
    private Long category_menu_id;
    private Long menu_item_id;
}