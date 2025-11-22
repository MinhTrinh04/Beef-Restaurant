'use client';

import React, { useId } from "react";
import { DishItemProps } from "@/app/types/common.types";

const DishItem: React.FC<DishItemProps> = ({ title, price, description, onAddToCart }) => {
    const id = useId();

    // Early return if essential data is missing
    if (!title || !price) {
        return null;
    }

    return (
        <div className="dish" role="article" aria-labelledby={`dish-title-${id}`}>
            <div className="dish__content">
                <h4 className="dish__title" id={`dish-title-${id}`}>
                    {title}
                </h4>
                <div className="flex items-center gap-3">
                    <span className="dish__price" aria-label={`Price: ${price}`}>
                        {price}
                    </span>
                    <button
                        type="button"
                        className="flex h-8 w-8 items-center justify-center border-2 border-primary rounded-full text-primary transition duration-300 ease-in-out hover:bg-primary hover:text-surface-dark focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50 disabled:cursor-not-allowed"
                        onClick={() => onAddToCart?.()}
                        aria-label={`Add ${title} to cart`}
                    >
                        <i className="fa-solid fa-plus" aria-hidden="true"></i>
                    </button>
                </div>
            </div>
            {description && (
                <div className="dish__description" aria-describedby={`dish-title-${id}`}>
                    <span>{description}</span>
                </div>
            )}
        </div>
    );
};

export default DishItem;
