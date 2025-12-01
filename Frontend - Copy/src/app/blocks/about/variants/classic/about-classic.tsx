"use client";
import Image from "next/image";
import React from "react";
import BlockTitle from "@/app/components/common/block-title/block-title";
import OpeningHours from "@/app/components/common/opening-hours/opening-hours";

import { AboutBlockProps, Alignment } from "@/app/types/common.types";

const AboutClassicBlock = ({
	imageUrl,
	altText,
	descriptionParagraphs,
	onImageClick,
	imageWidth,
	imageHeight,
	priorityImage,
	imageQuality,
	imageFill,
	onImageLoad,
	imageClassName,
	subtitle,
	title,
	divider,
	align,
	showOpeningHours,
	openingHours,
}: AboutBlockProps) => {
	if (
		!imageUrl ||
		!altText ||
		!descriptionParagraphs ||
		descriptionParagraphs.length === 0
	) {
		return (
			<div className="about__image-info about__image-info--empty">
				<p>No data available.</p>
			</div>
		);
	}

	return (
		<div
			className="about__image-info"
			role="region"
			aria-label="About Image Info"
			aria-describedby="about-description"
		>
			<div className="about-content">
				<div
					className={`about-content__image`}
					onClick={onImageClick}
					role={onImageClick ? "button" : undefined}
					tabIndex={onImageClick ? 0 : undefined}
				>
					<Image
						src={imageUrl}
						alt={altText}
						width={imageWidth || 809}
						height={imageHeight || 1011}
						priority={priorityImage}
						quality={imageQuality || 75}
						fill={imageFill}
						onLoad={onImageLoad}
						className={`about-content__image-img ${
							imageClassName || ""
						}`}
						sizes="(max-width: 768px) 100vw, (max-width: 1200px) 80vw, 70vw"
					/>
				</div>
				<div
					className={`about-content__info`}
					id="about-description"
				>
					<BlockTitle
						subtitle={subtitle}
						title={title || ""}
						divider={divider}
						align={align as Alignment}
						aria-level="2"
					/>
					<div className="about-content__description">
						{descriptionParagraphs.map((paragraph, index) => (
							<p key={index}>{paragraph}</p>
						))}
					</div>
					{showOpeningHours && openingHours && (
						<OpeningHours
							title={openingHours.title}
							schedule={openingHours.schedule}
						/>
					)}
				</div>
			</div>
		</div>
	);
};

AboutClassicBlock.displayName = "AboutClassicBlock";

export default AboutClassicBlock;
