package com.eshop.MenuService.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name="menu_items")
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private Double price;

    @Column(name="slug")
    private String slug;

    @Column(name="image")
    private String image; //Tạm thời để String maybe later sửa sau

    @Column(name="available_stock")
    private Integer availableStock;

    @Column(name="menu_category_id")
    private Integer menuCategory;

    public void Removestock(int quantityDesired) {
        if (this.availableStock < quantityDesired) {
            throw new IllegalArgumentException("Không đủ số lượng tồn kho cho món: " + this.name);
        }
        this.availableStock -= quantityDesired;
    }
}
