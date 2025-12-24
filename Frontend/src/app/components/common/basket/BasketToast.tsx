"use client";

import { useEffect, type ComponentType, type ComponentPropsWithoutRef } from "react";
import { AlertTriangle, CheckCircle2, X } from "lucide-react";
import { cn } from "@/lib/utils";

type Variant = "success" | "error";

const icons: Record<Variant, ComponentType<ComponentPropsWithoutRef<"svg">>> = {
	success: CheckCircle2,
	error: AlertTriangle,
};

export interface BasketToastProps {
	toast: {
		id: number;
		message: string;
		detail?: string;
		variant: Variant;
	} | null;
	onDismiss: () => void;
	duration?: number;
}

const BasketToast = ({ toast, onDismiss, duration = 3800 }: BasketToastProps) => {
	useEffect(() => {
		if (!toast) {
			return;
		}
		const timer = setTimeout(() => onDismiss(), duration);
		return () => clearTimeout(timer);
	}, [toast, duration, onDismiss]);

	if (!toast) {
		return null;
	}

	const Icon = icons[toast.variant] as any;

	return (
		<div className={cn("basket-toast", `basket-toast--${toast.variant}`)}>
			<Icon className="basket-toast__icon" size={20} aria-hidden="true" />
			<div className="basket-toast__body">
				<p className="basket-toast__message">{toast.message}</p>
				{toast.detail && <span className="basket-toast__detail">{toast.detail}</span>}
			</div>
			<button
				className="basket-toast__close"
				onClick={onDismiss}
				aria-label="Dismiss basket notification"
			>
				<X size={16} />
			</button>
		</div>
	);
};

export default BasketToast;

