import {
	AboutClassic,
	AboutModern,
	AboutTwoCols,
} from "@/app/blocks/about";
import ContactBlock from "@/app/blocks/contact/contact";
import { TeamTwoBlock } from "@/app/blocks/team";
import { TestimonialsBlock } from "@/app/blocks/testimonials/";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { MasonryGallery } from "@/app/blocks/gallery";
import BadgesList from "@/app/blocks/badges/BadgesList";

// Import Data
import { HeroInnerAboutData } from "@/app/hooks/data";
import { testimonialsData } from "@/app/hooks/data-testimonials";
import { badgesListData } from "@/app/hooks/data-brands";
import { aboutData, aboutTwoData } from "@/app/hooks/data-about";
import { contactData } from "@/app/hooks/data-contact";
import { galleryData } from "@/app/hooks/data-gallery";

const AboutPage = () => {
	return (
		<>
			{/* Hero Inner - Block */}
			<HeroInnerBlock
				title={HeroInnerAboutData.title}
				image={HeroInnerAboutData.image}
				altText={HeroInnerAboutData.altText}
				breadcrumbs={HeroInnerAboutData.breadcrumbs}
			/>
			{/* / Hero Inner - Block */}

			{/* About Classic - Block */}
			<AboutClassic {...aboutData} />
			{/* / About Classic - Block */}

			{/* About Modern - Block */}
			<AboutTwoCols {...aboutTwoData} />
			{/* / About Modern - Block */}

			{/* Badges Block */}
			<BadgesList {...badgesListData} />
			{/* / Badges Block */}

			{/* Testimonials Block */}
			<TestimonialsBlock {...testimonialsData} />
			{/* / Testimonials Block */}

			{/* Team Two Block */}
			<TeamTwoBlock />
			{/* / Team Two Block */}

			{/* Contact Block */}
			<ContactBlock {...contactData} />
			{/* / Contact Block */}

			{/* Masonry Gallery */}
			<MasonryGallery {...galleryData} />
			{/* / Masonry Gallery */}
		</>
	);
};

export default AboutPage;
