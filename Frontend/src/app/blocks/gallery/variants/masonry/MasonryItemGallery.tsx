import React from "react";
import Image from "next/image";

// Interface
import { MasonryItemGalleryProps } from "@/app/types/common.types";

const MasonryItemGallery: React.FC<MasonryItemGalleryProps> = ({
	id,
	title,
	subtitle,
	image,
	onClick,
	onLoad,
	lightboxOpenLabel = "Open lightbox",
}) => {
	// Determine size variations based on ID
	const getItemClass = () => {
		// Every third item will be double height
		if (id % 3 === 0) {
			return "masonry-item masonry-item--double-height";
		}
		// Every fifth item will be tall
		else if (id % 5 === 0) {
			return "masonry-item masonry-item--tall";
		}
		// Default size
		return "masonry-item";
	};

	return (
		<div className={getItemClass()} data-lightbox>
			<div className="masonry-item__image">
				{/* Image */}
				<Image
					src={image}
					alt={title}
					width={400}
					height={400}
					loading="lazy"
					onLoad={onLoad}
					className="object-cover"
					sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
					quality={85}
				/>
				{/* / Image */}

				{/* Icon */}
				<button
					className="masonry-item__icon"
					onClick={onClick}
					aria-label={lightboxOpenLabel}
				>
					<i className="fa-solid fa-plus" aria-hidden="true"></i>
				</button>
				{/* / Icon */}
			</div>

			{/* Info */}
			<div className="masonry-item__info">
				<h4>{title}</h4>
				<p>{subtitle}</p>
			</div>
			{/* / Info */}
		</div>
	);
};

export default MasonryItemGallery;
