"use client";
import React from "react";
import Image from "next/image";
import { Navigation, Pagination, A11y, Autoplay } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import {
	TestimonialsBlockProps,
	TestimonialItem,
} from "@/app/types/common.types";
import { testimonialsData } from "@/app/hooks/data-testimonials";

const TestimonialsBlock = (props: TestimonialsBlockProps) => {
	const {
		bgImage = testimonialsData.bgImage,
		bgImageAlt = testimonialsData.bgImageAlt,
		comments = testimonialsData.comments as TestimonialItem[],
		prevButtonAriaLabel = testimonialsData.prevButtonAriaLabel,
		nextButtonAriaLabel = testimonialsData.nextButtonAriaLabel,
		paginationBulletAriaLabel = testimonialsData.paginationBulletAriaLabel,
	} = props;

	// If there are no comments, don't render the component
	if (!comments || comments.length === 0) {
		return null;
	}

	return (
		<div className="testimonials">
			{/* Background - Use data from props */}
			<div className="testimonials__background">
				<Image src={bgImage} alt={bgImageAlt} fill priority />
			</div>
			{/* Background */}
			<div className="testimonials__container">
				<Swiper
					className="testimonials__slider"
					slidesPerView={1}
					pagination={{ clickable: true }}
					navigation={true}
					modules={[Navigation, Pagination, A11y, Autoplay]}
					loop={true}
					autoplay={{
						delay: 5000,
						disableOnInteraction: false,
						pauseOnMouseEnter: true,
					}}
					a11y={{
						prevSlideMessage: prevButtonAriaLabel,
						nextSlideMessage: nextButtonAriaLabel,
						paginationBulletMessage: paginationBulletAriaLabel.replace(
							"{index}",
							"{{index}}"
						),
					}}
				>
					{/* Map over comments from props data */}
					{comments.map((item) => (
						<SwiperSlide key={item.id}>
							<div className="testimonials__slider__item">
								<div className="testimonials__slider__item-comment">
									<p>{item.description}</p>
								</div>
								<div className="testimonials__slider__item-author">
									<strong>{item.name}</strong>
									<p>{item.position}</p>
								</div>
							</div>
						</SwiperSlide>
					))}
				</Swiper>
			</div>
		</div>
	);
};

export default TestimonialsBlock;
