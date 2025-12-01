import React from "react";
import { OpeningHoursItemProps } from "@/app/types/common.types";

const OpeningHoursItem: React.FC<OpeningHoursItemProps> = ({
	id,
	day,
	time,
}) => {
	// Early return if essential data is missing
	if (!day || !time) {
		return null;
	}

	return (
		<div className="opening-hours__item">
			<div className="opening-hours__item-day">{day}</div>
			<div className="opening-hours__item-time">{time}</div>
		</div>
	);
};

export default OpeningHoursItem;