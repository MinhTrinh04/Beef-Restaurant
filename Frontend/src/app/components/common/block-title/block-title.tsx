import { cn } from "@/lib/utils";
import Divider from "../divider/divider";
import React from "react";
import { Alignment, BlockTitleProps } from "@/app/types/common.types";

const BlockTitle: React.FC<BlockTitleProps> = ({
	subtitle,
	title,
	phrase,
	align = "center",
	divider = false,
	className,
}) => {
	// If there's no content to display, don't render the component
	if (!subtitle && !title && !phrase) {
		return null;
	}

	const alignmentClasses: Record<Alignment, string> = {
		left: "text-left",
		center: "text-center",
		right: "text-right",
	};

	const alignClass = alignmentClasses[align] || alignmentClasses.center;

	return (
		<div
			className={cn("block-title", alignClass, className)}
			role="heading"
			aria-level={2}
		>
			{subtitle && <p className="block-title__subtitle">{subtitle}</p>}
			{title && <h2 className="block-title__title">{title}</h2>}
			{phrase && <p className="block-title__phrase">{phrase}</p>}
			{divider && <Divider />}
		</div>
	);
};

export default BlockTitle;
