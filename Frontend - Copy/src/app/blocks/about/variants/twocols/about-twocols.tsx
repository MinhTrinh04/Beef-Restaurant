"use client";
import React from "react";

// interface
import { AboutBlockProps } from "@/app/types/common.types";

const AboutTwoCols = ({ descriptionParagraphsTwo, descriptionParagraphsThree }: AboutBlockProps) => {
	if (
		!descriptionParagraphsTwo ||
		!descriptionParagraphsThree ||
		descriptionParagraphsTwo.length === 0 ||
		descriptionParagraphsThree.length === 0
	) {
		return (
			<div className={`text-cols text-cols--empty `}>
				<p>No data available.</p>
			</div>
		);
	}

	return (
		<section className={`text-cols bg-background `}>
			<div className="text-cols__container">
				<div className={`text-cols__content `}>
					<div className={`text-cols__col text-cols__col--left`}>
						{descriptionParagraphsTwo.map((paragraph, index) => (
							<p key={index}>{paragraph}</p>
						))}
					</div>
					<div className={`text-cols__col`}>
						{descriptionParagraphsThree.map((paragraph, index) => (
							<p key={index}>{paragraph}</p>
						))}
					</div>
				</div>
			</div>
		</section>
	);
};

AboutTwoCols.displayName = "AboutTwoCols";
export default AboutTwoCols;
