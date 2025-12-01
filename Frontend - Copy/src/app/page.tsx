import { AboutClassic } from "@/app/blocks/about";
import BadgesList from "@/app/blocks/badges/BadgesList";
import Cta from "@/app/blocks/cta/variants/ctaOne/ctaOne";
import { SpecialOffersTwoBlock } from "@/app/blocks/special-offers";
import { TeamOneBlock as TeamBlock } from "@/app/blocks/team";
import WeekSpecials from "@/app/blocks/menu/menu-week-specials/menuWeekSpecials";
import { HeroClassicSliderBlock } from "@/app/blocks/hero";
import { MasonryGallery } from "@/app/blocks/gallery";
import {ServicesList} from "@/app/blocks/services/";

// Data
import { badgesListData } from "@/app/hooks/data-brands";
import { sliderHeroData } from "@/app/hooks/data-hero";
import { aboutData } from "@/app/hooks/data-about";
import { specialOffersData } from "@/app/hooks/data-special-offers";
import { menuWeekData } from "@/app/hooks/data-dish";
import { ServiceCardListData } from "@/app/hooks/data-services";
import { ctaData } from "@/app/hooks/data-cta";
import { galleryData } from "@/app/hooks/data-gallery";
import { teamBlockData } from "@/app/hooks/data-team";


const PageHomeClassic = () => {
    return (
		<>
			{/* Slider Hero - Block */}
			<HeroClassicSliderBlock {...sliderHeroData} />
			{/* / Slider Hero - Block */}

			{/* About - Block */}
			<AboutClassic {...aboutData} />
			{/* / About - Block */}

			{/* Special Offers - Block */}
			<SpecialOffersTwoBlock {...specialOffersData} />
			{/* / Special Offers - Block */}

			{/* Badges - Block */}
			<BadgesList {...badgesListData} />
			{/* / Badges - Block */}

			{/* Week Specials - Block */}
			<WeekSpecials {...menuWeekData} />
			{/* / Week Specials - Block */}

			{/* Illustrations Card - List - Block */}
			<ServicesList {...ServiceCardListData} />
			{/* / Illustrations Card - List - Block */}

			{/* CTA - Block */}
			<Cta {...ctaData} />
			{/* / CTA - Block */}

			{/* Masonry Gallery */}
			<MasonryGallery {...galleryData} />
			{/* / Masonry Gallery */}

			{/* Team */}
			<TeamBlock {...teamBlockData} />
			{/* / Team */}
		</>
	);
};

export default PageHomeClassic;
