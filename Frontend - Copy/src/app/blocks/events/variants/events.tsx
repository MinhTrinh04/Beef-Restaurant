import React from "react";
import BlockTitle from "../../../components/common/block-title/block-title";
import CardEvent from "./cardEvent";

// Import
import { EventsBlockProps, Alignment } from "@/app/types/common.types";

const EventsBlock = ({
	subtitle,
	title,
	description,
	align,
	items,
	emptyMessage = "No offers available at the moment.",
}: EventsBlockProps) => {
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="events-block events-block--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className={`events-block bg-surface`}>
			<div className="events-block__container">
				{/* Section title  */}
				<BlockTitle
					subtitle={subtitle}
					title={title || ""}
					phrase={description}
					align={align as Alignment}
				/>
				{/*/ Section title  */}

				{/* Events list  */}
				<div className={`events-block__list`}>
					{items.map((item) => (
						<CardEvent {...item} key={item.id} />
					))}
				</div>
				{/*/ Events list  */}
			</div>
		</div>
	);
};

EventsBlock.displayName = "EventsBlock";
export default EventsBlock;
