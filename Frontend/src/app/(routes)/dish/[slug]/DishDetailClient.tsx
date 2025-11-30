"use client";

import React from "react";
import { useBasket } from "@/app/contexts/BasketContext";
import { MenuItemDto } from "@/app/types/menu.types";

export default function DishDetailClient({ item }: { item: MenuItemDto }) {
    const { addDish } = useBasket();

    const handleAdd = () => {
        addDish({
            id: item.id,
            title: item.name,
            price: String(item.price),
            image: item.image,
        });
    };

    return (
        <div className="mt-6">
            <button type="button" className="btn__solid" onClick={handleAdd} aria-label={`Add ${item.name} to cart`}>
                Add to cart
            </button>
        </div>
    );
}
