"use client";
import React, { useEffect, useState } from "react";
import { InfoGeneralData } from "@/app/hooks/data-general";
import throttle from 'lodash.throttle';

const SCROLL_THRESHOLD = 20;
const SCROLL_THROTTLE_LIMIT = 200;

const ScrollToTopButton: React.FC = () => {
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const handleScroll = () => {
            setIsVisible(window.scrollY > SCROLL_THRESHOLD);
        };

        const throttledScrollHandler = throttle(handleScroll, SCROLL_THROTTLE_LIMIT);

        window.addEventListener("scroll", throttledScrollHandler);
        handleScroll(); 

        return () => {
            window.removeEventListener("scroll", throttledScrollHandler);
        };
    }, []);

    const handleScrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
    };

    if (!isVisible) {
        return null;
    }

    return (
        <button
            className="btn-to-top"
            onClick={handleScrollToTop}
            aria-label={InfoGeneralData.scrollToTopLabel}
            title={InfoGeneralData.scrollToTopLabel}
        >
            <i className={InfoGeneralData.scrollToTopIconClasses}></i>
        </button>
    );
};

export default ScrollToTopButton; 