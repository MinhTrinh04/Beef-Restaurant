"use client";
import React, { useState } from "react";
import Image from "next/image";
import {
	EffectFade, Navigation,
	Pagination,
	Scrollbar,
	A11y,
} from "swiper/modules";
import BlockTitle from "@/app/components/common/block-title/block-title";
import { Swiper, SwiperSlide } from "swiper/react";
import Link from "next/link";
import { cn } from "@/lib/utils";

// Interface
import { SpecialOffersBlockProps } from "@/app/types/common.types";
import { specialOffersData } from "@/app/hooks/data-special-offers";


const SpecialOffersTwoBlock = ({
	subtitle = specialOffersData.subtitle,
	title = specialOffersData.title,
	phrase = specialOffersData.phrase,
	items = specialOffersData.items,
	emptyMessage = specialOffersData.emptyMessage,
	carouselAriaLabel = specialOffersData.carouselAriaLabel,
	prevButtonAriaLabel = specialOffersData.prevButtonAriaLabel,
	nextButtonAriaLabel = specialOffersData.nextButtonAriaLabel,
}: SpecialOffersBlockProps) => {
	const [activeSlide, setActiveSlide] = useState(0);

	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="special-offers special-offers--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	return (
		<section id="special-offers-two" className={`special-offers-two bg-background`}>
			<div className="special-offers-two__container">
				{/* Info title */}
				<BlockTitle subtitle={subtitle} title={title ?? ''} phrase={phrase} />
				{/* Info title */}

				{/* Slider Special Offers */}
				<Swiper
					className="special-offers-two__slider"
					breakpoints={{
						320: { slidesPerView: 1 },
						768: { slidesPerView: 1 },
						1024: { slidesPerView: 1 },
					}}
					effect={"fade"}
					navigation={true}
					modules={[EffectFade, Navigation, A11y]}
					loop={false}
					onSlideChange={(swiper) => setActiveSlide(swiper.realIndex)}
					a11y={{
						enabled: true,
						prevSlideMessage: prevButtonAriaLabel,
						nextSlideMessage: nextButtonAriaLabel,
						containerMessage: carouselAriaLabel,
					}}
				>
					{items.map((item, index) => (
						<SwiperSlide key={item.id} className={index === activeSlide ? "swiper-slide-active" : ""}>
							<div className="special-offers-two__slider__item">
								<div className="special-offers-two__slider__image">
									<Image
										src={item.image}
										alt={item.altText}
										layout="fill"
										objectFit="cover"
									/>
								</div>

								<div
									className={cn("special-offers-two__slider__info", {
										active: index === activeSlide,
									})}
								>
									<span className="tag">{item.tag}</span>
									<h3 className="special-offers-two__slider__title">
										{item.title} <span>{item.price}</span>
									</h3>

									<p className="special-offers-two__slider__primary">
										{item.description_primary}
									</p>
									<p className="special-offers-two__slider__secondary">
										{item.description_secondary}
									</p>

									<Link
										href={item.link}
										className="btn btn__link"
									>
										{item.linkText || "add order"}
									</Link>
								</div>
							</div>
						</SwiperSlide>
					))}
				</Swiper>
				{/* Slider Special Offers */}
			</div>
		</section>
	);
};

export default SpecialOffersTwoBlock;
