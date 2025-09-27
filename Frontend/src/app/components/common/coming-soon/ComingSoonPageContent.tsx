'use client';

import Image from "next/image";
import Link from "next/link";
import React from "react";
import SubscriptionForm from "@/app/components/common/forms/SubscriptionForm";
import SocialIcons from "@/app/components/common/social/SocialIcons";
import { ComingSoonPageContentProps } from "@/app/types/common.types";

/**
 * Renders the content for a "Coming Soon" page.
 * Displays a background image, a logo, a title, a descriptive text,
 * a subscription form, and social media icons.
 *
 * @param {ComingSoonPageContentProps} props The props for the component.
 * @returns {React.ReactElement | null} The rendered page content or null if essential props are missing.
 */
const ComingSoonPageContent: React.FC<ComingSoonPageContentProps> = ({
	backgroundImage,
	backgroundAltText,
	logoImage,
	logoAltText,
	title,
	text,
	socials,
	subscriptionFormData,
}) => {
	// Early return if essential props are not provided
	if (!backgroundImage || !logoImage || !title) {
		return null;
	}

	return (
		<div className="coming-soon__wrapper">
			<div className="coming-soon__container">
				<div className="coming-soon__image">
					<Image
						src={backgroundImage}
						alt={backgroundAltText}
						fill
						priority
						style={{ objectFit: "cover" }}
					/>
				</div>
				<div className="coming-soon__content" suppressHydrationWarning>
					<Link aria-current="page" className="logo" href="/">
						<Image src={logoImage} alt={logoAltText} width={250} height={150} />
					</Link>
					<h1 className="coming-soon__title">{title}</h1>
					<p className="coming-soon__text">{text}</p>
					<SubscriptionForm {...subscriptionFormData} />
					<div className="coming-soon__social">
						<SocialIcons socials={socials} />
					</div>
				</div>
			</div>
		</div>
	);
};

export default ComingSoonPageContent; 