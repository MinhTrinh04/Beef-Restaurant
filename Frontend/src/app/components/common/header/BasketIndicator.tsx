"use client";

import Link from "next/link";
import { ShoppingBag } from "lucide-react";
import { useBasket } from "@/app/contexts/BasketContext";

const BasketIndicator = () => {
	const { totalItems, totalCost, isLoading, isMutating } = useBasket();
	const isPending = isLoading || isMutating;
	const formattedTotal =
		totalCost > 0
			? new Intl.NumberFormat("vi-VN", {
				style: "currency",
				currency: "VND",
				maximumFractionDigits: 0,
			}).format(totalCost)
			: "0₫";

	return (
		<Link className="basket-indicator" href="/basket">
			<ShoppingBag size={18} aria-hidden="true" />
			<div className="basket-indicator__details">
				<span className="basket-indicator__label">Basket</span>
				<span className="basket-indicator__total">{formattedTotal}</span>
			</div>
			<span className="basket-indicator__count" aria-live="polite">
				{isPending ? "…" : totalItems}
			</span>
		</Link>
	);
};

export default BasketIndicator;

