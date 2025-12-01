"use client";
import { useState, memo } from "react";
import SliderItem from "./slider-item";
import SliderThumbnailItem from "./slider-thumbnail-item";
import Dots from "./dots";

// interface
import { HeroClassicSliderProps } from "@/app/types/common.types";

const HeroCreativeSliderBlock = ({
	items,
	emptyMessage = "No hero available at the moment.",
}: HeroClassicSliderProps) => {
	// Initialize state before any conditional returns
	const [itemActive, setItemActive] = useState<number>(
		items?.[0]?.id ?? 1 // Use optional chaining for safety
	);

	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="hero-classic hero-classic--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	const countItems = items.length;

	// This check is redundant due to the improved check above, but kept for structure if needed later.
	if (!items || items.length === 0) {
		return (
			<div className={`slider-hero slider-hero--empty`}>
				<p>No data available.</p>
			</div>
		);
	}

	// Functions to handle previous/next navigation (currently unused in the template).
	const onPrev = () => {
		setItemActive((prev) =>
			prev === items[0].id ? items[countItems - 1].id : prev - 1
		);
	};

	const onNext = () => {
		setItemActive((prev) =>
			prev === items[countItems - 1].id ? items[0].id : prev + 1
		);
	};

	return (
		// Main container for the slider, enhanced with ARIA roles for accessibility.
		<div
			className={`slider-hero`}
			role="region" 
			aria-roledescription="carousel" 
			aria-label="Slider principal de héroe" 
		>
			{/* List containing the main slide items. */}
			<ul>
				{items.map((item) => (
					<SliderItem
						key={item.id} 
						itemActive={itemActive}
						id={item.id}
						image={item.image}
						subtitle={item.subtitle}
						title={item.title}
						altText={item.altText} 
						desc={item.desc}
						link={item.link}
						textLink={item.textLink}
					/>
				))}
			</ul>

			{/* List containing the thumbnail navigation items. */}
			<ul className={`slider-hero__thumbnail`}>
				{items.map((item) => (
					<SliderThumbnailItem
						key={item.id} 
						itemActive={itemActive}
						id={item.id}
						image={item.image}
						altText={item.altText} 
						onClick={() => setItemActive(item.id)}
					/>
				))}
			</ul>

			{/* List containing the dot navigation indicators. */}
			<ul className={`slider-list-dots`}>
				{items.map((item) => (
					<Dots
						key={item.id} 
						itemActive={itemActive}
						id={item.id}
						onClick={() => setItemActive(item.id)}
					/>
				))}
			</ul>
		</div>
	);
};

HeroCreativeSliderBlock.displayName = "HeroCreativeSliderBlock";
export default HeroCreativeSliderBlock;
