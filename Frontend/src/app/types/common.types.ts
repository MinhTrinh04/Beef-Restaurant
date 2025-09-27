import { IconType } from "react-icons";
import React from "react";
import { StaticImport } from 'next/dist/shared/lib/get-img-props';

export interface NavItem {
	id: number;
	title: string;
	link: string;
	children?: NavItem[];
}

export interface SocialLink {
	href: string;
	ariaLabel: string;
	icon: IconType | React.ElementType;
}

export interface TeamMember {
	id: number;
	name: string;
	position: string;
	image: string;
	social: SocialLink[];
}

export interface TeamBlockProps {
	subtitle?: string;
	title?: string;
	divider?: boolean;
	description?: string;
	items?: TeamMember[];
	className?: string;
	emptyMessage?: string;
}

export interface CardTeamProps {
	name: string;
	position: string;
	image: string;
	social: SocialLink[];
}

export interface SocialIconsProps {
	socials: SocialLink[];
}

export interface CardTeamDescriptionProps {
	urlImage: string;
	altImage: string;
	description: string;
}

export interface TestimonialItem {
	id: number;
	description: string;
	name: string;
	position: string;
}

export interface TestimonialsBlockProps {
	bgImage?: string;
	bgImageAlt?: string;
	comments?: TestimonialItem[];
	prevButtonAriaLabel?: string;
	nextButtonAriaLabel?: string;
	paginationBulletAriaLabel?: string;
}

export interface DishItem {
	id: number | string;
	title: string;
	price: string;
	description: string;
}

export interface MenuCategory {
	id: number | string;
	tag?: string;
	title: string;
	phrase?: string;
	image: string;
	altText?: string;
	dishesList: DishItem[];
	anchor?: string;
	align?: string;
	icon?: string;
	link?: string;
}

export interface MenuImageBlockProps {
	className?: string;
	subtitle: string;
	title:string;
	phrase: string;
	divider: boolean;
	items: MenuCategory[];
	emptyMessage?: string;
}

export interface EventItem {
	id: number;
	image: string;
	subtitle: string;
	title: string;
	pharse: string;
}

export interface EventsBlockProps {
	subtitle?: string;
	title?: string;
	description?: string;
	align?: string;
	items?: EventItem[];
	className?: string;
	listClassName?: string;
	emptyMessage?: string;
}

export interface ServiceItem {
	id: number;
	title: string;
	image?: string;
	image_hover?: string;
	description: string;
	link_url?: string;
	link_text?: string;
}

export interface ServicesListProps {
	items: ServiceItem[];
	emptyMessage?: string;
}

export interface GalleryItem {
	id: number;
	title: string;
	subtitle: string;
	image: string;
	altText?: string;
}

export interface GalleryBasicProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	align?: string;
	divider?: boolean;
	items?: GalleryItem[];
	className?: string;
	listClassName?: string;
	children?: React.ReactNode;
	emptyMessage?: string;
	errorMessage?: string;
}

export interface ItemGalleryProps extends GalleryItem {
	onClick?: () => void;
	listClassName?: string;
	children?: React.ReactNode;
}

export interface ContactFormContent {
	nameLabel: string;
	namePlaceholder: string;
	phoneLabel: string;
	phonePlaceholder: string;
	emailLabel: string;
	emailPlaceholder: string;
	personsLabel: string;
	personsPlaceholder: string;
	dateLabel: string;
	timeLabel: string;
	messageLabel: string;
	messagePlaceholder: string;
	submitButtonText: string;
}

export interface ContactBlockProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	formAction?: string;
	className?: string;
	formContent?: ContactFormContent;
}

export interface Location {
	id: number;
	name: string;
	addressLine1: string;
	addressLine2: string;
	phone: string;
	email: string;
	imageSrc: string;
	imageAltText?: string;
	directionLink: string;
	directionButtonText?: string;
}

export interface LocationsBlockProps {
	title: string;
	description: string;
	items: Location[];
}

