import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import { HeroInnerServicesData } from "@/app/hooks/data";

import ContactBlock from "@/app/blocks/contact/contact";
import IllustrationsListTwo from "@/app/blocks/services/variants/servicesListTwo/ServicesListTwo";
import { TestimonialsBlock } from "@/app/blocks/testimonials/";
import { GalleryBasic } from "@/app/blocks/gallery";
import { EventsBlock } from "@/app/blocks/events";

// Data
import { testimonialsData } from "@/app/hooks/data-testimonials";
import { ServiceCardListData } from "@/app/hooks/data-services";
import { contactData } from "@/app/hooks/data-contact";
import { galleryData } from "@/app/hooks/data-gallery";
import { eventsBlockData } from "@/app/hooks/data-events";

const ServicesPage = () => {
	return (
		<>
			{/* Hero Inner - Block */}
			<HeroInnerBlock
				title={HeroInnerServicesData.title}
				image={HeroInnerServicesData.image}
				altText={HeroInnerServicesData.altText}
				breadcrumbs={HeroInnerServicesData.breadcrumbs}
			/>
			{/* / Hero Inner - Block */}

			{/* Illustrations Card - List - Block */}
			<IllustrationsListTwo {...ServiceCardListData} />
			{/* / Illustrations Card - List - Block */}

			{/* Events Block - Box */}
			<EventsBlock {...eventsBlockData}/>
			{/* Events Block - Box */}

			{/* Testimonials Block */}
			<TestimonialsBlock {...testimonialsData} />
			{/* / Testimonials Block */}

			{/* Masonry Gallery */}
			<GalleryBasic {...galleryData}/>
			{/* / Masonry Gallery */}

			{/* Contact Block */}
			<ContactBlock {...contactData} />
			{/* / Contact Block */}
		</>
	);
};

export default ServicesPage;