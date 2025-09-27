import Link from "next/link";
import React from "react";
import { SocialIconsProps } from "@/app/types/common.types";

const SocialIcons: React.FC<SocialIconsProps> = ({ socials }) => {
	// If there are no social links, don't render the component
	if (!socials || socials.length === 0) {
		return null;
	}

	return (
		<ul className="social_icons">
			{socials.map((item) => {
				const IconComponent = item.icon;
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
