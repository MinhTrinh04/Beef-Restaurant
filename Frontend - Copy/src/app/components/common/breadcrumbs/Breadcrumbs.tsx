import Link from "next/link";
import React from "react";
import { BreadcrumbsProps, Breadcrumb } from "@/app/types/common.types";

const Breadcrumbs: React.FC<BreadcrumbsProps> = ({ items }) => {
	if (!items || items.length === 0) {
		return null;
	}

	return (
		<nav aria-label="Breadcrumb" className="breadcrumbs">
			<ol className="breadcrumbs__list">
				{items.map((item, index) => (
					<li key={item.id} className="breadcrumbs__item">
						<Link href={item.link} className="breadcrumbs__link">
							{item.title}
						</Link>
						{index < items.length - 1 && (
							<span
								className="breadcrumbs__separator"
								aria-hidden="true"
							>
								{" "}
								-{" "}
							</span>
						)}
					</li>
				))}
			</ol>
		</nav>
	);
};

export default Breadcrumbs; 