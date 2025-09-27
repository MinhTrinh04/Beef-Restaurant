import { FaFacebookF, FaXTwitter, FaInstagram } from "react-icons/fa6";
import { StatusPageProps } from "@/app/types/common.types";

export const InfoGeneralData = {
	urlLogo: "/logo.png",
	urlMap: "https://www.google.com/maps",
	phone: "+1 123 456 7890",
	fax: "+1 123 456 7890",
	emailInfo: "info@beef.com",
	emailReservations: "reservations@beef.com",
	address: "Restaurant St, Delicious City",
	city: "London",
	zip: "9578",
	country: "ENG",
	largeCountry: "England",
	phoneLabel: "Phone:",
	faxLabel: "Fax:",
	copyright: "© 2025 Beef Restaurant.",
	scrollToTopLabel: "Go to top",
	scrollToTopIconClasses: "fas fa-angle-double-up",
	menuToggleAriaLabelOpen: "Open menu",
	menuToggleAriaLabelClose: "Close menu",
	topbar: {
		addressIcon: "fa-solid fa-location-dot",
		addressAriaLabel: "Location",
		phoneIcon: "fa-solid fa-phone",
		phoneAriaLabel: "Call us at",
		emailIcon: "fa-solid fa-envelope",
		emailAriaLabel: "Email us at",
	},
};

export const LogoData={
	urlLogo: "/logo.png",
	altLogo: "Logo",
	hrefLogo: "/",
	widthLogo: 120,
	heightLogo: 120,
}

export const menuLegalData = [
	{
		id: 1,
		title: "Privacy Policy",
		link: "/",
	},
	{
		id: 2,
		title: "Spam Policy",
		link: "/",
	},
	{
		id: 3,
		title: "Terms & Conditions",
		link: "/",
	},
	{
		id: 4,
		title: "Accessibility Statement",
		link: "/",
	},
];

export const socialIconsData = [
	{
		href: "http://www.facebook.com",
		ariaLabel: "Facebook",
		icon: FaFacebookF,
	},
	{
		href: "http://www.twitter.com",
		ariaLabel: "Twitter",
		icon: FaXTwitter,
	},
	{
		href: "http://www.instagram.com",
		ariaLabel: "Instagram",
		icon: FaInstagram,
	},
];

export const confirmationPageData: StatusPageProps = {
	backgroundImageSrc: "/bg/bg-cta.jpg",
	backgroundImageAlt: "Decorative background with a pleasant texture",
	imageSrc: "",
	imageAlt: "Illustration for confirmation page",
	imageWidth: 500,
	imageHeight: 283,
	heading: "Thank you!",
	text: "We have received your submission and will get back to you as soon as possible.",
	buttonLink: "/",
	buttonText: "Back to Homepage",
};