import StatusPage from "@/app/components/common/status-page/StatusPage";
import { confirmationPageData } from "@/app/hooks/data-general";
import React from "react";

const ConfirmationPage = () => {
	return <StatusPage {...confirmationPageData} />;
};

export default ConfirmationPage;
