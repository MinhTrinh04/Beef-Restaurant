"use client";
import React from "react";
import { motion } from "framer-motion";
import { cn } from "@/lib/utils";
import { DividerProps } from "@/app/types/common.types";

/**
 * Renders a visual divider component with an optional animation.
 * It's used to separate content sections. It also has a decorative
 * arrow pointing down.
 *
 * @param {DividerProps} props The props for the component.
 * @returns {React.ReactElement} The rendered divider.
 */
const Divider: React.FC<DividerProps> = ({ className }) => {
	const animationVariants = {
		hidden: { opacity: 0 },
		visible: { opacity: 1 },
	};

	const animationTransition = {
		ease: "easeOut",
		duration: 0.5,
	};

	return (
		<motion.div
			className={cn("divider div-transparent div-arrow-down", className)}
			initial="hidden"
			animate="visible"
			variants={animationVariants}
			transition={animationTransition}
			role="separator"
			aria-hidden="true"
		></motion.div>
	);
};

export default Divider;
