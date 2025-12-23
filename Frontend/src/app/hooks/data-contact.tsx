import { title } from "process";

export const HeroInnerContactData = {
	title: "contact us",
	breadcrumbs: [
		{
			id: 1,
			title: "Home",
			link: "/",
		},
		{
			id: 2,
			title: "Contact us",
			link: "",
		},
	],
	image: "/bg/bg-cta.webp",
	altText: "Contact us",
};

export const contactData = {
	subtitle: "Reservation",
	title: "Book a Table on Time",
	align: "center",
	phrase: `The first restaurant proprietor is believed to have
            been one A. Boulanger, a soup vendor, who opened his
            business in 1765.`,
	formAction: "/ruta-de-envio",
	className: "",
	formContent: {
		nameLabel: "Name:",
		namePlaceholder: "Name",
		phoneLabel: "Phone:",
		phonePlaceholder: "Phone",
		emailLabel: "Email:",
		emailPlaceholder: "Email",
		personsLabel: "Persons:",
		personsPlaceholder: "0",
		dateLabel: "Date:",
		timeLabel: "Hours:",
		messageLabel: "Message:",
		messagePlaceholder: "Comments",
		submitButtonText: "Book a Table",
	},
};

export const titleLocationsData = {
	title: "Our locations",
	align: "left",
};

// Extract location data into an array
export const locationsData = {
	title: "Our locations",
	description:
		"Visit us at our premium locations in Vietnam's most vibrant cities. Experience exceptional dining in the heart of Ho Chi Minh City and Hanoi.",
	items: [
		{
			id: 1,
			name: "Ho Chi Minh City",
			addressLine1: "123 Nguyen Hue Boulevard, District 1,",
			addressLine2: "Ho Chi Minh City, Vietnam",
			phone: "+84 28 3822 5678",
			email: "saigon@beef.vn",
			imageSrc: "/locations/location-1.webp",
			imageAltText: "Image of the Ho Chi Minh City location",
			directionLink: "https://maps.google.com/?q=Nguyen+Hue+Boulevard+District+1+Ho+Chi+Minh+City",
			directionButtonText: "Get direction",
		},
		{
			id: 2,
			name: "Hanoi",
			addressLine1: "45 Trang Tien Street, Hoan Kiem District,",
			addressLine2: "Hanoi, Vietnam",
			phone: "+84 24 3826 1234",
			email: "hanoi@beef.vn",
			imageSrc: "/locations/location-2.webp",
			imageAltText: "Image of the Hanoi location",
			directionLink: "https://maps.google.com/?q=Trang+Tien+Street+Hoan+Kiem+Hanoi",
			directionButtonText: "Get direction",
		},
	],
};