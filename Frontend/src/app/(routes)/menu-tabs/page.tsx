import BadgesList from "@/app/blocks/badges/BadgesList";
import CtaTwo from "@/app/blocks/cta/variants/ctaTwo/ctaTwo";
import { GalleryBasic as Gallery } from "@/app/blocks/gallery";
import { MenuTabsBlock } from "@/app/blocks/menu";
import { SpecialOffersTwoBlock } from "@/app/blocks/special-offers";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { getMenuCategories } from "@/app/services/menu";

// Data
import { badgesListData } from "@/app/hooks/data-brands";
import { HeroInnerMenuTabsData } from "@/app/hooks/data";
import { specialOffersData } from "@/app/hooks/data-special-offers";
import { ctaTwoData } from "@/app/hooks/data-cta";
import { galleryData } from "@/app/hooks/data-gallery";

const PageMenu = async () => {
    // Fetch real menu data from backend
    const menuCategories = await getMenuCategories();

    return (
        <div className="menu-page menu-tabs">
            {/* Hero Inner - Block */}
            <HeroInnerBlock
                title={HeroInnerMenuTabsData.title}
                image={HeroInnerMenuTabsData.image}
                altText={HeroInnerMenuTabsData.altText}
                breadcrumbs={HeroInnerMenuTabsData.breadcrumbs}
            />
            {/* / Hero Inner - Block */}

            {/* Menu Tabs */}
            <MenuTabsBlock items={menuCategories} />
            {/* / Menu Tabs */}

            {/* Special Offers - Block */}
            <SpecialOffersTwoBlock {...specialOffersData} />
            {/* / Special Offers - Block */}

            {/* CtaTwo - Block */}
            <CtaTwo {...ctaTwoData} />
            {/* / CtaTwo - Block */}

            {/* Badges - Block */}
            <BadgesList {...badgesListData} />
            {/* / Badges - Block */}

            {/* Gallery */}
            <Gallery {...galleryData} />
            {/* / Gallery */}
        </div>
    );
};

export default PageMenu;
