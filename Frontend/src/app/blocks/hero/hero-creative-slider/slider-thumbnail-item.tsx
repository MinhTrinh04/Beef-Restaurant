import { cn } from "@/lib/utils";
import Image from "next/image";
import { SliderThumbnailItemProps } from "@/app/types/common.types";

const SliderThumbnailItem = ({
	itemActive,
	id,
	image,
	altText,
	onClick,
}: SliderThumbnailItemProps) => {
	return (
		<li
			role="button"
			onClick={onClick}
			className={cn(
				"slider-hero__thumbnail__item ",
				itemActive === id
					? "slider-hero__thumbnail__item__active"
					: "slider-hero__thumbnail__item__no-active"
			)}
		>
			<div className="slider-hero__thumbnail__image">
				<Image src={image} alt={altText} width={150} height={200} />
			</div>
		</li>
	);
};

export default SliderThumbnailItem;
