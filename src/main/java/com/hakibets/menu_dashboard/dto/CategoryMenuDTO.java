package com.hakibets.menu_dashboard.dto;

import lombok.Data;

@Data
public class CategoryMenuDTO {
    private Long id;
    private String name;
    private Integer priority;
    private String status;
    private String createdDate;
    private String updatedDate;
}