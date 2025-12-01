"use client";
import React from "react";
import Image from "next/image";

// interfaces
import { ItemGalleryProps } from "@/app/types/common.types";

const ItemGallery = ({
	id,
	title,
	subtitle,
	image,
	altText,
	onClick,
}: ItemGalleryProps) => {
	return (
		<>
			{/* Item Gallery */}
			<div className="gallery-block__item" data-lightbox>
				<div className="gallery-block__item-image">
					{/* Image */}
					<Image
						src={image}
						alt={altText ?? ""}
						width={382}
						height={360}
					/>
					{/* / Image */}

					{/* Icon */}
					<div
						className="gallery-block__item-icon"
						role="button"
						onClick={onClick}
					>
						<i className="fa-solid fa-plus" aria-hidden="true"></i>
					</div>
					{/* / Icon */}
				</div>

				{/* Info */}
				<div className="gallery-block__item-info">
					<h4>{title}</h4>
					<p>{subtitle}</p>
				</div>
				{/* / Info */}
			</div>
			{/* Item Gallery */}
		</>
	);
};

export default ItemGallery;
