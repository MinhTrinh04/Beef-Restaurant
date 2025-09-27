"use client";
import { useState } from "react";
import Link from "next/link";
import { motion } from "framer-motion";
import { cn } from "@/lib/utils";

interface NavItemData {
    id: number;
    title: string;
    link: string;
    children?: NavItemData[];
}

interface NavItemProps {
    item: NavItemData;
}

const NavItem = ({ item }: NavItemProps) => {
    const [isOpen, setIsOpen] = useState(false);
    const hasChildren = item.children && item.children.length > 0;

    const handleMouseEnter = () => {
        if (hasChildren) {
            setIsOpen(true);
        }
    };

    const handleMouseLeave = () => {
        if (hasChildren) {
            setIsOpen(false);
        }
    };

    const handleFocusWithin = () => {
        if (hasChildren) {
            setIsOpen(true);
        }
    };

    const handleBlurWithin = (event: React.FocusEvent<HTMLLIElement>) => {
        if (hasChildren && !event.currentTarget.contains(event.relatedTarget as Node)) {
            setIsOpen(false);
        }
    };

    return (
        <li
            className={cn("dropdown simple-dropdown", { "has-children": hasChildren })}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            onFocusCapture={handleFocusWithin}
            onBlurCapture={handleBlurWithin}
        >
            <Link
                href={item.link}
                aria-haspopup={hasChildren ? "true" : undefined}
                aria-expanded={hasChildren ? isOpen : undefined}
            >
                {item.title}
            </Link>
            {hasChildren && isOpen && (
                <ul className="dropdown-menu open" aria-label={`${item.title} submenu`}>
                    {item.children?.map((childItem, childIndex) => (
                        <motion.li
                            key={childItem.id}
                            className="dropdown"
                            initial={{ opacity: 0, y: 0 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.1, ease: "easeInOut", delay: 0.2 + childIndex * 0.1 }}
                        >
                            <Link href={childItem.link}>{childItem.title}</Link>
                        </motion.li>
                    ))}
                </ul>
            )}
        </li>
    );
};

export default NavItem; 