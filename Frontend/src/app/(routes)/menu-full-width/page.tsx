import BadgesList from "@/app/blocks/badges/BadgesList";
import CtaTwo from "@/app/blocks/cta/variants/ctaTwo/ctaTwo";
import { GalleryBasic as Gallery } from "@/app/blocks/gallery";
import MenuImageBlock from "@/app/blocks/menu/menu-image/menuImage";
import { SpecialOffersTwoBlock } from "@/app/blocks/special-offers";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { getMenuCategories } from "@/app/services/menu";
// Data
import { HeroInnerMenuFullData } from "@/app/hooks/data";
import { galleryData } from "@/app/hooks/data-gallery";
import { badgesListData } from "@/app/hooks/data-brands";
import { ctaTwoData } from "@/app/hooks/data-cta";
import { specialOffersData } from "@/app/hooks/data-special-offers";

const PageMenu = async () => {
    // Fetch real menu data from backend
    const menuCategories = await getMenuCategories();

    return (
        <>
            {/* Hero Inner - Block */}
            <HeroInnerBlock
                title={HeroInnerMenuFullData.title}
                image={HeroInnerMenuFullData.image}
                altText={HeroInnerMenuFullData.altText}
                breadcrumbs={HeroInnerMenuFullData.breadcrumbs}
            />
            {/* / Hero Inner - Block */}

            {/* Special Offers - Block */}
            <SpecialOffersTwoBlock {...specialOffersData}/>
            {/* / Special Offers - Block */}

            {/* Menu Image */}
            <MenuImageBlock 
                subtitle="Our Menu"
                title="Delicious Dishes"
                phrase="Explore our carefully crafted menu"
                divider={true}
                items={menuCategories}
                className="menu__container__full"
            />
            {/* / Menu Image */}

            {/* CtaTwo - Block */}
            <CtaTwo {...ctaTwoData}/>
            {/* / CtaTwo - Block */}

            {/* Badges - Block */}
            <BadgesList {...badgesListData} />
            {/* / Badges - Block */}

            {/* Gallery */}
            <Gallery {...galleryData}/>
            {/* / Gallery */}
        </>
    );
};

export default PageMenu;