export interface HistoryItem {
	id: number;
	year: string;
	title: string;
	description: string;
	imageSrc: string;
}

export interface HistoryBlockProps {
	subtitle: string;
	title: string;
	description: string;
	items: HistoryItem[];
}

export interface BadgeItem {
	id: number;
	image: string;
	altText: string;
}

export interface BadgesListProps {
	items: BadgeItem[];
	emptyMessage?: string;
	defaultAltText?: string;
	carouselAriaLabel?: string;
}

export interface HeroSlideItem {
	id: number;
	image: string;
	altText: string;
	subtitle: string;
	title: string;
	desc: string;
	link: string;
	textLink: string;
}

export interface HeroClassicSliderProps {
	items: HeroSlideItem[];
	emptyMessage?: string;
	mainCarouselAriaLabel?: string;
	prevButtonAriaLabel?: string;
	nextButtonAriaLabel?: string;
	paginationBulletAriaLabel?: string;
	firstSlideMessage?: string;
	lastSlideMessage?: string;
}

export interface OpeningHoursItemProps {
	id: number;
	day: string;
	time: string;
}

export interface OpeningHoursProps {
	title: string;
	schedule: OpeningHoursItemProps[];
}

export interface AboutBlockProps {
	// Data basic
	imageUrl?: string; // a single image
	imageLeftUrl?: string; // for two-column layouts
	imageRightUrl?: string; // for two-column layouts
	altText?: string;
	subtitle?: string;
	title?: string;
	divider?: boolean;
	align?: "left" | "center" | "right";
	descriptionParagraphs?: string[];
	descriptionParagraphsTwo?: string[];
	descriptionParagraphsThree?: string[];
	buttonLink?: string;
	buttonText?: string;

	// Options image
	imageWidth?: number;
	imageHeight?: number;
	priorityImage?: boolean;
	imageQuality?: number;
	imageFill?: boolean;

	// Options layout
	showOpeningHours?: boolean;
	openingHours?: OpeningHoursProps;

	// style and class
	className?: string;
	imageClassName?: string;
	contentClassName?: string;

	// Callbacks
	onImageLoad?: () => void;
	onImageClick?: () => void;
}

export interface SpecialOfferItemProps {
	id: number;
	image: string;
	altText: string;
	title: string;
	price: string;
	link: string;
	description?: string;
	tag?: string;
	description_primary?: string;
	description_secondary?: string;
	linkText?: string;
}

export interface SpecialOffersBlockProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	items?: SpecialOfferItemProps[];
	buttonUrl?: string;
	buttonText?: string;
	divider?: boolean;
	align?: 'left' | 'center' | 'right';
	className?: string;
	slideClassName?: string;
	infoClassName?: string;
	emptyMessage?: string;
	carouselAriaLabel?: string;
	prevButtonAriaLabel?: string;
	nextButtonAriaLabel?: string;
	paginationBulletAriaLabel?: string;
}

export interface WeekSpecialItem {
	id: number;
	title: string;
	price: string;
	description: string;
}

export interface WeekSpecialsProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	divider?: boolean;
	align?: string;
	items?: WeekSpecialItem[];
	buttonUrl?: string;
	buttonText?: string;
	emptyMessage?: string;
}

export interface SliderItemProps extends HeroSlideItem {
	itemActive: number;
}

export interface SliderThumbnailItemProps {
	itemActive: number;
	id: number;
	image: string;
	altText: string;
	onClick: () => void;
}

export interface DotsProps {
	itemActive: number;
	id: number;
	onClick: () => void;
}

export interface CtaProps {
	title?: string;
	description?: string;
	link?: string;
	linkText?: string;
	className?: string;
}

export interface MasonryGalleryProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	align?: string;
	divider?: boolean;
	items?: GalleryItem[];
	className?: string;
	listClassName?: string;
	children?: React.ReactNode;
	emptyMessage?: string;
	lightboxOpenLabel?: string;
}

