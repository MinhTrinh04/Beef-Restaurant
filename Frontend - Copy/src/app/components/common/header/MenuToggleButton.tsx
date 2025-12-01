import React from 'react';
import { CgClose } from 'react-icons/cg';
import { GiHamburgerMenu } from 'react-icons/gi';
import { MenuToggleButtonProps } from '@/app/types/common.types';
import { InfoGeneralData } from '@/app/hooks/data-general';

/**
 * A button component that toggles the mobile menu's visibility.
 * It displays a hamburger icon or a close icon based on the menu state.
 *
 * @param {MenuToggleButtonProps} props The props for the component.
 * @returns {React.ReactElement} The rendered toggle button.
 */
const MenuToggleButton: React.FC<MenuToggleButtonProps> = ({
	isMobile,
	onToggle,
	'aria-controls': ariaControls,
	ariaLabelOpen = InfoGeneralData.menuToggleAriaLabelOpen,
	ariaLabelClose = InfoGeneralData.menuToggleAriaLabelClose,
}) => {
	return (
		<button
			className="header__mobile"
			onClick={onToggle}
			aria-expanded={isMobile}
			aria-label={isMobile ? ariaLabelClose : ariaLabelOpen}
			aria-controls={ariaControls}
		>
			{isMobile ? (
				<CgClose className="nav-toggle__icon" />
			) : (
				<GiHamburgerMenu className="nav-toggle__icon" />
			)}
		</button>
	);
};

export default MenuToggleButton; 