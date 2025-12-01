import Image from "next/image";
import Link from "next/link";
import React from "react";
import BlockTitle from "@/app/components/common/block-title/block-title";

// interface
import { AboutBlockProps } from "@/app/types/common.types";

const AboutModernBlock = ({
	imageLeftUrl,
	imageRightUrl,
	altText,
	descriptionParagraphs,
	subtitle,
	title,
	divider,
	align,
	buttonLink,
	buttonText
}: AboutBlockProps) => {
	if (
		!imageLeftUrl ||
		!imageRightUrl ||
		!altText ||
		!descriptionParagraphs ||
		descriptionParagraphs.length === 0
	) {
		return (
			<div className={`about-s2 about-s2--empty`}>
				<p>No data available.</p>
			</div>
		);
	}

	return (
		<div className={`about-s2 `} aria-label="About Us">
			<div className="about-s2__container-fluid">
				<div className="about-s2__content">
					{/* About Left Image */}
					<div className={`about-s2__image `}>
						<Image
							src={imageLeftUrl}
							alt={altText}
							width={809}
							height={1011}
							className="about-s2__image-img"
						/>
					</div>
					{/* Text Content */}
					<div className={`about-s2__text`}>
						<BlockTitle
							subtitle={subtitle}
							title={title || ""}
							divider={divider}
							align={align || "left"}
						/>
						<div className="about-content__description">
							{descriptionParagraphs.map(
								(paragraph, index) => (
									<p key={index}>{paragraph}</p>
								)
							)}
						</div>
						<Link
							href={buttonLink || "#"}
							className="btn btn__link"
							aria-label={buttonText}
						>
							{buttonText}
						</Link>
					</div>
					{/* About Right Image */}
					<div className={`about-s2__image`}>
						<Image
							src={imageRightUrl}
							alt={altText}
							width={809}
							height={1011}
							className="about-s2__image-img"
						/>
					</div>
				</div>
			</div>
		</div>
	);
};

AboutModernBlock.displayName = "AboutModernBlock";
export default AboutModernBlock;
