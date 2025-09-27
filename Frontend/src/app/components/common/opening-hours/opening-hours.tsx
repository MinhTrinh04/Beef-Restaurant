import React from "react";
import OpeningHoursItem from "./OpeningHoursItem";
import { OpeningHoursProps } from "@/app/types/common.types";

const OpeningHours: React.FC<OpeningHoursProps> = ({ title, schedule }) => {
	// Early return if schedule is not available or empty
	if (!schedule || schedule.length === 0) {
		return null;
	}

	return (
		<div className="opening-hours">
			<div className="opening-hours__header">
				<h3>{title}</h3>
			</div>
			<div className="opening-hours__body">
				{schedule.map((item) => (
					<OpeningHoursItem
						key={item.id}
						id={item.id}
						day={item.day}
						time={item.time}
					/>
				))}
			</div>
		</div>
	);
};

export default OpeningHours;
