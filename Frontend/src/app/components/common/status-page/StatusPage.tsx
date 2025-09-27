import Image from "next/image";
import Link from "next/link";
import React from "react";
import { StatusPageProps } from "@/app/types/common.types";

/**
 * Renders a generic status page, typically for 404 Not Found errors.
 * It displays a background image, a main status image (like "404"),
 * a heading, a descriptive text, and a call-to-action button.
 *
 * @param {StatusPageProps} props The props for the component.
 * @returns {React.ReactElement | null} The rendered status page or null if essential data is missing.
 */
const StatusPage: React.FC<StatusPageProps> = ({
	backgroundImageSrc,
	backgroundImageAlt,
	imageSrc,
	imageAlt,
	imageWidth,
	imageHeight,
	heading,
	text,
	buttonLink,
	buttonText,
}) => {
	// Early return if essential data is missing.
	if (!backgroundImageSrc || !heading || !text || !buttonLink || !buttonText) {
		return null;
	}
	return (
		<div className="content-404__container">
			<div className="content-404__background">
				<Image
					src={backgroundImageSrc}
					alt={backgroundImageAlt}
					width={1620}
					height={560}
				/>
			</div>
			<div className="content-404__content">
				{imageSrc && imageAlt && imageWidth && imageHeight && (
				<h1 className="content-404__title">
					{imageSrc && imageAlt && imageWidth && imageHeight && (
						<Image
							src={imageSrc}
							alt={imageAlt}
							width={imageWidth}
							height={imageHeight}
						/>
					)}
				</h1>
				)}
				<h2>{heading}</h2>
				<p>{text}</p>
				<Link href={buttonLink} className="btn btn__default">
					{buttonText}
				</Link>
			</div>
		</div>
	);
};

export default StatusPage; 