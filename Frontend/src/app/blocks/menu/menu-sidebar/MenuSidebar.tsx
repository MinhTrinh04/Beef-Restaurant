"use client";
import React, { useState, useEffect, useRef, useCallback } from "react";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import SidebarNavigation from "./SidebarNavigation";
import MenuContentDisplay from "./MenuContentDisplay";
import throttle from 'lodash.throttle';

//Data
import { HeroInnerMenuSidebarData } from "@/app/hooks/data";
import { categoryDishesData } from "@/app/hooks/data-dish";

interface MenuSidebarProps {
  // Props can be added here if needed in the future
}

const SCROLL_THROTTLE_LIMIT = 200; // Throttle limit in ms

const MenuSidebar: React.FC<MenuSidebarProps> = () => {
	const [activeItem, setActiveItem] = useState(0);
	const navLinksRef = useRef<NodeListOf<HTMLAnchorElement> | null>(null);
	const sectionsRef = useRef<NodeListOf<HTMLElement> | null>(null);

	const getDynamicOffset = () => {
		if (typeof window === "undefined") return 200; // Default for SSR
		const currentWidth = window.innerWidth;
		if (currentWidth >= 1200) return 460;
		if (currentWidth >= 768) return 300;
		return 200;
	};

	const onScroll = useCallback(() => {
		if (
			!sectionsRef.current ||
			!navLinksRef.current ||
			sectionsRef.current.length === 0
		)
			return;

		let currentSectionId = "";
		const pageYOffset = window.pageYOffset;
		const offset = getDynamicOffset(); // Get current dynamic offset

		if (pageYOffset <= offset) {
			// Check if at the top (or near, considering offset)
			currentSectionId = sectionsRef.current[0]?.getAttribute("id") || "";
		} else if (
			window.innerHeight + pageYOffset >=
			document.body.offsetHeight - 2
		) {
			// Check if at the bottom
			currentSectionId =
				sectionsRef.current[sectionsRef.current.length - 1]?.getAttribute(
					"id",
				) || "";
		} else {
			sectionsRef.current.forEach((section) => {
				const sectionTop = section.offsetTop;
				const sectionHeight = section.offsetHeight;
				// Adjust condition to ensure section is comfortably in view considering the dynamic offset
				if (
					pageYOffset >= sectionTop - offset &&
					pageYOffset < sectionTop + sectionHeight - offset
				) {
					currentSectionId = section.getAttribute("id") || "";
				}
			});
		}

		// Update active states for navigation links
		let activeIndexFromScroll = -1;
		navLinksRef.current.forEach((link, index) => {
			link.classList.remove("active");
			if (currentSectionId && link.getAttribute("href") === `#${currentSectionId}`) {
				link.classList.add("active");
				activeIndexFromScroll = index;
			}
		});

		// Sync activeItem state if a new section is scrolled into view
		// This ensures the state used by SidebarNavigation for explicit active class is also updated
		if (activeIndexFromScroll !== -1 && activeItem !== activeIndexFromScroll) {
			setActiveItem(activeIndexFromScroll);
		}
	}, [activeItem]);

	useEffect(() => {
		const sections = document.querySelectorAll(".menu__category");
		const navLinks = document.querySelectorAll(".navmenu-sidebar__link");

		sectionsRef.current = sections as NodeListOf<HTMLElement>;
		navLinksRef.current = navLinks as NodeListOf<HTMLAnchorElement>;

		if (navLinksRef.current && navLinksRef.current.length > 0) {
			onScroll();
		}

		const throttledOnScroll = throttle(onScroll, SCROLL_THROTTLE_LIMIT);

		window.addEventListener("scroll", throttledOnScroll);
		return () => window.removeEventListener("scroll", throttledOnScroll);
	}, [onScroll]);

	const handleSetActiveItem = (index: number) => {
		setActiveItem(index);
	};

	const handleNavLinkClick = (event: React.MouseEvent<HTMLAnchorElement>, index: number, anchor: string) => {
		handleSetActiveItem(index);
		const targetElement = document.getElementById(anchor);

		if (targetElement) {
			event.preventDefault();
			const headerHeight = parseFloat(getComputedStyle(document.documentElement).getPropertyValue("--header-height")) || 0;
			// The dynamic offset for scrolling should be relative to the viewport, not header.
			// The scrollspy's getDynamicOffset logic might need to be different from scrolling adjustment.
			// For click, we usually want to align the top of the element with the bottom of a sticky header.
			const topPosition = targetElement.offsetTop - headerHeight; // Simpler offset for click-scroll

			window.scrollTo({
				top: topPosition,
				behavior: "smooth",
			});

			// Update active class on navLinksRef immediately for visual feedback
			// The scroll event will eventually also call onScroll and update, but this is faster.
			if (navLinksRef.current) {
				navLinksRef.current.forEach(link => link.classList.remove("active"));
				event.currentTarget.classList.add("active");
			}
		}
	};

	return (
		<div className="menu-page">
			
			<HeroInnerBlock
				title={HeroInnerMenuSidebarData.title}
				image={HeroInnerMenuSidebarData.image}
				altText={HeroInnerMenuSidebarData.altText}
				breadcrumbs={HeroInnerMenuSidebarData.breadcrumbs}
			/>

			<div className="menu menu-sidebar">
				<div className="navmenu__container">
					{categoryDishesData?.items && (
						<SidebarNavigation
							navItems={categoryDishesData.items}
							activeItem={activeItem}
							onNavLinkClick={handleNavLinkClick}
						/>
					)}
					{categoryDishesData?.items && (
						<MenuContentDisplay
							categories={categoryDishesData.items}
							emptyMessage={categoryDishesData.emptyMessage}
						/>
					)}
				</div>
			</div>
		</div>
	);
};

export default MenuSidebar; 