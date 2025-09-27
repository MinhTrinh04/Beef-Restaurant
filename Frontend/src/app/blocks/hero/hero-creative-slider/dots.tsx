import { cn } from "@/lib/utils";
import { DotsProps } from "@/app/types/common.types";

const Dots = ({ itemActive, id, onClick }: DotsProps) => {
	return (
		<li
			role="button"
			onClick={onClick}
			className={cn(
				"slider-dots ",
				itemActive === id ? "active" : "not-active"
			)}
		>
			<div className="slider-button"></div>
		</li>
	);
};

export default Dots;
