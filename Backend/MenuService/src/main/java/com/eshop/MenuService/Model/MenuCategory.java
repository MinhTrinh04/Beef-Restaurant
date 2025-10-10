package com.eshop.MenuService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "menuCategory", fetch = FetchType.LAZY)
    private List<MenuItem> menuItems;
}
