import { AboutModern, AboutTwoCols } from "@/app/blocks/about";
import ContactBlock from "@/app/blocks/contact/contact";
import { TeamTwoBlock } from "@/app/blocks/team";
import TestimonialsBlock from "@/app/blocks/testimonials/variants/testimonials";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { HeroInnerChefsData } from "@/app/hooks/data";
import { GalleryBasic } from "@/app/blocks/gallery";

{ /* Data */}
import { testimonialsData } from "@/app/hooks/data-testimonials";
import { galleryData } from "@/app/hooks/data-gallery";

const ChefsPage = () => {
	return (
		<>
			{/* Hero Inner - Block */}
			<HeroInnerBlock
				title={HeroInnerChefsData.title}
				image={HeroInnerChefsData.image}
				altText={HeroInnerChefsData.altText}
				breadcrumbs={HeroInnerChefsData.breadcrumbs}
			/>
			{/* / Hero Inner - Block */}

			{/* Team Two Block */}
			<TeamTwoBlock />
			{/* / Team Two Block */}

			{/* Testimonials Block */}
			<TestimonialsBlock {...testimonialsData} />
			{/* / Testimonials Block */}

			{/* Masonry Gallery */}
			<GalleryBasic {...galleryData} />
			{/* / Masonry Gallery */}
		</>
	);
};

export default ChefsPage;
