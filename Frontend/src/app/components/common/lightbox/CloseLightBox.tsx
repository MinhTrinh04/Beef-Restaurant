import React from "react";
import { CloseButtonProps } from "@/app/types/common.types";
import { galleryData } from "@/app/hooks/data-gallery";

const CloseLightBox: React.FC<CloseButtonProps> = ({
	onClose,
	ariaLabel = galleryData.lightboxCloseAriaLabel,
}) => {
	return (
		<button onClick={onClose} className="lightbox-close" aria-label={ariaLabel}>
			×
		</button>
	);
};

export default CloseLightBox;