import React, { memo } from "react";
import { IconType } from "react-icons"; // Import IconType
import BlockTitle from "@/app/components/common/block-title/block-title";
import CardTeam from "./cardTeamTwo";
import { TeamBlockProps, TeamMember } from "@/app/types/common.types";

// Data
import { teamBlockData } from "@/app/hooks/data-team";

const TeamTwoBlock = memo((props: TeamBlockProps) => {
	const {
		subtitle = teamBlockData.subtitle,
		title = teamBlockData.title,
		divider = teamBlockData.divider,
		description = teamBlockData.description,
		items = teamBlockData.items as TeamMember[],
		className = "",
	} = props;

	if (items.length === 0) {
		return (
			<div className={`team-block team-block--empty ${className}`}>
				<p>No data available.</p>
			</div>
		);
	}

	return (
		<div className="team-block-2 bg-surface">
			<div className="team-block-2__container">
				{/* Info title */}
				<BlockTitle
					subtitle={subtitle}
					title={title}
					divider={divider}
				/>
				{/* Info title */}
			</div>
			{/* Team grid */}
			<div className="team-block-2__grid">
				{/* List Item team */}
				{items.map((item) => (
					<CardTeam
						key={item.id}
						name={item.name}
						position={item.position}
						image={item.image}
						social={item.social}
					/>
				))}
				{/*/ List Item team */}
			</div>
			{/* Team grid */}
		</div>
	);
});

TeamTwoBlock.displayName = "TeamTwoBlock"; // Add display name

export default TeamTwoBlock;
