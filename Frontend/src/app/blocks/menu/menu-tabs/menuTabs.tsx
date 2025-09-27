"use client";
import React, { useState, useCallback, memo } from "react";
import { MenuTabsBlockProps } from "@/app/types/common.types";
import TabButton from "./tabButton";
import TabPanel from "./TabPanel";

const MenuTabsBlock = ({
	items,
	emptyMessage = "No menu items available.",
}: MenuTabsBlockProps) => {
	const [activeTab, setActiveTab] = useState(0);
	
	const handleTabClick = useCallback((index: number) => {
		setActiveTab(index);
	}, []);

	if (!items?.length) {
		return (
			<div className="menu menu--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className="menu">
			<div className="menu__container">
				<div className="tabs">
					{items.map((item, index) => (
						<TabButton
							key={index}
							item={item}
							index={index}
							activeTab={activeTab}
							onClick={handleTabClick}
						/>
					))}
				</div>

				<div className="tabs-content">
					{items.map((item, index) => (
						<TabPanel
							key={index}
							index={index}
							activeTab={activeTab}
							items={item.dishesList}
							id={`tabpanel-${index}`}
							ariaLabelledBy={`tab-button-${index}`}
						/>
					))}
				</div>
			</div>
		</div>
	);
};

MenuTabsBlock.displayName = "MenuTabsBlock";

export default memo(MenuTabsBlock);
