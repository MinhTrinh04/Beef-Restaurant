"use client";

import React, { useState, useEffect } from "react";
import { cn } from "@/lib/utils";
import { LightBoxProps, GalleryItem } from "@/app/types/common.types";
import ItemGalleryLightbox from "./ItemGalleryLightbox";
import CloseLightBox from "./CloseLightBox";
import ImageCounter from "./ImageCounter";
import Arrows from "./arrows";

/**
 * A modal component for displaying a gallery of images.
 * It is a controlled component, relying on the parent to manage its open/closed state.
 *
 * @param {LightBoxProps} props - The props for the component.
 * @returns {React.ReactElement} The rendered lightbox component.
 */
const LightBox: React.FC<LightBoxProps> = ({
	items,
	startIndex,
	onClose,
}) => {
	const [currentIndex, setCurrentIndex] = useState(startIndex);

	/**
	 * Effect to handle keyboard events for accessibility.
	 * Listens for the "Escape" key to close the lightbox.
	 */
	useEffect(() => {
		const handleKeyDown = (event: KeyboardEvent) => {
			if (event.key === "Escape") {
				onClose();
			}
		};
		document.addEventListener("keydown", handleKeyDown);
		return () => {
			document.removeEventListener("keydown", handleKeyDown);
		};
	}, [onClose]);

	/**
	 * Navigates to the previous image in the gallery.
	 * Wraps around to the last image if at the beginning.
	 */
	const handlePrev = () => {
		setCurrentIndex((prevIndex) =>
			prevIndex === 0 ? items.length - 1 : prevIndex - 1
		);
	};

	/**
	 * Navigates to the next image in the gallery.
	 * Wraps around to the first image if at the end.
	 */
	const handleNext = () => {
		setCurrentIndex((prevIndex) =>
			prevIndex === items.length - 1 ? 0 : prevIndex + 1
		);
	};

	/**
	 * Closes the lightbox if a click occurs on the backdrop overlay.
	 * @param {React.MouseEvent<HTMLDivElement>} event - The mouse event.
	 */
	const handleClickOutside = (event: React.MouseEvent<HTMLDivElement>) => {
		if (event.target === event.currentTarget) {
			onClose();
		}
	};

	// Get the currently active item based on currentIndex
	const activeItem = items[currentIndex];

	return (
		<div
			className={cn("lightbox open")}
			onClick={handleClickOutside}
			role="dialog"
			aria-modal="true"
			aria-label="Image gallery lightbox"
		>
			<div className="lightbox-wrapper">
				<div className="lightbox-content">
					{activeItem && (
						<ItemGalleryLightbox
							key={activeItem.id}
							id={activeItem.id}
							title={activeItem.title}
							image={activeItem.image}
						/>
					)}
				</div>
				<Arrows onClickPrev={handlePrev} onClickNext={handleNext} />
				<ImageCounter current={currentIndex + 1} total={items.length} />
				<CloseLightBox onClose={onClose} />
			</div>
		</div>
	);
};

export default LightBox;
