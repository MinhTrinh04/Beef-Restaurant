"use client";
import React, { useState, useEffect, useCallback } from "react";
import Link from "next/link";
import LogoLink from "@/app/components/common/logo/logoLink";
import { LogoData, socialIconsData } from "@/app/hooks/data-general";
import { itemsNavbar } from "@/app/hooks/data-navbar";
import SocialIcons from "@/app/components/common/social/SocialIcons";
import MenuMobile from "@/app/components/common/menuMobile/menuMobile";
import MenuToggleButton from "./MenuToggleButton";
import { cn } from "@/lib/utils";
import throttle from 'lodash.throttle';

const STICKY_THRESHOLD = 100; // Define scroll threshold (adjust as needed)
const SCROLL_THROTTLE_LIMIT = 200; // Throttle limit in ms

/**
 * The main header component for the website.
 * It includes the logo, main navigation, social icons, and a mobile menu toggle.
 * It also handles the "sticky" behavior on scroll.
 * @returns {React.ReactElement} The rendered header component.
 */
const Header: React.FC = () => {
	const [isSticky, setIsSticky] = useState(false);
	const [isMobile, setIsMobile] = useState(false);

	/**
	 * Effect to handle the sticky header functionality.
	 * It adds a throttled scroll event listener to track the page's scroll position
	 * and applies a sticky class to the header when the threshold is passed.
	 */
	useEffect(() => {
		const handleScroll = () => {
			setIsSticky(window.scrollY > STICKY_THRESHOLD);
		};

		const throttledScrollHandler = throttle(handleScroll, SCROLL_THROTTLE_LIMIT);

		window.addEventListener("scroll", throttledScrollHandler);
		handleScroll(); // Initial check

		return () => {
			window.removeEventListener("scroll", throttledScrollHandler);
		};
	}, []); // Empty dependency array ensures this runs only on mount and unmount

	/**
	 * Toggles the visibility of the mobile menu.
	 */
	const handleToggleMobileMenu = () => {
		setIsMobile(!isMobile);
	};

	return (
		<>
			<header className={cn("header", { "header__sticky": isSticky })}>
				<div className="header__content">
					<div className="header__logo">
						<LogoLink
							href={LogoData.hrefLogo}
							src={LogoData.urlLogo}
							alt={LogoData.altLogo}
							width={LogoData.widthLogo}
							height={LogoData.heightLogo}
						/>
					</div>

					<div className="header__nav">
						{/* Desktop Navigation */}
						<nav className="header__menu" aria-label="Main navigation">
							<ul className="header__menu-list">
								{itemsNavbar.map((item) => (
									<li
										key={item.id}
										className={cn(
											"dropdown simple-dropdown",
											item.children &&
												item.children.length > 0 &&
												"has-children",
										)}
									>
										<Link href={item.link}>{item.title}</Link>
										{item.children && item.children.length > 0 && (
											<ul className="dropdown-menu">
												{item.children.map((child) => (
													<li key={child.id}>
														<Link href={child.link}>{child.title}</Link>
													</li>
												))}
											</ul>
										)}
									</li>
								))}
							</ul>
						</nav>

						{/* Mobile Menu Toggle & Social Icons Wrapper */}
						<div className="flex items-center">
							<div className="header__social">
								<SocialIcons socials={socialIconsData} />
							</div>
							<div className="header__mobile">
								<MenuToggleButton
									isMobile={isMobile}
									onToggle={handleToggleMobileMenu}
									aria-controls="menu-mobile"
								/>
							</div>
						</div>
					</div>
				</div>
			</header>
			<MenuMobile
				id="menu-mobile"
				isMobile={isMobile}
				setIsMobile={setIsMobile}
			/>
		</>
	);
};

export default Header;
