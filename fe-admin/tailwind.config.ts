import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  safelist: [
    // Status badge colors - ensure these are always included
    'bg-yellow-500/20', 'text-yellow-500',
    'bg-blue-500/20', 'text-blue-500',
    'bg-purple-500/20', 'text-purple-500',
    'bg-green-500/20', 'text-green-500',
    'bg-green-600/20', 'text-green-600',
    'bg-green-700/20', 'text-green-700',
    'bg-red-500/20', 'text-red-500',
    'bg-yellow-600/20', 'text-yellow-600',
    'bg-gray-500/20', 'text-gray-500',
  ],
  theme: {
    extend: {
      colors: {
        // Semantic color palette matching Frontend
        primary: "#D9B282", // Main accent color
        secondary: "#AE8E68", // Secondary accent color
        background: "#0A0D0F", // Main background color
        surface: "#151A1E", // For cards, menus, etc.
        "surface-dark": "#050608", // Darker surface color
        "text-base": "#FBF7F3", // Base text color
        "text-muted": "#A3A6A8", // Muted text color
        border: "#484D51", // Border and divider color
        
        // Original color palettes
        calico: {
          0: "#FBF7F3",
          100: "#F7F0E6",
          200: "#F0E0CD",
          300: "#E8D1B4",
          400: "#E1C19B",
          500: "#D9B282",
          600: "#AE8E68",
          700: "#826B4E",
          800: "#574734",
          900: "#2B241A",
        },
        coarseWool: {
          0: "#E8E9E9",
          100: "#D1D2D4",
          200: "#A3A6A8",
          300: "#76797D",
          400: "#484D51",
          500: "#1A2026",
          600: "#151A1E",
          700: "#0f1115",
          800: "#0A0D0F",
          900: "#050608",
        },
      },
      fontFamily: {
        "josefin-sans": ["Josefin Sans", "sans-serif"],
        mulish: ["Mulish", "sans-serif"],
        "sorts-mill-goudy": ["Sorts Mill Goudy", "serif"],
        "barlow-condensed": ["Barlow Condensed", "sans-serif"],
      },
      fontSize: {
        "title-1": ["58px", { lineHeight: "1.2" }],
        "title-2": ["48px", { lineHeight: "1.2" }],
        "title-3": ["38px", { lineHeight: "1.2" }],
        "title-4": ["28px", { lineHeight: "1.4" }],
        "title-5": ["22px", { lineHeight: "1.4" }],
        "title-6": ["18px", { lineHeight: "1.4", letterSpacing: "-0.5px" }],
      },
    },
  },
  plugins: [],
};

export default config;
