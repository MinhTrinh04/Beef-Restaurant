import { cn } from "@/lib/utils";
import Image from "next/image";
import Link from "next/link";
import { motion } from "framer-motion"; // Import motion
import { SliderItemProps } from "@/app/types/common.types";

// Define animation variants
const contentVariants = {
	hidden: { y: 0, filter: "blur(20px)", opacity: 0 },
	visible: (delay: number) => ({
		y: 0,
		filter: "blur(0px)",
		opacity: 1,
		transition: {
			delay: delay * 0.1, // Stagger the animation like animation-delay
			duration: 0.5,
			ease: "easeOut",
		},
	}),
};

const SliderItem = ({
	itemActive,
	id,
	image,
	altText,
	subtitle,
	title,
	desc,
	link,
	textLink,
}: SliderItemProps) => {
	const isActive = itemActive === id;

	return (
		<li
			className={cn(
				"slider-hero__item",
				isActive ? "slider-hero__item--show" : "slider-hero__item--hide"
			)}
		>
			<div className="slider-hero__item-image">
				<Image src={image} alt={altText} width={1920} height={800} />
			</div>
			<div className="slider-hero__item-content">
				<motion.p
					className="slider-hero__item-subtitle"
					custom={3} // Corresponds to animation-delay-3
					initial="hidden"
					animate={isActive ? "visible" : "hidden"}
					variants={contentVariants}
				>
					{subtitle}
				</motion.p>
				<motion.h1
					className="slider-hero__item-title"
					custom={4} // Corresponds to animation-delay-4
					initial="hidden"
					animate={isActive ? "visible" : "hidden"}
					variants={contentVariants}
				>
					{title}
				</motion.h1>
				<motion.p
					className="slider-hero__item-text"
					custom={5} // Corresponds to animation-delay-5
					initial="hidden"
					animate={isActive ? "visible" : "hidden"}
					variants={contentVariants}
				>
					{desc}
				</motion.p>
				{/* Use motion.a for Link component */}
				<motion.div
					custom={6} // Corresponds to animation-delay-6
					initial="hidden"
					animate={isActive ? "visible" : "hidden"}
					variants={contentVariants}
				>
					<Link className="btn btn__default" href={link}>
						{textLink}
					</Link>
				</motion.div>
			</div>
		</li>
	);
};

export default SliderItem;
