"use client";
import Image from "next/image";
import { cn } from "@/lib/utils";
import React from "react";
import { ItemGalleryLightboxProps } from "@/app/types/common.types";

const ItemGalleryLightbox: React.FC<ItemGalleryLightboxProps> = ({
	title,
	image,
}) => {
	return (
		<Image
			className={cn("lightbox-img lightbox-img__show")}
			src={image}
			alt={title}
			width={624}
			height={390}
		/>
	);
};

export default ItemGalleryLightbox;
