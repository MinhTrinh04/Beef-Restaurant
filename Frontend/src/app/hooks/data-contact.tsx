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
	image: "/bg/bg-cta.jpg",
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
		"Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam, quos.",
	items: [
		{
			id: 1,
			name: "Brooklyn",
			addressLine1: "123 Main Street, New York,",
			addressLine2: "NY 10001, USA",
			phone: "+1 123 456 7890",
			email: "brooklyn@beef.com",
			imageSrc: "/locations/location-1.jpg",
			imageAltText: "Image of the Brooklyn location",
			directionLink: "#", // Replace with actual direction link
			directionButtonText: "Get direction",
		},
		{
			id: 2,
			name: "Queens",
			addressLine1: "St Jhons PI/Nostrand Av, Brooklyn,",
			addressLine2: "NY 11216, USA",
			phone: "+1 123 456 7890",
			email: "queens@beef.com", // Assuming different email, update if needed
			imageSrc: "/locations/location-2.jpg",
			imageAltText: "Image of the Queens location",
			directionLink: "#", // Replace with actual direction link
			directionButtonText: "Get direction",
		},
	],
};