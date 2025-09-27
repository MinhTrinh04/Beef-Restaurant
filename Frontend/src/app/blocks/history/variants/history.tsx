"use client"; // Required for framer-motion hooks
import Image from "next/image";
import React from "react";
import BlockTitle from "@/app/components/common/block-title/block-title";
import { motion } from "framer-motion"; // Import motion

// interfaces
import { HistoryBlockProps } from "@/app/types/common.types";

const itemVariants = {
	hidden: { opacity: 0, y: 0 },
	visible: {
		opacity: 1,
		y: 0,
		transition: {
			duration: 0.6,
			ease: "easeInOut",
		},
	},
};

const HistoryBlock = ({
	subtitle,
	title,
	description,
	items,
}: HistoryBlockProps) => {
	return (
		<>
			{/* History Block */}
			<div className="history">
				<div className="history__container">
					{/* Section title */}
					<BlockTitle
						subtitle={subtitle}
						title={title}
						phrase={description}
					/>
					{/*/ Section title */}

					<div className="history__content">
						{/* History list */}
						<div className="history__list">
							{/* Map through history data and apply animation */}
							{items.map((item) => (
								<motion.div
									key={item.id}
									className="history__item"
									variants={itemVariants}
									initial="hidden"
									whileInView="visible"
									viewport={{ once: true, amount: 0.3 }} // Trigger animation when 30% of the item is in view
								>
									{/* Image */}
									<div className="history__item-image">
										<Image
											src={item.imageSrc}
											alt={item.title} // Improved alt text
											width="361"
											height="541"
										/>
									</div>
									{/*/ Image */}
									{/* Content */}
									<div className="history__item-content">
										<span className="history__item-year">
											{item.year}
										</span>
										<h3 className="history__item-title">
											{item.title}
										</h3>
										<p className="history__item-description">
											{item.description}
										</p>
									</div>
									{/*/ Content */}
								</motion.div>
							))}
							{/*/ Map through history data */}
						</div>
						{/*/ History list */}
					</div>
				</div>
			</div>
			{/* /History Block */}
		</>
	);
};

export default HistoryBlock;
