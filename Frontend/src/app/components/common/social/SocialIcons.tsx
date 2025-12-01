'use client';

import Link from "next/link";
import React from "react";
import { FaFacebookF, FaXTwitter, FaInstagram } from "react-icons/fa6";
import { SocialIconsProps } from "@/app/types/common.types";

// Map string identifiers to React components
const iconMap: Record<string, React.ComponentType> = {
	FaFacebookF,
	FaXTwitter,
	FaInstagram,
};

const SocialIcons: React.FC<SocialIconsProps> = ({ socials }) => {
	// If there are no social links, don't render the component
	if (!socials || socials.length === 0) {
		return null;
	}

	return (
		<ul className="social_icons">
			{socials.map((item) => {
				// Handle both string identifiers and React components for backward compatibility
				const IconComponent = typeof item.icon === 'string' 
					? iconMap[item.icon] 
					: item.icon;
				
				if (!IconComponent) {
					console.warn(`Icon not found: ${item.icon}`);
					return null;
				}

				return (
					<li key={item.href}>
						<Link
							href={item.href}
							target="_blank"
							aria-label={item.ariaLabel}
						>
							<IconComponent />
						</Link>
					</li>
				);
			})}
		</ul>
	);
};

export default SocialIcons;
