import React from "react";
import ContactInfo from "./ContactInfo";
import { TopbarProps } from "@/app/types/common.types";
import { InfoGeneralData } from "@/app/hooks/data-general";

/**
 * Renders the top bar of the website, displaying contact information.
 * It uses the ContactInfo component to show address, phone, and email.
 *
 * @param {TopbarProps} props The props for the component, containing contact details.
 * @returns {React.ReactElement | null} The rendered top bar or null if essential data is missing.
 */
const Topbar: React.FC<TopbarProps> = ({
	address,
	city,
	zip,
	country,
	mapUrl,
	phone,
	email,
}) => {
	// Early return if phone or email is not provided.
	if (!phone || !email) {
		return null;
	}

	const fullAddress = `${address}, ${city} ${zip}, ${country}`;
	const { topbar } = InfoGeneralData;

	return (
		<div className="topbar">
			{/** Topbar Left */}
			<div className="topbar__left">
				{/** Address */}
				<ContactInfo
					className="topbar__address"
					href={mapUrl}
					icon={topbar.addressIcon}
					text={fullAddress}
					ariaLabel={`${topbar.addressAriaLabel}: ${fullAddress}`}
				/>
				{/** Address */}
			</div>
			{/** Topbar Left */}
			{/** Topbar Rigth */}
			<div className="topbar__right">
				{/** Phone */}
				<ContactInfo
					className="topbar__phone"
					href={`tel:${phone}`}
					icon={topbar.phoneIcon}
					text={phone}
					ariaLabel={`${topbar.phoneAriaLabel} ${phone}`}
				/>
				{/** Phone */}
				{/** Email */}
				<ContactInfo
					className="topbar__email"
					href={`mailto:${email}`}
					icon={topbar.emailIcon}
					text={email}
					ariaLabel={`${topbar.emailAriaLabel} ${email}`}
				/>
				{/** Email */}
			</div>
			{/** Topbar Rigth */}
		</div>
	);
}

export default Topbar