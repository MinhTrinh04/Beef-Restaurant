import React from "react";
import MenuSidebar from "@/app/blocks/menu/menu-sidebar/MenuSidebar";
import { getMenuCategories } from "@/app/services/menu";

const PageMenu = async () => {
    // Fetch real menu data from backend
    const menuCategories = await getMenuCategories();

    return (
        <MenuSidebar categories={menuCategories} />
    );
};

export default PageMenu;
