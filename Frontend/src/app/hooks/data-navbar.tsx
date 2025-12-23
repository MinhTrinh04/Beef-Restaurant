// Define the type for navbar items
export interface NavbarItem {
	id: number;
	title: string;
	link: string;
	children?: NavbarItem[];
}

// Extract history data into an array
export const itemsNavbar: NavbarItem[] = [
	{
		id: 1,
		title: "Home",
		link: "/",
	},
	{
		id: 2,
		title: "Menu",
		link: "/menu",
	},
	{
		id: 3,
		title: "About Us",
		link: "/about-us",
	},
	{
		id: 6,
		title: "Contact",
		link: "/contact-us",
	},
];