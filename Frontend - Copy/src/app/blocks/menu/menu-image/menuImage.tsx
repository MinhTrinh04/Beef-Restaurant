import React from "react";
import BlockTitle from "@/app/components/common/block-title/block-title";
import CardMenuImage from "./cardMenuImage";

// Interfaces
import { MenuImageBlockProps } from "@/app/types/common.types";

const MenuImageBlock = ({
	subtitle,
	title,
	phrase,
	divider,
	items,
	className,
	emptyMessage = "No offers available at the moment.",
}: MenuImageBlockProps) => {
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="menu menu--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className="menu">
			<div className={`${className || ""}`}>
				{/* Info title */}
				<BlockTitle
					subtitle={subtitle}
					title={title}
					phrase={phrase}
					divider={divider}
				/>
				{/* Info title */}

				{/* Iterate over the 'items' prop */}
				{items.map((item) => (
					<CardMenuImage {...item} key={item.id} />
				))}
			</div>
		</div>
	);
};

MenuImageBlock.displayName = "MenuImageBlock";

export default MenuImageBlock; 
