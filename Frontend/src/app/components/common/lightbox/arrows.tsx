import React from "react";
import { ArrowsProps } from "@/app/types/common.types";
import { galleryData } from "@/app/hooks/data-gallery";

const Arrows: React.FC<ArrowsProps> = ({
	onClickPrev,
	onClickNext,
	prevAriaLabel = galleryData.lightboxPrevAriaLabel,
	nextAriaLabel = galleryData.lightboxNextAriaLabel,
}) => {
	return (
		<div className="lightbox-controls">
			<button
				onClick={onClickPrev}
				className="lightbox-arrows lightbox-prev"
				aria-label={prevAriaLabel}
			>
				<svg
					xmlns="http://www.w3.org/2000/svg"
					id="Outline"
					viewBox="0 0 24 24"
					width="20"
					height="20"
				>
					<path d="M17.17,24a1,1,0,0,1-.71-.29L8.29,15.54a5,5,0,0,1,0-7.08L16.46.29a1,1,0,1,1,1.42,1.42L9.71,9.88a3,3,0,0,0,0,4.24l8.17,8.17a1,1,0,0,1,0,1.42A1,1,0,0,1,17.17,24Z" />
				</svg>
			</button>
			<button
				onClick={onClickNext}
				className="lightbox-arrows lightbox-next"
				aria-label={nextAriaLabel}
			>
				<svg
					xmlns="http://www.w3.org/2000/svg"
					id="Outline"
					viewBox="0 0 24 24"
					width="20"
					height="20"
				>
					<path d="M7,24a1,1,0,0,1-.71-.29,1,1,0,0,1,0-1.42l8.17-8.17a3,3,0,0,0,0-4.24L6.29,1.71A1,1,0,0,1,7.71.29l8.17,8.17a5,5,0,0,1,0,7.08L7.71,23.71A1,1,0,0,1,7,24Z" />
				</svg>
			</button>
		</div>
	);
};

export default Arrows;
