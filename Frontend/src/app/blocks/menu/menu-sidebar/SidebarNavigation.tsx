"use client";
import React from "react";
import Image from "next/image";
import Link from "next/link";

import { SidebarNavigationProps } from "@/app/types/common.types";

const SidebarNavigation: React.FC<SidebarNavigationProps> = ({ navItems, activeItem, onNavLinkClick }) => {
  if (!navItems || navItems.length === 0) {
    return null; // Or some fallback UI
  }

  return (
    <nav className="navmenu-sidebar">
      <ul className="navmenu-sidebar__list">
        {navItems.map((item, index) => {
          // Provide a fallback for anchor to prevent it from being undefined
          const anchor = item.anchor || '';

          return (
            <li key={index}>
              <Link
                href={`#${anchor}`}
                className={`navmenu-sidebar__link ${
                  activeItem === index ? "active" : ""
                }`}
                onClick={(e) => onNavLinkClick(e, index, anchor)}
                aria-label={`Navigate to ${item.title} section`}
                tabIndex={0}
                onKeyDown={(e) => {
                  if (e.key === "Enter" || e.key === " ") {
                    // Trigger click behavior for keyboard accessibility
                    onNavLinkClick(e as any, index, anchor);
                  }
                }}
              >
                {item.icon && (
                  <Image
                    src={item.icon}
                    alt="" // Decorative icon, alt is empty
                    width={24}
                    height={24}
                    aria-hidden="true"
                  />
                )}
                <span>{item.title}</span>
              </Link>
            </li>
          );
        })}
      </ul>
    </nav>
  );
};

export default SidebarNavigation; 