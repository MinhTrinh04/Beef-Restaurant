import Link from "next/link";
import React from "react";
import { ContactInfoProps } from "@/app/types/common.types";

/**
 * Renders a contact information item with an icon, text, and a link.
 * It's a reusable component for displaying phone numbers, emails, addresses, etc.
 *
 * @param {ContactInfoProps} props The props for the component.
 * @returns {React.ReactElement} The rendered contact info item.
 */
const ContactInfo: React.FC<ContactInfoProps> = ({
	className,
	href,
	icon,
	text,
	ariaLabel,
}) => {
	// Early return if essential props are missing
	if (!href || !icon || !text) {
		return null;
	}

	return (
		<div className={`contact-info ${className}`}>
			<Link href={href} aria-label={ariaLabel || text}>
				<i className={icon} aria-hidden="true"></i>
				<span>{text}</span>
			</Link>
		</div>
	);
};

export default ContactInfo; 