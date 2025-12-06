import StatusPage from "@/app/components/common/status-page/StatusPage";
import { paymentCancelPageData } from "@/app/hooks/data-general";
import React from "react";

const PaymentCancelPage = () => {
	return <StatusPage {...paymentCancelPageData} />;
};

export default PaymentCancelPage;
