import React from "react";
import { ImageCounterProps } from "@/app/types/common.types";

const ImageCounter: React.FC<ImageCounterProps> = ({ current, total }) => {
	// Don't render if the total is zero or less.
	if (total <= 0) {
		return null;
	}

	return (
		<div className="lightbox-caption">
			{current} of {total}
		</div>
	);
};

export default ImageCounter;