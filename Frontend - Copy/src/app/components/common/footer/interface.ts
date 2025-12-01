// src/app/components/common/footer/interface.ts

import { ReactNode } from "react";

/**
 * Interface for the Footer component props
 */
export interface FooterProps {
	/** Additional CSS classes for the footer */
	className?: string;
	/** Additional inline styles for the footer */
	style?: React.CSSProperties;
	/** Whether to show social media icons */
	showSocialIcons?: boolean;
	/** Whether to show legal links */
	showLegalLinks?: boolean;
	/** Whether to show scroll to top button */
	showScrollTop?: boolean;
	/** Custom content to be rendered in the footer */
	children?: ReactNode;
}

/**
 * Interface for the contact information
 */
export interface ContactInfo {
	/** Phone number */
	phone: string;
	/** Fax number */
	fax: string;
	/** Email address for reservations */
	emailReservations: string;
	/** Physical address */
	address: string;
	/** City name */
	city: string;
	/** ZIP/Postal code */
	zip: string;
	/** Country name */
	largeCountry: string;
}





/**
 * Interface for the general information data
 */
export interface GeneralInfo {
	/** URL of the logo image */
	urlLogo: string;
	/** Contact information */
	contact: ContactInfo;
	/** Copyright text */
	copyright: string;
}

/**
 * Interface for the scroll to top button props
 */
export interface ScrollToTopProps {
	/** Whether the button is visible */
	isVisible: boolean;
	/** Function to handle scroll to top */
	onScrollToTop: () => void;
}

/**
 * Interface for the footer section props
 */
export interface FooterSectionProps {
	/** Title of the section */
	title?: string;
	/** Content of the section */
	children: ReactNode;
	/** Additional CSS classes */
	className?: string;
}
