import HeroInner from "@/app/components/common/hero-inner/Hero-inner";
import { HeroInnerMenuData } from "@/app/hooks/data";
import { SpecialOffersTwoBlock } from "@/app/blocks/special-offers";
import { specialOffersData } from "@/app/hooks/data-special-offers";
import MenuImageBlock from "@/app/blocks/menu/menu-image/menuImage";
import { getMenuCategories } from "@/app/services/menu";
import BadgesList from "@/app/blocks/badges/BadgesList";
import { badgesListData } from "@/app/hooks/data-brands";
import { GalleryBasic as Gallery } from "@/app/blocks/gallery";
import { galleryData } from "@/app/hooks/data-gallery";
import { CtaTwo } from "@/app/blocks/cta";
import { ctaTwoData } from "@/app/hooks/data-cta";

const MenuPage = async () => {
    // Fetch real menu data from backend
    const menuCategories = await getMenuCategories();

    return (
        <main>
            <HeroInner
                title={HeroInnerMenuData.title}
                image={HeroInnerMenuData.image}
                altText={HeroInnerMenuData.altText}
                breadcrumbs={HeroInnerMenuData.breadcrumbs}
            />
            <SpecialOffersTwoBlock {...specialOffersData} />
            <MenuImageBlock
                subtitle="Our Menu"
                title="Delicious Dishes"
                phrase="Explore our carefully crafted menu"
                divider={true}
                items={menuCategories}
            />
            <CtaTwo {...ctaTwoData} />
            <BadgesList {...badgesListData} />
            <Gallery {...galleryData} />
        </main>
    );
};

export default MenuPage;
