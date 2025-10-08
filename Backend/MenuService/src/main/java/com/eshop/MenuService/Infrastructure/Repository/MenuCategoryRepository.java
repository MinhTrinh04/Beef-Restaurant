package com.eshop.MenuService.Infrastructure.Repository;

import com.eshop.MenuService.Model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Integer> {

}
