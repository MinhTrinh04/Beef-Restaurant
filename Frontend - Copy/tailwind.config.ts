import { transform } from "next/dist/build/swc";
import type { Config } from "tailwindcss";
import plugin from "tailwindcss/plugin";

const config: Config = {
	content: [
		"./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/components/**/*.{js,ts,jsx,tsx,mdx}",
		"./src/app/**/*.{js,ts,jsx,tsx,mdx}",
	],
	theme: {
		extend: {
			backgroundImage: {
				slider: "linear-gradient(to bottom, rgba(5, 6, 8, 0.6), rgba(5, 6, 8, 1))",
				thumbnails: "linear-gradient(to top, #000 2%, transparent)",
			},
			colors: {
				// NOTE: Semantic color palette for easy theming
				primary: "#D9B282", // Main accent color
				secondary: "#AE8E68", // Secondary accent color
				background: "#0A0D0F", // Main background color
				surface: "#151A1E", // For cards, menus, etc.
				"surface-dark": "#050608", // Darker surface color
				"text-base": "#FBF7F3", // Base text color
				"text-muted": "#A3A6A8", // Muted text color
				border: "#484D51", // Border and divider color
				black: "#000000",
				white: "#ffffff",

				// NOTE: Original color palettes kept for reference or specific use cases
				calico: {
					0: "#FBF7F3",
					100: "#F7F0E6",
					200: "#F0E0CD",
					300: "#E8D1B4",
					400: "#E1C19B",
					500: "#D9B282", // Main Accent (primary)
					600: "#AE8E68", // Secondary Accent
					700: "#826B4E",
					800: "#574734",
					900: "#2B241A",
				},
				coarseWool: {
					0: "#E8E9E9",
					100: "#D1D2D4",
					200: "#A3A6A8", // Muted Text
					300: "#76797D",
					400: "#484D51", // Border
					500: "#1A2026",
					600: "#151A1E", // Surface
					700: "#0f1115",
					800: "#0A0D0F", // Background
					900: "#050608", // Surface Dark
				},
			},
			fontFamily: {
				"josefin-sans": ["Josefin Sans", "sans-serif"],
				mulish: ["Mulish", "sans-serif"],
				"sorts-mill-goudy": ["Sorts Mill Goudy", "serif"],
				"barlow-condensed": ["Barlow Condensed", "sans-serif"],
				"icon-awesome": ["Font Awesome 5 Free"],
			},
			fontSize: {
				"display-1": [
					"108px",
					{
						lineHeight: "1.2",
						letterSpacing: "-0.5px",
					},
				],
				"display-2": [
					"81px",
					{
						lineHeight: "1.2",
					},
				],
				"title-1": ["58px", { lineHeight: "1.2" }],
				"title-2": ["48px", { lineHeight: "1.2" }],
				"title-3": ["38px", { lineHeight: "1.2" }],
				"title-4": ["28px", { lineHeight: "1.4" }],
				"title-5": ["22px", { lineHeight: "1.4" }],
				"title-6": [
					"18px",
					{ lineHeight: "1.4", letterSpacing: "-0.5px" },
				],
				"subtitle-1": [
					"20px",
					{ lineHeight: "1.4", letterSpacing: "1.5px" },
				],
				"subtitle-2": [
					"18px",
					{ lineHeight: "1.4", letterSpacing: "1px" },
				],
				lead: ["21px", { lineHeight: "1.6", letterSpacing: "0.5px" }],
				body: ["18px", { lineHeight: "1.6", letterSpacing: "0.25px" }],
				button: ["18px", { lineHeight: "1.6", letterSpacing: "0.5px" }],
				caption: [
					"15px",
					{ lineHeight: "1.6", letterSpacing: "0.5px" },
				],
				overline: [
					"13px",
					{ lineHeight: "1.6", letterSpacing: "1.5px" },
				],
			},
			container: {
				center: true,
				padding: "1rem",
				},
			},
		},
	plugins: [
		require("tailwindcss"),
		require("postcss-nested"),
		require("autoprefixer"),
		require("@tailwindcss/typography"),
		plugin(function ({ addComponents }) {
			addComponents({
				".container-fluid": {
					width: "100%",
					maxWidth: {
						"2xl": "1340px",
						"3xl": "1420px",
					},
					marginRight: "auto",
					marginLeft: "auto",
				},
			});
		}),
	],
};
export default config;
