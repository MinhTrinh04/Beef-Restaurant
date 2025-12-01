import StatusPage from "@/app/components/common/status-page/StatusPage";
import { notFoundPageData } from "@/app/hooks/data-status-page";

/**
 * Not Found page (404).
 * This page is rendered when a user tries to access a route that does not exist.
 * It uses the generic StatusPage component to display the 404 error information.
 *
 * @returns {React.ReactElement} The rendered 404 page.
 */
const NotFoundPage = () => {
	return <StatusPage {...notFoundPageData} />;
};

export default NotFoundPage; 