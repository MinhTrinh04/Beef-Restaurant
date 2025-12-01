import Link from "next/link";
import { memo } from "react";

// interface
import { CtaProps } from "@/app/types/common.types";

const CtaTwo = ({title, description, link, linkText}: CtaProps) => {
	return (
		<div className={`cta-2 cta-2__right`}>
			<div className="cta-2__overlayer"></div>
			<div className="cta-2__container">
				<div className="cta-2__content">
					<div className="cta-2__header">
						<h2>{title}</h2>
					</div>
					<div
						className="divider div-transparent div-stopper wow animate__fadeInUp"
						data-wow-delay="0.7s"
					></div>
					<div className="cta-2__body">
						<p>{description}</p>
					</div>
					<div className="cta-2__footer">
						<Link href={link || '#'} className="btn btn__default">
							{linkText}
						</Link>
					</div>
				</div>
			</div>
		</div>
	);
};

CtaTwo.displayName = "CtaTwo";
export default CtaTwo;
