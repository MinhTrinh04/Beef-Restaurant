"use client";
import React from "react";
import Image from "next/image";
import BlockTitle from "@/app/components/common/block-title/block-title";
import DishItem from "@/app/components/common/dish/DishItem";

import {
	MenuContentDisplayProps,
	MenuCategory,
	DishItem as DishItemType,
} from "@/app/types/common.types";

const MenuContentDisplay: React.FC<MenuContentDisplayProps> = ({
	categories,
	emptyMessage,
}) => {
	if (!categories || categories.length === 0) {
		return <p>{emptyMessage || "No menu items to display."}</p>;
	}

	return (
		<div className="navmenu-content">
			{categories.map((categoryItem: MenuCategory, categoryIndex: number) => (
				<React.Fragment key={categoryIndex}>
					<div id={categoryItem.anchor || ''} className="menu__category">
						<div className="menu__category-dishes">
							<BlockTitle
								title={categoryItem.title}
								phrase={categoryItem.phrase}
								divider={true}
							/>
							{categoryItem.dishesList.map(
								(dish: DishItemType, dishIndex: number) => (
									<DishItem
										key={dish.id ?? `dish-${categoryIndex}-${dishIndex}`}
										title={dish.title}
										price={dish.price}
										description={dish.description}
									/>
								)
							)}
						</div>
						<div className="menu__category-image">
							<Image
								src={categoryItem.image}
								alt={categoryItem.altText || ""}
								width={568}
								height={880}
							/>
						</div>
					</div>
				</React.Fragment>
			))}
		</div>
	);
};

export default MenuContentDisplay; 