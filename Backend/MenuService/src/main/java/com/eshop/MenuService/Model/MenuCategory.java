package com.eshop.MenuService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "menu_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name="slug")
    private String slug;

    @Column(name="description")
    private String description;

}
