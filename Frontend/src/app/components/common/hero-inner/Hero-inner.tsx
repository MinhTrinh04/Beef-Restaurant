import Image from "next/image";
import React from "react";
import Breadcrumbs from "../breadcrumbs/Breadcrumbs";
import { HeroInnerBlockProps } from "@/app/types/common.types";

const HeroInnerBlock: React.FC<HeroInnerBlockProps> = ({
	title,
	image,
	altText,
	breadcrumbs,
}) => {
	return (
		<div className="hero-inner">
			{/* Background */}
			<div className="hero-inner__background">
				<Image src={image} alt={altText} width={1620} height={880} />
			</div>
			{/* Background */}
			{/* Content */}
			<div className="hero-inner__container">
				<div className="hero-inner__content">
					<h1 className="hero-inner__title">{title}</h1>
					<div className="hero-inner__breadcrumbs">
						<Breadcrumbs items={breadcrumbs} />
					</div>
				</div>
			</div>
			{/* Content */}
		</div>
	);
};

export default HeroInnerBlock;
