"use client";
import React, { useState, useEffect, useCallback, useMemo } from "react";
import ItemGallery from "./ItemGallery";
import BlockTitle from "@/app/components/common/block-title/block-title";
import { LightBox } from "@/app/components/common/lightbox";
import { Alignment, GalleryBasicProps } from "@/app/types/common.types";
import { cn } from "@/lib/utils";

const GalleryBasic: React.FC<GalleryBasicProps> = ({
	subtitle,
	title,
	phrase,
	align,
	divider,
	items,
	className,
	listClassName,
	children,
	emptyMessage = "No offers available at the moment.",
	errorMessage = "Error loading gallery:",
}: GalleryBasicProps) => {
	const [state, setState] = useState({
		itemGalleryActive: 0,
		isClient: false,
		isLoading: true,
		error: null as Error | null,
	});

	useEffect(() => {
		try {
			setState((prevState) => ({
				...prevState,
				isClient: true,
			}));
		} catch (err) {
			setState((prevState) => ({
				...prevState,
				error: err as Error,
			}));
		}
	}, []);

	const handleItemClick = useCallback((id: number) => {
		setState((prevState) => ({
			...prevState,
			itemGalleryActive: id,
		}));
	}, []);

	const galleryItems = useMemo(
		() =>
			items?.map((item) => (
				<ItemGallery
					key={item.id}
					id={item.id}
					title={item.title}
					subtitle={item.subtitle}
					image={item.image}
					altText={item.altText}
					onClick={() => handleItemClick(item.id)}
				/>
			)) || [],
		[items, handleItemClick]
	);

	const getGridClass = () => {
		if (typeof window !== "undefined") {
			if (window.innerWidth < 640) return "grid-cols-1";
			if (window.innerWidth < 1024) return "grid-cols-2";
		}
		return "grid-cols-4";
	};

	if (state.error) {
		return (
			<div className="gallery-block gallery-block--error">
				<p>
					{errorMessage} {state.error.message}
				</p>
			</div>
		);
	}

	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="gallery-block gallery-block--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div
			className={cn("gallery-block", className)}
			role="region"
			aria-label="Gallery"
		>
			<div className="gallery-block__container">
				{/* Section title */}
				<BlockTitle
					subtitle={subtitle}
					title={title || ""}
					phrase={phrase}
					align={align as Alignment}
					divider={divider}
				/>
				{/* / Section title */}

				{/* Gallery list */}
				<div
					className={cn("gallery-block__list", listClassName, getGridClass())}
					role="list"
					aria-label="Gallery items"
				>
					{galleryItems}
				</div>
				{/* / Gallery list */}

				{/* Lightbox */}
				{state.isClient &&
					state.itemGalleryActive > 0 &&
					items && (
						<LightBox
							items={items}
							startIndex={state.itemGalleryActive - 1}
							onClose={() => handleItemClick(0)}
						/>
					)}
				{/* Lightbox End */}
				{children}
			</div>
		</div>
	);
};

GalleryBasic.displayName = "GalleryBasic";
export default GalleryBasic;
