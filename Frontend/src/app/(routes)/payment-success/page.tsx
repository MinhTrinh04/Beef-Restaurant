import StatusPage from "@/app/components/common/status-page/StatusPage";
import { paymentSuccessPageData } from "@/app/hooks/data-general";
import React from "react";

const PaymentSuccessPage = () => {
	return <StatusPage {...paymentSuccessPageData} />;
};

export default PaymentSuccessPage;
