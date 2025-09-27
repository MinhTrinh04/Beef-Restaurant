"use client";
import { itemsNavbar } from "@/app/hooks/data-navbar";
import NavItem from "./NavItem";

const Navbar = ({ isMobile }: { isMobile: boolean }) => {
	return (
		<div
			className={`header__nav ${isMobile ? "navbar__mobile" : ""}`}
			role="navigation"
			aria-label="Main navigation"
		>
			<nav className="header__menu">
				<ul className="header__menu-list">
					{itemsNavbar.map((item) => (
						<NavItem key={item.id} item={item} />
					))}
				</ul>
			</nav>
		</div>
	);
};

export default Navbar;
