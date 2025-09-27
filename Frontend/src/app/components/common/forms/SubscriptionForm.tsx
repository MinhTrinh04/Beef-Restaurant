'use client';

import React, { useState } from "react";
import { useRouter } from 'next/navigation';
import { SubscriptionFormProps } from "@/app/types/common.types";
import { subscriptionFormData } from "@/app/hooks/data-forms";

type FormStatus = "idle" | "loading" | "success" | "error";

const emailRegex =
	/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

/**
 * A subscription form component that captures an email address.
 * It handles its own state for submission, including loading, success, and error states,
 * and displays appropriate messages to the user.
 *
 * @param {SubscriptionFormProps} props The props for configuring the form's text and messages.
 * @returns {React.ReactElement} The rendered subscription form.
 */
const SubscriptionForm: React.FC<SubscriptionFormProps> = ({
	placeholder = subscriptionFormData.placeholder,
	buttonAriaLabel = subscriptionFormData.buttonAriaLabel,
	iconClass = subscriptionFormData.iconClass,
	successMessage = subscriptionFormData.successMessage,
	errorMessage = subscriptionFormData.errorMessage,
}) => {
	const [email, setEmail] = useState("");
	const [status, setStatus] = useState<FormStatus>("idle");
	const [message, setMessage] = useState("");
	const router = useRouter();

	const validateEmail = (email: string): boolean => {
		return emailRegex.test(email);
	};

	const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
		event.preventDefault();

		if (!validateEmail(email)) {
			setStatus('error');
			setMessage('Please enter a valid email address.');
			return;
		}

		setStatus("loading");
		setMessage("");

		// Simulate API call
		try {
			await new Promise((resolve) => {
				setTimeout(() => {
					resolve('Success');
				}, 2000);
			});

			router.push('/confirmation');
		} catch (error) {
			setStatus("error");
			setMessage(errorMessage);
		}
	};

	return (
		<form className="form coming-soon__form" onSubmit={handleSubmit}>
			<div className="form__body form_field form_field__email">
				<input
					type="email"
					className="coming-soon-input"
					placeholder={placeholder}
					required
					value={email}
					onChange={(e) => setEmail(e.target.value)}
					disabled={status === "loading"}
				/>
			</div>
			<div className="form__footer form_field">
				<button
					className="btn btn__default"
					type="submit"
					aria-label={buttonAriaLabel}
					disabled={status === "loading"}
				>
					{status === "loading" ? (
						<span className="loader"></span>
					) : (
						<i className={iconClass}></i>
					)}
				</button>
			</div>
			{status === "success" && (
				<p className="form-success-message">{message}</p>
			)}
			{status === "error" && <p className="form-error-message">{message}</p>}
		</form>
	);
};

export default SubscriptionForm; 