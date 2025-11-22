'use client';

import React from 'react';
import { addDishToBasket } from '@/app/services/basket';
import { MenuItemDto } from '@/app/types/menu.types';

export default function DishDetailClient({ item }: { item: MenuItemDto }) {
    const handleAdd = () => {
        addDishToBasket({ id: item.id, title: item.name, price: String(item.price) });
    };

    return (
        <div className="mt-6">
            <button
                type="button"
                className="btn__solid"
                onClick={handleAdd}
                aria-label={`Add ${item.name} to cart`}
            >
                Add to cart
            </button>
        </div>
    );
}


