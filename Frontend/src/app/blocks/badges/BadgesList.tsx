"use client";
import Image from "next/image";
import { A11y, Autoplay } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import { BadgesListProps } from "@/app/types/common.types"; // Import the interface
import { badgesListData } from "@/app/hooks/data-brands";

const BadgesList = ({
	items = badgesListData.items,
	emptyMessage = badgesListData.emptyMessage,
	defaultAltText = badgesListData.defaultAltText,
	carouselAriaLabel = badgesListData.carouselAriaLabel,
}: BadgesListProps) => {
	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="badges badges--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<div className="badges">
			{/* Badges list */}
			<Swiper
				className="badges__list"
				slidesPerView={2}
				breakpoints={{
					480: { slidesPerView: 3, spaceBetween: 10 },
					640: { slidesPerView: 4, spaceBetween: 15 },
					992: { slidesPerView: 4, spaceBetween: 20 },
					1200: { slidesPerView: 5, spaceBetween: 20 },
				}}
				spaceBetween={20}
				modules={[A11y, Autoplay]}
				loop={true}
				speed={6000}
				autoplay={{
					delay: 0,
					disableOnInteraction: false,
					pauseOnMouseEnter: false,
				}}
				allowTouchMove={false}
				freeMode={{
					enabled: true,
					momentum: false,
				}}
				a11y={{
					enabled: true,
					containerMessage: carouselAriaLabel,
				}}
			>
				{/* Map over items from props data */}
				{items.map((item) => (
					<SwiperSlide key={item.id}>
						<div className="badges__item">
							<Image
								src={item.image}
								alt={item.altText || defaultAltText}
								width={180}
								height={180}
							/>
						</div>
					</SwiperSlide>
				))}
			</Swiper>
		</div>
	);
};

export default BadgesList;
