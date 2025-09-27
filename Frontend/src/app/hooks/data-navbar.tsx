// Extract history data into an array
export const itemsNavbar = [
	{
		id: 1,
		title: "Home",
		link: "/",
		children: [
			{
				id: 11,
				title: "Creative",
				link: "/home-creative",
			},
			{
				id: 12,
				title: "Classic",
				link: "/home-classic",
			},
		],
	},
	{
		id: 2,
		title: "Menu",
		link: "/menu",
		children: [
			{
				id: 21,
				title: "Menu classic",
				link: "/menu",
			},
			{
				id: 24,
				title: "Full Width",
				link: "/menu-full-width",
			},
			{
				id: 22,
				title: "Menu Tabs",
				link: "/menu-tabs",
			},
			{
				id: 23,
				title: "Menu Sidebar",
				link: "/menu-sidebar",
			},
		],
	},
	{
		id: 3,
		title: "Pages",
		link: "/",
		children: [
			{
				id: 31,
				title: "About Us",
				link: "/about-us",
			},
			{
				id: 32,
				title: "Chefs",
				link: "/chefs",
			},
			{
				id: 33,
				title: "History",
				link: "/history",
			},
			{
				id: 34,
				title: "Services",
				link: "/services",
			},
		],
	},
	{
		id: 6,
		title: "Contact",
		link: "/contact-us",
	},
	{
		id: 7,
		title: "Others",
		link: "/",
		children: [
			{
				id: 71,
				title: "Error 404",
				link: "/404",
			},
			{
				id: 72,
				title: "Confirmation",
				link: "/confirmation",
			},
			{
				id: 73,
				title: "Coming Soon",
				link: "/coming-soon",
			},
		],
	},
];