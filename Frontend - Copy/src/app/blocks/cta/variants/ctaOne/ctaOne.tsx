import Link from "next/link";
import { memo } from "react";

// interface
import { CtaProps } from "@/app/types/common.types";

const CtaOne = ({ title, description, link, linkText, className }: CtaProps) => {

	return (
		<div className={`cta ${className || ''}`}>
			<div className="cta__overlayer"></div>
			<div className="cta__container">
				<div className="cta__content">
					<div className="cta__header">
						<h2>{title}</h2>
					</div>
					<div className="cta__body">
						<p>{description}</p>
					</div>
					<div className="cta__footer">
						<Link href={link || '#'} className="btn btn__default">
							{linkText}
						</Link>
					</div>
				</div>
			</div>
		</div>
	);
};

CtaOne.displayName = "CtaOne";
export default CtaOne;