export interface MasonryItemGalleryProps {
	id: number;
	title: string;
	subtitle: string;
	image: string;
	onClick: () => void;
	onLoad: () => void;
	lightboxOpenLabel?: string;
}

export interface MenuTabsBlockProps {
	items?: MenuCategory[];
	emptyMessage?: string;
}

export interface SidebarNavigationProps {
	navItems: MenuCategory[];
	activeItem: number;
	onNavLinkClick: (
		event: React.MouseEvent<HTMLAnchorElement>,
		index: number,
		anchor: string
	) => void;
}

export interface MenuContentDisplayProps {
	categories: MenuCategory[];
	emptyMessage?: string;
}

export interface TabButtonProps {
	item: MenuCategory;
	index: number;
	activeTab: number;
	onClick: (index: number) => void;
}

export interface TabPanelProps {
	index: number;
	activeTab: number;
	items?: DishItem[];
	emptyMessage?: string;
	id: string;
	ariaLabelledBy: string;
}

export interface StatusPageProps {
	backgroundImageSrc: string;
	backgroundImageAlt: string;
	imageSrc?: string;
	imageAlt?: string;
	imageWidth?: number;
	imageHeight?: number;
	heading: string;
	text: string;
	buttonLink: string;
	buttonText: string;
}

export interface ComingSoonPageContentProps {
    backgroundImage: string;
    backgroundAltText: string;
    logoImage: string;
    logoAltText: string;
    title: string;
    text: string;
    socials: SocialLink[];
    subscriptionFormData: SubscriptionFormProps;
}

export interface Breadcrumb {
	id: number;
	title: string;
	link: string;
}

export type Alignment = "left" | "center" | "right";

export interface BlockTitleProps {
	subtitle?: string;
	title?: string;
	phrase?: string;
	align?: Alignment;
	divider?: boolean;
	className?: string;
}

export interface DividerProps {
	className?: string;
}

export interface DishItemProps {
	title: string;
	price: string;
	description: string;
}

export interface LightBoxProps {
	items: GalleryItem[];
	startIndex: number;
	onClose: () => void;
}

export interface ItemGalleryLightboxProps {
	id: number;
	title: string;
	image: string;
}

export interface ArrowsProps {
	onClickPrev: () => void;
	onClickNext: () => void;
	prevAriaLabel?: string;
	nextAriaLabel?: string;
}

export interface ImageCounterProps {
	current: number;
	total: number;
}

export interface CloseButtonProps {
	onClose: () => void;
	ariaLabel?: string;
}

export interface HeroInnerBlockProps {
	title: string;
	image: string;
	altText: string;
	breadcrumbs: Breadcrumb[];
}

export interface BreadcrumbsProps {
	items: Breadcrumb[];
}

export interface MenuToggleButtonProps {
	isMobile: boolean;
	onToggle: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
	"aria-controls"?: string;
	ariaLabelOpen?: string;
	ariaLabelClose?: string;
}

export interface MenuMobileProps {
	id: string;
	isMobile: boolean;
	setIsMobile: (value: boolean) => void;
	ariaLabelGoBack?: string;
	ariaLabelCloseMenu?: string;
	ariaLabelSubmenu?: string;
	defaultMenuTitle?: string;
}

export interface ContactInfoProps {
	className?: string;
	href: string;
	icon: string;
	text: string;
	ariaLabel?: string;
}

export interface TopbarProps {
	address: string;
	city: string;
	zip: string;
	country:string;
	mapUrl: string;
	phone: string;
	email: string;
}

export interface SubscriptionFormProps {
	placeholder: string;
	buttonAriaLabel: string;
	iconClass: string;
	successMessage: string;
	errorMessage: string;
}


export interface MenuSidebarProps {
	title: string;
} 

export interface HomepageItem {
	id: number;
	image: string;
	altText: string;
	link: string;
	caption: string;
}
export interface HomePagesListProps {
	items: HomepageItem[];
	emptyMessage?: string;
	defaultAltText?: string;
	subtitle?: string;
	title?: string;
	divider?: boolean;
	align?: "left" | "center" | "right";
}