"use client";
import { useState } from "react";
import Image from "next/image";
import { Navigation, Pagination, A11y } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import { motion } from "framer-motion";
import Link from "next/link";

// interface
import { HeroClassicSliderProps } from "@/app/types/common.types";
import { sliderHeroData } from "@/app/hooks/data-hero";

const HeroClassicSliderBlock = ({
	items = sliderHeroData.items,
	emptyMessage = sliderHeroData.emptyMessage,
	mainCarouselAriaLabel = sliderHeroData.mainCarouselAriaLabel,
	prevButtonAriaLabel = sliderHeroData.prevButtonAriaLabel,
	nextButtonAriaLabel = sliderHeroData.nextButtonAriaLabel,
	paginationBulletAriaLabel = sliderHeroData.paginationBulletAriaLabel,
	firstSlideMessage = sliderHeroData.firstSlideMessage,
	lastSlideMessage = sliderHeroData.lastSlideMessage,
}: HeroClassicSliderProps) => {
	const [activeSlide, setActiveSlide] = useState(0);

	// Improved data validation check
	if (!items || items.length === 0) {
		return (
			<div className="hero-classic hero-classic--empty">
				<p>{emptyMessage}</p>
			</div>
		);
	}

	const swiperConfig = {
		breakpoints: {
			320: {
				slidesPerView: 1,
				spaceBetween: 0,
			},
			480: {
				slidesPerView: 1,
				spaceBetween: 10,
			},
			768: {
				slidesPerView: 1,
				spaceBetween: 0,
			},
			1024: {
				slidesPerView: 1,
				spaceBetween: 30,
			},
		},
		freeMode: false,
		pagination: { clickable: true },
		navigation: true,
		modules: [Navigation, Pagination, A11y],
		loop: true,
		speed: 1000,
		effect: "fade",
		fadeEffect: { crossFade: true },
	};

	const animationConfig = {
		wrapper: {
			initial: { opacity: 0, y: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { duration: 1, ease: "easeOut" }
		},
		content: {
			initial: { opacity: 0, y: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { duration: 0.8 }
		},
		subtitle: {
			initial: { opacity: 0, y: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { delay: 0.3, duration: 0.8 }
		},
		title: {
			initial: { opacity: 0, y: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { delay: 0.5, duration: 0.8 }
		},
		description: {
			initial: { opacity: 0, y: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { delay: 0.7, duration: 0.8 }
		},
		button: {
			initial: { opacity: 0 },
			animate: (activeSlide: number, index: number) => ({
				opacity: activeSlide === index ? 1 : 0,
			}),
			transition: { delay: 0.9, duration: 0.5 }
		}
	};

	return (
		<section
			id="hero-classic-slider"
			className={`hero-classic bg-background`}
		>
			<div className="hero-classic__container">
				{/* Slider hero classic */}
				<Swiper
					className="hero-classic__slider"
					breakpoints={swiperConfig.breakpoints}
					freeMode={swiperConfig.freeMode}
					pagination={{
						clickable: swiperConfig.pagination.clickable,
					}}
					navigation={swiperConfig.navigation}
					modules={swiperConfig.modules}
					loop={swiperConfig.loop}
					speed={swiperConfig.speed}
					effect={swiperConfig.effect}
					fadeEffect={{
						crossFade: swiperConfig.fadeEffect.crossFade,
					}}
					onSlideChange={(swiper) => setActiveSlide(swiper.realIndex)}
					a11y={{
						enabled: true,
						prevSlideMessage: prevButtonAriaLabel,
						nextSlideMessage: nextButtonAriaLabel,
						paginationBulletMessage:
							paginationBulletAriaLabel.replace(
								"{index}",
								"{{index}}"
							),
						firstSlideMessage: firstSlideMessage,
						lastSlideMessage: lastSlideMessage,
						slideRole: "group",
						containerRole: "region",
						containerMessage: mainCarouselAriaLabel,
					}}
				>
					{items.map((item, index) => (
						<SwiperSlide
							key={item.id}
							className="hero-classic__slide"
						>
							<div className="hero-basic hero-center">
								<div className="hero-basic__background">
									<div className="hero-basic__overlayer"></div>
									<Image
										src={item.image}
										alt={item.altText}
										width={1920}
										height={1080}
										priority={index === 0}
										loading={index === 0 ? "eager" : "lazy"}
										quality={90}
										sizes="(max-width: 768px) 100vw, (max-width: 1200px) 80vw, 70vw"
									/>
								</div>
								<motion.div
									className="hero-basic__wrapper"
									initial={animationConfig.wrapper.initial}
									animate={animationConfig.wrapper.animate(
										activeSlide,
										index
									)}
									transition={
										animationConfig.wrapper.transition
									}
								>
									<div className="hero-basic__content">
										<motion.p
											className="hero-basic__subtitle"
											initial={
												animationConfig.subtitle.initial
											}
											animate={animationConfig.subtitle.animate(
												activeSlide,
												index
											)}
											transition={
												animationConfig.subtitle
													.transition
											}
										>
											{item.subtitle}
										</motion.p>
										<motion.h1
											className="hero-basic__title"
											initial={
												animationConfig.title.initial
											}
											animate={animationConfig.title.animate(
												activeSlide,
												index
											)}
											transition={
												animationConfig.title.transition
											}
										>
											{item.title}
										</motion.h1>
										{/* Divider */}
										<div
											className="divider div-transparent div-stopper wow animate__fadeInUp"
											data-wow-delay="0.7s"
										></div>
										{/*/ Divider */}
										<motion.div
											className="hero-basic__description"
											initial={
												animationConfig.description
													.initial
											}
											animate={animationConfig.description.animate(
												activeSlide,
												index
											)}
											transition={
												animationConfig.description
													.transition
											}
										>
											<p>{item.desc}</p>

											<Link
												className="btn btn__default"
												href={item.link}
											>
												{item.textLink}
											</Link>
										</motion.div>
									</div>
								</motion.div>
							</div>
						</SwiperSlide>
					))}
				</Swiper>
				{/* Slider hero classic */}
			</div>
		</section>
	);
};

HeroClassicSliderBlock.displayName = "HeroClassicSliderBlock";
export default HeroClassicSliderBlock;
