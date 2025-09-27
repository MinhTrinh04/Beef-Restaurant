"use client";
import React, { useState, useEffect, useRef, useCallback } from "react";
import { FaAngleRight } from "react-icons/fa6";
import Link from "next/link";
import { itemsNavbar } from "@/app/hooks/data-navbar";
import { NavItem, MenuMobileProps } from "@/app/types/common.types";
import { MenuMobileData } from "@/app/hooks/data-mobile-menu";
import { cn } from "@/lib/utils";

/**
 * A full-screen mobile menu with multi-level navigation.
 * It handles its own state for sub-menus and closing animations.
 *
 * @param {MenuMobileProps} props The props for the component.
 * @returns {React.ReactElement} The rendered mobile menu.
 */
const MenuMobile: React.FC<MenuMobileProps> = ({
	id,
	isMobile,
	setIsMobile,
	ariaLabelGoBack = MenuMobileData.ariaLabelGoBack,
	ariaLabelCloseMenu = MenuMobileData.ariaLabelCloseMenu,
	ariaLabelSubmenu = MenuMobileData.ariaLabelSubmenu,
	defaultMenuTitle = MenuMobileData.defaultMenuTitle,
}) => {
	const [delayedIsMobile, setDelayedIsMobile] = useState(false);
	const [activeSubMenuId, setActiveSubMenuId] = useState<number | null>(null);
	const menuContentRef = useRef<HTMLDivElement>(null);

	/**
	 * Effect to add a delay to the `isMobile` state.
	 * This allows for CSS exit animations to complete before the component is unmounted or hidden.
	 */
	useEffect(() => {
		let timer: NodeJS.Timeout;
		if (isMobile) {
			setDelayedIsMobile(true);
		} else {
			timer = setTimeout(() => {
				setDelayedIsMobile(false);
			}, 300); // Delay should match the CSS transition duration
		}
		return () => clearTimeout(timer);
	}, [isMobile]);

	const handleClose = useCallback(() => {
		setIsMobile(false);
	}, [setIsMobile]);

	/**
	 * Effect to handle clicks outside the menu content to close it.
	 * This improves user experience by allowing an intuitive closing action.
	 */
	useEffect(() => {
		const handleClickOutside = (event: MouseEvent) => {
			if (
				menuContentRef.current &&
				!menuContentRef.current.contains(event.target as Node)
			) {
				handleClose();
			}
		};

		if (isMobile) {
			document.addEventListener("mousedown", handleClickOutside);
		}

		return () => {
			document.removeEventListener("mousedown", handleClickOutside);
		};
	}, [isMobile, handleClose]);

	/**
	 * Effect to reset the active submenu when the main menu is closed.
	 * Ensures a clean state next time the menu is opened.
	 */
	useEffect(() => {
		if (!isMobile) {
			const timer = setTimeout(() => {
				setActiveSubMenuId(null);
			}, 300); // Delay matches the closing animation for a smooth transition.
			return () => clearTimeout(timer);
		}
	}, [isMobile]);

	/**
	 * Navigates back to the main menu from a submenu.
	 */
	const handleBackClick = () => {
		setActiveSubMenuId(null);
	};

	/**
	 * Sets the active submenu based on the item ID clicked.
	 * @param {number} itemId The ID of the menu item that triggers the submenu.
	 */
	const handleSubMenuTriggerClick = (itemId: number) => {
		setActiveSubMenuId(itemId);
	};

	const activeMenuItem = itemsNavbar.find((item) => item.id === activeSubMenuId);
	const menuTitle = activeMenuItem ? activeMenuItem.title : defaultMenuTitle;

	// Early return if not in mobile view to avoid rendering an empty component
	if (!isMobile && !delayedIsMobile) {
		return null;
	}

	return (
		<>
			<nav
				id={id}
				className={cn("menu-mobile", { "menu-mobile__open": isMobile })}
				role="navigation"
				aria-label="Mobile menu"
			>
				<div
					ref={menuContentRef}
					className={cn("menu-mobile__content", {
						"menu-mobile__content__open": delayedIsMobile,
					})}
				>
					<div className="menu-mobile__header">
						<div
							className={cn("menu-mobile__back", {
								"back-active": activeSubMenuId !== null,
							})}
							onClick={handleBackClick}
							onKeyDown={(e) => e.key === "Enter" && handleBackClick()}
							role="button"
							tabIndex={activeSubMenuId !== null ? 0 : -1}
							aria-label={ariaLabelGoBack}
						></div>
						<div className="menu-mobile__title">
							<span>{menuTitle}</span>
						</div>
						<div
							className="menu-mobile__close"
							onClick={handleClose}
							onKeyDown={(e) => e.key === "Enter" && handleClose()}
							role="button"
							tabIndex={0}
							aria-label={ariaLabelCloseMenu}
						></div>
					</div>
					<div
						className={cn("menu-mobile__nav", {
							"submenu-active": activeSubMenuId !== null,
						})}
					>
						<div className="menu-mobile__nav-content">
							<ul className="menu-mobile__nav-list">
								{itemsNavbar.map((item: NavItem) => (
									<li
										key={item.id}
										className={cn("menu-mobile__nav-item", {
											"nav-item__has-children":
												item.children && item.children.length > 0,
											"show-menu": activeSubMenuId === item.id,
										})}
									>
										{item.children && item.children.length > 0 ? (
											<>
												<div className="menu-mobile__nav-item__title">
													<Link
														href={item.link}
														className="menu-mobile__nav-link"
														onClick={handleClose}
													>
														{item.title}
													</Link>
													<div
														className="menu-mobile__nav-icon"
														onClick={() => handleSubMenuTriggerClick(item.id)}
														onKeyDown={(e) =>
															e.key === "Enter" &&
															handleSubMenuTriggerClick(item.id)
														}
														role="button"
														tabIndex={0}
														aria-label={`${ariaLabelSubmenu} ${item.title}`}
														aria-expanded={activeSubMenuId === item.id}
													>
														<FaAngleRight />
													</div>
												</div>
												<div className="menu-mobile__nav-submenu">
													<ul className="menu-mobile__nav-submenu-list">
														{item.children.map((childItem) => (
															<li
																key={childItem.id}
																className="menu-mobile__nav-submenu-item"
															>
																<Link
																	href={childItem.link}
																	className="menu-mobile__nav-link"
																	onClick={handleClose}
																>
																	{childItem.title}
																</Link>
															</li>
														))}
													</ul>
												</div>
											</>
										) : (
											<Link
												href={item.link}
												className="menu-mobile__nav-link"
												onClick={handleClose}
											>
												{item.title}
											</Link>
										)}
									</li>
								))}
							</ul>
						</div>
					</div>
				</div>
			</nav>
			<div
				className={cn("menu-mobile__overlay", { active: isMobile })}
				onClick={handleClose}
				aria-label={ariaLabelCloseMenu}
			></div>
		</>
	);
};

export default MenuMobile;
