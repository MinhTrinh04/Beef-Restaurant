import React from "react";
import BlockTitle from "@/app/components/common/block-title/block-title";
import Link from "next/link";
import DishItem from "@/app/components/common/dish/DishItem";
import { WeekSpecialsProps, Alignment } from "@/app/types/common.types";

const MenuWeekSpecialsBlock = ({
	subtitle,
	title,
	phrase,
	align,
	divider,
	items,
	buttonUrl,
	buttonText,
	emptyMessage = "No week specials available at the moment.",
}: WeekSpecialsProps) => {
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="week-specials week-specials--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className="week-specials">
			<div className="week-specials__overlayer"></div>
			<div className="week-specials__container">
				{/* block title */}
				<BlockTitle
					subtitle={subtitle}
					title={title || ""}
					phrase={phrase}
					align={align as Alignment}
					divider={divider}
				/>
				{/* / Section title */}

				{/* Menu Block */}
				<div className="week-specials__dishes">
					{/* Dishes list  */}
					{items.map((item) => (
						<DishItem
							key={item.id}
							title={item.title}
							price={item.price}
							description={item.description}
						/>
					))}
					{/* / Dishes list  */}
				</div>
				{/* / Menu Block */}

				{/* Link */}
				{buttonUrl && (
					<Link href={buttonUrl} className="btn btn__default">
						{buttonText}
					</Link>
				)}
				{/* / Link */}
			</div>
		</div>
	);
};

export default MenuWeekSpecialsBlock;
