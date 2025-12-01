export const homepagesListData = {
	emptyMessage: "No home pages available at the moment.",
	defaultAltText: "Home page",
	carouselAriaLabel: "Our home pages",
	subtitle: "Home pages",
	title: "take a look at the home pages",
	divider: true,
	align: "center" as const,
	items: [
		{
			id: 1,
			image: "/sections/home-classic.jpg",
			altText: "Homer Classic",
			link: "/home-classic",
			caption: "Home Classic",
		},
		{
			id: 2,
			image: "/sections/home-creative.jpg",
			altText: "Homer Creative",
			link: "/home-creative",
			caption: "Home Creative",
		},
	],
};
