import React from "react";
import Image from "next/image";
import { CardTeamDescriptionProps } from "@/app/types/common.types";

const CardTeamDescription = ({
	urlImage,
	altImage,
	description,
}: CardTeamDescriptionProps) => {
	return (
		<div className="team-block__description bg-surface-dark">
			<div className="team-block__icon">
				<Image
					src={urlImage}
					alt={altImage}
					width="54"
					height="54"
				/>
			</div>
			<div className="team-block__text">
				<p>{description}</p>
			</div>
		</div>
	);
};

export default CardTeamDescription;
