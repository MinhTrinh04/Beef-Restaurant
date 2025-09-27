'use client';

import React from "react";
import ComingSoonPageContent from "@/app/components/common/coming-soon/ComingSoonPageContent";
import { comingSoonData } from "@/app/hooks/data-coming-soon";

const ComingSoonPage = () => {
	return <ComingSoonPageContent {...comingSoonData} />;
};

export default ComingSoonPage;
