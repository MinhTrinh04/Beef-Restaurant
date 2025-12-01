import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { HistoryBlock } from "@/app/blocks/history";
import BadgesList from "@/app/blocks/badges/BadgesList";

// Data
import { HeroInnerHistoryData } from "@/app/hooks/data";
import { badgesListData } from "@/app/hooks/data-brands";
import { historyData } from "@/app/hooks/data-history";
const HistoryPage = () => {
	return (
		<>
			{/* Hero Inner - Block */}
			<HeroInnerBlock
				title={HeroInnerHistoryData.title}
				image={HeroInnerHistoryData.image}
				altText={HeroInnerHistoryData.altText}
				breadcrumbs={HeroInnerHistoryData.breadcrumbs}
			/>
			{/* / Hero Inner - Block */}

			{/* History Block */}
			<HistoryBlock {...historyData} />
			{/* / History Block */}

			{/* Badges Block */}
			<BadgesList {...badgesListData} />
			{/* / Badges Block */}
		</>
	);
};

export default HistoryPage;