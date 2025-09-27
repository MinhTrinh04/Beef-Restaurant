import React, { memo } from 'react';
import DishItem from "@/app/components/common/dish/DishItem";
import { TabPanelProps } from '@/app/types/common.types';
import { DishItem as DishItemType } from '@/app/types/common.types';
import { cn } from "@/lib/utils";

const TabPanel: React.FC<TabPanelProps> = ({ index, activeTab, items, emptyMessage, id, ariaLabelledBy }) => (
    <div
        className={cn("tabs-content__item", { active: activeTab === index })}
        id={id}
        role="tabpanel"
        aria-labelledby={ariaLabelledBy}
        hidden={activeTab !== index}
    >
        <div className="menu-block__dishes">
            {items?.map((dish: DishItemType, dishIndex: number) => (
                <DishItem
                    key={dish.id ?? `dish-${index}-${dishIndex}`}
                    title={dish.title}
                    price={dish.price}
                    description={dish.description}
                />
            ))}
        </div>
    </div>
);

TabPanel.displayName = "TabPanel";

export default TabPanel; 