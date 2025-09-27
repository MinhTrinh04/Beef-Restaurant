import Image from "next/image";
import Link from "next/link";
import { ServiceItem } from "@/app/types/common.types";

const IllustrationsCard = ({
	id,
	title,
	image,
	image_hover,
	description,
	link_url,
	link_text,
}: ServiceItem) => {
	return (
		<div className="services-ilustrations__card">
			{image && image_hover && (
				<div className="services-ilustrations__card-ilustration">
					<Image src={image} alt={title} width="120" height="120" />
					<Image
						className="hover"
						src={image_hover}
						alt={title}
						width="120"
						height="120"
					/>
				</div>
			)}
			<div className="services-ilustrations__card-info">
				<h4>{title}</h4>
				<p>{description}</p>
				{/* Link */}
				{link_url && link_text && (
					<Link href={link_url} className="btn btn__link">
						{link_text}
					</Link>
				)}
				{/* / Link */}
			</div>
		</div>
	);
};

export default IllustrationsCard;
