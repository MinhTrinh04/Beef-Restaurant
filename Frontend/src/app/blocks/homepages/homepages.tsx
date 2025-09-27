"use client";
import Image from "next/image";
import { HomePagesListProps, Alignment } from "@/app/types/common.types"; // Import the interface
import { homepagesListData } from "@/app/hooks/data-homepages";
import BlockTitle from "@/app/components/common/block-title/block-title";
import Link from "next/link";

const HomePagesBlock = ({
	items = homepagesListData.items,
	emptyMessage = homepagesListData.emptyMessage,
	defaultAltText = homepagesListData.defaultAltText,
	subtitle = homepagesListData.subtitle,
	title = homepagesListData.title,
	divider = homepagesListData.divider,
	align = homepagesListData.align,
}: HomePagesListProps) => {
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="pages pages--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className="pages bg-coarseWool-800">
			<div className="pages__container">
				<BlockTitle
					subtitle={subtitle}
					title={title || ""}
					divider={divider}
					align={align as Alignment}
					aria-level="2"
				/>
				<div className="pages__content">
					{/* Map over items from props data */}
					{items.map((item) => (
						<div key={item.id} className="pages__item">
							<Link href={item.link}>
								<div className="pages__image">
									<Image
										src={item.image}
										alt={item.altText || defaultAltText}
										width={478}
										height={793}
									/>
								</div>
							</Link>
							<h6 className="pages__title">{item.caption}</h6>
						</div>
					))}
				</div>
			</div>
		</div>
	);
};

export default HomePagesBlock;
