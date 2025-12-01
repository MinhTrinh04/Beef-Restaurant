import { HeroInnerContactData } from "@/app/hooks/data-contact";
import HeroInnerBlock from "@/app/components/common/hero-inner/Hero-inner";
import ContactBlock from "@/app/blocks/contact/contact";
import LocationsBlock from "@/app/blocks/locations/locations";

// data 
import { contactData } from "@/app/hooks/data-contact";
import { locationsData } from "@/app/hooks/data-contact";


const ContactUsPage = () => {
	return (
		<>
			{/* Hero Inner - Block */}
			<HeroInnerBlock
				title={HeroInnerContactData.title}
				image={HeroInnerContactData.image}
				altText={HeroInnerContactData.altText}
				breadcrumbs={HeroInnerContactData.breadcrumbs}
			/>
			{/* / Hero Inner - Block */}

			{/* Contact Block */}
			<ContactBlock {...contactData} />
			{/* / Contact Block */}

			{/* Locations Block */}
			<LocationsBlock {...locationsData} />
			{/* Locations Block */}
		</>
	);
};

export default ContactUsPage;