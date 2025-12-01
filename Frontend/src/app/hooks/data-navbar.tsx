// Extract history data into an array
export const itemsNavbar = [
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