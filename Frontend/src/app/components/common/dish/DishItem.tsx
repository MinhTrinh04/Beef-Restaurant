import React, { useId } from "react";
import { DishItemProps } from "@/app/types/common.types";

const DishItem: React.FC<DishItemProps> = ({ title, price, description }) => {
	const id = useId();

	// Early return if essential data is missing
	if (!title || !price) {
		return null;
	}

	return (
		<div
			className="dish"
			role="article"
			aria-labelledby={`dish-title-${id}`}
		>
			<div className="dish__content">
				<h4 className="dish__title" id={`dish-title-${id}`}>
					{title}
				</h4>
				<span className="dish__price" aria-label={`Price: ${price}`}>
					{price}
				</span>
			</div>
			{description && (
				<div
					className="dish__description"
					aria-describedby={`dish-title-${id}`}
				>
					<span>{description}</span>
				</div>
			)}
		</div>
	);
};

export default DishItem;
