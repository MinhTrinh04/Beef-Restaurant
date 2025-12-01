"use client";
import BlockTitle from "@/app/components/common/block-title/block-title";
import { useState, useEffect } from "react";

// Interface
import { ContactBlockProps } from "@/app/types/common.types";

const defaultFormContent = {
	nameLabel: "Name:",
	namePlaceholder: "Name",
	phoneLabel: "Phone:",
	phonePlaceholder: "Phone",
	emailLabel: "Email:",
	emailPlaceholder: "Email",
	personsLabel: "Persons:",
	personsPlaceholder: "0",
	dateLabel: "Date:",
	timeLabel: "Hours:",
	messageLabel: "Message:",
	messagePlaceholder: "Comments",
	submitButtonText: "Book a Table",
};

const ContactBlock = ({
	subtitle,
	title,
	phrase,
	formAction,
	className,
	formContent = defaultFormContent,
}: ContactBlockProps) => {
	const [isClient, setIsClient] = useState(false);

	useEffect(() => {
		setIsClient(true);
	}, []);

	if (!subtitle || !title || !phrase) {
		return (
			<div className={`contact contact--empty`}>
				<p>No data available.</p>
			</div>
		);
	}

	return (
		<section
			id="contact"
			className={`contact bg-background ${className}`}
		>
			<div className="contact__container">
				<div className="contact__content">
					{/* Section title */}
					<BlockTitle
						subtitle={subtitle}
						title={title}
						phrase={phrase}
						divider={true}
					/>
					{/* / Section title */}
					{/* Form */}
					<div className="contact__form">
						{/* Only render the form on the client side after mount */}
						{isClient ? (
							<form
								className="form"
								action={formAction}
								method="post"
							>
								<div className="form__body">
									<div className="form_row">
										<div className="form_field form_field__name">
											<label htmlFor="name">{formContent.nameLabel}</label>
											<input
												type="text"
												id="name"
												name="name"
												placeholder={formContent.namePlaceholder}
											/>
										</div>
										<div className="form_field form_field__phone">
											<label htmlFor="phone">{formContent.phoneLabel}</label>
											<input
												type="tel"
												id="phone"
												name="phone"
												placeholder={formContent.phonePlaceholder}
											/>
										</div>
									</div>
									<div className="form_row">
										<div className="form_field form_field__email">
											<label htmlFor="email">{formContent.emailLabel}</label>
											<input
												type="email"
												id="email"
												name="email"
												placeholder={formContent.emailPlaceholder}
											/>
										</div>
										<div className="form_field form_field__person">
											<label htmlFor="person">{formContent.personsLabel}</label>
											<input
												type="number"
												id="person"
												name="person"
												placeholder={formContent.personsPlaceholder}
											/>
										</div>
										<div className="form_field form_field__date">
											<label htmlFor="birthdate">{formContent.dateLabel}</label>
											<input
												type="date"
												id="birthdate"
												name="birthdate"
											/>
										</div>
										<div className="form_field form_field__hours">
											<label htmlFor="time">{formContent.timeLabel}</label>
											<input
												type="time"
												id="time"
												name="time"
											/>
										</div>
									</div>
									<div className="form_row">
										<div className="form_field form_field__message">
											<label htmlFor="message">
												{formContent.messageLabel}
											</label>
											<textarea
												id="message"
												name="message"
												placeholder={formContent.messagePlaceholder}
											></textarea>
										</div>
									</div>
								</div>
								<div className="form_footer">
									<div className="form_field">
										<button
											className="btn btn__default"
											type="submit"
											value="submit"
										>
											{formContent.submitButtonText}
										</button>
									</div>
								</div>
							</form>
						) : null}
					</div>
					{/* / Form */}
				</div>
			</div>
		</section>
	);
};

ContactBlock.displayName = "ContactBlock";
export default ContactBlock;
