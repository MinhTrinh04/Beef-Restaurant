import React, { memo } from 'react';
import { TabButtonProps } from '@/app/types/common.types';

const TabButton = memo(({ item, index, activeTab, onClick }: TabButtonProps) => (
	<button
		role="tab"
		id={`tab-button-${index}`}
		className={`tabs__link ${activeTab === index ? "active" : ""}`}
		data-tab={index}
		onClick={() => onClick(index)}
		aria-selected={activeTab === index}
		aria-controls={`tabpanel-${index}`}
	>
		{item.title}
	</button>
));

TabButton.displayName = "TabButton";

export default TabButton;
