"use client";

import Link from "next/link";
import { Minus, Plus, Trash2 } from "lucide-react";
import { useBasket } from "@/app/contexts/BasketContext";

const currency = new Intl.NumberFormat("vi-VN", {
	style: "decimal",
	minimumFractionDigits: 0,
	maximumFractionDigits: 0,
});

const formatVND = (amount: number) => `${currency.format(Math.round(amount))}₫`;

const BasketPageClient = () => {
	const { items, isLoading, isMutating, totalItems, totalCost, updateQuantity, removeItem } =
		useBasket();

	const handleAdjustQuantity = async (productId: number, currentUnits: number, delta: number) => {
		const nextUnits = Math.max(0, currentUnits + delta);
		await updateQuantity(productId, nextUnits);
	};

	const handleRemove = async (productId: number) => {
		await removeItem(productId);
	};

	const hasItems = items.length > 0;

	return (
		<section className="basket-page">
			<div className="basket-page__hero">
				<p className="uppercase tracking-[0.4em] text-xs text-text-muted">Your order</p>
				<h1>Basket</h1>
				<p>
					Review everything you&apos;ve added. Adjust portions, remove dishes, or head straight
					to checkout when you&apos;re ready.
				</p>
			</div>

			<div className="basket-page__grid">
				<div className="basket-page__list" aria-live="polite">
					{isLoading && !hasItems ? (
						<div className="basket-page__empty">
							<p>Loading your selections…</p>
						</div>
					) : null}

					{!isLoading && !hasItems ? (
						<div className="basket-page__empty">
							<p>Your basket is empty. Hungry for something new?</p>
							<Link href="/menu" className="btn btn__default mt-6 inline-flex">
								Browse the menu
							</Link>
						</div>
					) : null}

					{hasItems &&
						items.map((item) => (
							<article key={item.productId} className="basket-card">
								<div className="basket-card__info">
									<h3>{item.productName}</h3>
									<p>
										{formatVND(item.unitPrice)} &middot;{" "}
										{item.units} serving{item.units > 1 ? "s" : ""}
									</p>
								</div>
								<div className="basket-card__price">
									{formatVND(item.unitPrice * item.units)}
								</div>
								<div className="basket-card__controls">
									<div className="basket-card__qty" aria-label="Quantity selector">
										<button
											type="button"
											onClick={() => handleAdjustQuantity(item.productId, item.units, -1)}
											disabled={isMutating}
											aria-label={`Decrease ${item.productName}`}
										>
											<Minus size={14} />
										</button>
										<span>{item.units}</span>
										<button
											type="button"
											onClick={() => handleAdjustQuantity(item.productId, item.units, 1)}
											disabled={isMutating}
											aria-label={`Increase ${item.productName}`}
										>
											<Plus size={14} />
										</button>
									</div>
									<button
										type="button"
										className="basket-card__remove inline-flex items-center gap-2"
										onClick={() => handleRemove(item.productId)}
										disabled={isMutating}
									>
										<Trash2 size={14} />
										Remove
									</button>
								</div>
							</article>
						))}
				</div>

				<aside className="basket-page__summary">
					<div className="basket-summary">
						<div className="basket-summary__row">
							<span>Items</span>
							<span>{totalItems}</span>
						</div>
						<div className="basket-summary__row">
							<span>Subtotal</span>
							<span>{formatVND(totalCost)}</span>
						</div>
						<div className="basket-summary__row">
							<span>Service</span>
							<span>Calculated at checkout</span>
						</div>
						<div className="basket-summary__total">
							<span>Total</span>
							<span>{formatVND(totalCost)}</span>
						</div>
						<Link href="/checkout" className="w-full">
							<button
								type="button"
								className="btn btn__solid basket-summary__cta w-full"
								disabled={!hasItems || isMutating}
							>
								Proceed to checkout
							</button>
						</Link>
						<p className="text-xs text-text-muted text-center">
							Need to add more?{" "}
							<Link href="/menu" className="text-primary underline-offset-4 underline">
								Continue browsing
							</Link>
						</p>
					</div>
				</aside>
			</div>
		</section>
	);
};

export default BasketPageClient;

