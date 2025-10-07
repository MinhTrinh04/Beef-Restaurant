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
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name="slug")
    private String slug;

    @Column(name="image")
    private String image; //Tạm thời để String maybe later sửa sau

    @Column(name="available_stock")
    private int availableStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id", nullable = false)
    private MenuCategory menuCategory;

    public void Removestock(int quantityDesired) {
        if (this.availableStock < quantityDesired) {
            throw new IllegalArgumentException("Không đủ số lượng tồn kho cho món: " + this.name);
        }
        this.availableStock -= quantityDesired;
    }
}
