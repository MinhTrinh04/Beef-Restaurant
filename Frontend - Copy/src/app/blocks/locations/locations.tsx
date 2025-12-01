import React from 'react';
import BlockTitle from "../../components/common/block-title/block-title";
import { LocationItem } from './cardLocation';

 // Interface
import { LocationsBlockProps } from '@/app/types/common.types';

const LocationsBlock = ({title, items}: LocationsBlockProps) => {
  return (
		<div className="location">
			<div className="location__container">
				{/* Section title */}
				<BlockTitle title={title} align="center" divider={true} />
				{/*/ Section title */}

				{/* Location list */}
				<div className="location__list">
					{items.map((location) => (
						<LocationItem key={location.id} {...location} />
					))}
				</div>
				{/*/ Location list */}
			</div>
		</div>
  );
}

export default LocationsBlock;