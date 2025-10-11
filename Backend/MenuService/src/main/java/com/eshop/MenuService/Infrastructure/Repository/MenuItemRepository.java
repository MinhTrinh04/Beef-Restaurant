package com.eshop.MenuService.Infrastructure.Repository;


import com.eshop.MenuService.Model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    //Select * from menu_items where menu_category = ?
    Optional<List<MenuItem>> findByMenuCategory(Integer categoryid);

    Optional<MenuItem> findBySlug(String slug);

    Optional<MenuItem> findById(Integer id);
}
