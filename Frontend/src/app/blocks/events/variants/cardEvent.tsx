import Image from "next/image";
import React from "react";

// interface
import { EventItem } from "@/app/types/common.types";

const CardEvent = ({ image, title, subtitle, pharse }: EventItem) => {
	return (
		<>
			{/* Events Item  */}
			<div className="card-event">
				{/* className=  */}
				<div className="card-event-image">
					<Image src={image} alt={title} width={500} height={500} />
				</div>
				{/*/ className=  */}
				{/* Info  */}
				<div className="card-event-info">
					<h6 className="card-event-info__subtitle">{subtitle}</h6>
					<h5 className="card-event-info__title">{title}</h5>
					<p>{pharse}</p>
				</div>
				{/*/ Info  */}
			</div>
			{/* Events Item  */}
		</>
	);
};

export default CardEvent;
