"use client";

import ServiceCard from "../../ServiceCard";

// interface
import { ServicesListProps } from "@/app/types/common.types";

const ServicesList = ({
	items,
	emptyMessage = "No services available at the moment.",
}: ServicesListProps) => { 
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="services-ilustrations services-ilustrations--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}
	return (
		<div className="services-ilustrations">
			<div className="services-ilustrations__container">
				<div className="services-ilustrations__content">
						{items.map((item) => (
								<ServiceCard
									id={item.id}
									key={item.id}
									title={item.title}
									image={item.image || ""}
									image_hover={item.image_hover || ""}
									description={item.description}
									link_url={item.link_url || ""} 
									link_text={item.link_text || ""}
								/>
						))}
				</div>
			</div>
		</div>
	);
};

export default ServicesList;
