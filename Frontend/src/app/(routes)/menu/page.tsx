import HeroInner from "@/app/components/common/hero-inner/Hero-inner";
import { HeroInnerMenuData } from "@/app/hooks/data";
import { SpecialOffersTwoBlock } from "@/app/blocks/special-offers";
import { specialOffersData } from "@/app/hooks/data-special-offers";
import MenuImageBlock from "@/app/blocks/menu/menu-image/menuImage";
import { categoryDishesData } from "@/app/hooks/data-dish";
import BadgesList from "@/app/blocks/badges/BadgesList";
import { badgesListData } from "@/app/hooks/data-brands";
import { GalleryBasic as Gallery } from "@/app/blocks/gallery";
import { galleryData } from "@/app/hooks/data-gallery";
import { CtaTwo } from "@/app/blocks/cta";
import { ctaTwoData } from "@/app/hooks/data-cta";

const MenuPage = () => {
	return (
		<main>
			<HeroInner
				title={HeroInnerMenuData.title}
				image={HeroInnerMenuData.image}
				altText={HeroInnerMenuData.altText}
				breadcrumbs={HeroInnerMenuData.breadcrumbs}
			/>
			<SpecialOffersTwoBlock {...specialOffersData} />
			<MenuImageBlock {...categoryDishesData} />
			<CtaTwo {...ctaTwoData} />
			<BadgesList {...badgesListData} />
			<Gallery {...galleryData} />
		</main>
	);
};

export default MenuPage;