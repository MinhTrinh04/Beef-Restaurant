import Image from "next/image";
import React from "react";
import SocialIcons from "@/app/components/common/social/SocialIcons";
import { CardTeamProps } from "@/app/types/common.types";

const CardTeam = ({ name, position, image, social }: CardTeamProps) => {
	return (
		<div className="card-team">
			<div className="card-team__image">
				<Image src={image} alt={`Photo of ${name}`} width={424} height={468} />
			</div>
			<div className="card-team__info">
				<h4 className="card-team__name">{name}</h4>
				<p className="card-team__position">{position}</p>
				<SocialIcons socials={social} />
			</div>
		</div>
	);
};

export default CardTeam;
