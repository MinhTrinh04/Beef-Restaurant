'use client';

import BlockTitle from "@/app/components/common/block-title/block-title";
import DishItem from "@/app/components/common/dish/DishItem";
import { MenuCategory } from "@/app/types/common.types";
import Image from "next/image";
import React from "react";
import { useBasket } from "@/app/contexts/BasketContext";

const CardMenuImage = ({
	image,
	altText,
	tag,
	title,
	phrase,
	dishesList,
}: MenuCategory) => {
	const { addDish } = useBasket();

	return (
		<div className="menu__category">
			{/* Category image */}
			<div className="menu__category-image">
				<Image
					src={image}
					alt={altText || ""}
					fill
					className="object-cover"
				/>
			</div>
			{/* Category image */}
			{/* Category dishes */}
			<div className="menu__category-dishes">
				{/* Section title */}
				<BlockTitle
					subtitle={tag}
					title={title}
					phrase={phrase}
					divider={false}
					align="left"
				/>
				{/* Section title */}
				{/* List Item dishes */}
                {dishesList.map((item) => (
					<DishItem
						key={item.id}
						title={item.title}
						price={item.price}
                        description={item.description}
                        onAddToCart={() => addDish({ id: item.id, title: item.title, price: item.price })}
					/>
				))}
				{/*/ List Item dishes */}
			</div>
			{/* Category dishes */}
		</div>
	);
};

export default CardMenuImage;
