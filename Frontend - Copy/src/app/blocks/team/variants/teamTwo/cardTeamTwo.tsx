import Image from "next/image";
import React from "react";
import SocialIcons from "@/app/components/common/social/SocialIcons";
import { CardTeamProps } from "@/app/types/common.types";

const CardTeamTwo = ({ name, position, image, social }: CardTeamProps) => {
	return (

        <div className="card-team-2">
            <div className="card-team-2__image">
                <Image
                    src={image}
                    alt={`Photo of ${name}`}
                    width={424}
                    height={468}
                />
                <SocialIcons socials={social} />
            </div>
            <div className="card-team-2__info">
                <h4 className="card-team-2__name">{name}</h4>
                <p className="card-team-2__position">{position}</p>
            </div>
        </div>

    );
};

export default CardTeamTwo;
