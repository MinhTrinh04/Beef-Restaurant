"use client";
import React, {
	useState,
	useEffect,
	useCallback,
	useRef,
	useMemo,
} from "react";
import BlockTitle from "../../../../components/common/block-title/block-title";
import MasonryItemGallery from "./MasonryItemGallery";
import { LightBox } from "../../../../components/common/lightbox";
import {
	MasonryGalleryProps,
	GalleryItem,
	Alignment,
} from "@/app/types/common.types";
import throttle from 'lodash.throttle';

const RESIZE_THROTTLE_LIMIT = 200; // Throttle limit in ms

/**
 * Renders a masonry-style photo gallery.
 * This component uses the 'masonry-layout' library to create a dynamic grid.
 * It also integrates with a LightBox component to display images in a modal view.
 *
 * @param {MasonryGalleryProps} props - The props for the component.
 * @returns {React.ReactElement | null} The rendered masonry gallery or null if no items are provided.
 */
const MasonryGallery: React.FC<MasonryGalleryProps> = ({
	subtitle,
	title,
	phrase,
	align,
	divider,
	items = [],
	emptyMessage = "No gallery items available.",
	lightboxOpenLabel = "Open lightbox",
}) => {
	const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
	const masonryRef = useRef<HTMLDivElement>(null);
	const masonryInstance = useRef<any | null>(null);

	/**
	 * Opens the lightbox with the selected image's index.
	 * @param {number} index - The index of the item to display.
	 */
	const handleOpenLightbox = (index: number) => {
		setSelectedIndex(index);
	};

	/**
	 * Closes the lightbox by resetting the selected index.
	 */
	const handleCloseLightbox = () => {
		setSelectedIndex(null);
	};

	/**
	 * Initializes the Masonry layout instance.
	 * This effect runs once on mount. It dynamically imports Masonry,
	 * creates an instance, and attaches it to the grid container.
	 * It also handles cleaning up the instance on unmount.
	 */
	useEffect(() => {
		const initMasonry = async () => {
			try {
				if (typeof window !== "undefined" && masonryRef.current) {
					const Masonry = (await import("masonry-layout")).default;
					const getGutter = () => {
						if (window.innerWidth < 640) return 16;
						if (window.innerWidth < 1024) return 20;
						return 24;
					};
					masonryInstance.current = new Masonry(masonryRef.current, {
						itemSelector: ".masonry-item",
						columnWidth: ".masonry-grid-sizer",
						percentPosition: true,
						gutter: getGutter(),
						transitionDuration: "0.3s",
					});
				}
			} catch (err) {
				console.error("Failed to initialize Masonry", err);
			}
		};

		initMasonry();

		/**
		 * Cleanup function to destroy the Masonry instance when the component unmounts.
		 */
		return () => {
			if (masonryInstance.current) {
				masonryInstance.current.destroy();
				masonryInstance.current = null;
			}
		};
	}, []);

	/**
	 * A callback that re-layouts the masonry grid.
	 * This is passed to each gallery item and called when an image finishes loading.
	 */
	const handleImageLoad = useCallback(() => {
		if (masonryInstance.current) {
			masonryInstance.current.layout();
		}
	}, []);

	/**
	 * Handles window resize events to re-calculate gutter and re-layout the grid.
	 */
	useEffect(() => {
		const handleResize = () => {
			if (masonryInstance.current?.options) {
				const getGutter = () => {
					if (window.innerWidth < 640) return 16;
					if (window.innerWidth < 1024) return 20;
					return 24;
				};
				masonryInstance.current.options.gutter = getGutter();
				masonryInstance.current.layout();
			}
		};

		const throttledResizeHandler = throttle(handleResize, RESIZE_THROTTLE_LIMIT);

		window.addEventListener("resize", throttledResizeHandler);
		return () => window.removeEventListener("resize", throttledResizeHandler);
	}, []);

	/**
	 * Memoized list of gallery items.
	 * This prevents re-rendering of all items if the props don't change.
	 */
	const galleryItems = useMemo(
		() =>
			items.map((item, index) => (
				<MasonryItemGallery
					key={item.id}
					id={item.id}
					title={item.title}
					subtitle={item.subtitle}
					image={item.image}
					onClick={() => handleOpenLightbox(index)}
					onLoad={handleImageLoad}
					lightboxOpenLabel={lightboxOpenLabel}
				/>
			)),
		[items, handleImageLoad, lightboxOpenLabel]
	);

	if (items.length === 0) {
		return (
			<div className="special-offers special-offers--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className={`masonry-gallery-block w-full`}>
			<div className="masonry-gallery-block__container w-full max-w-[1920px] mx-auto px-4">
				<BlockTitle
					subtitle={subtitle}
					title={title || ""}
					phrase={phrase}
					align={align as Alignment}
					divider={divider}
				/>
				<div
					className={`masonry-gallery-wrapper`}
					ref={masonryRef}
					role="list"
					aria-label="Masonry gallery"
				>
					<div className="masonry-grid-sizer"></div>
					{galleryItems}
				</div>
				{selectedIndex !== null && (
					<LightBox
						items={items}
						startIndex={selectedIndex}
						onClose={handleCloseLightbox}
					/>
				)}
			</div>
		</div>
	);
};

export default MasonryGallery;
