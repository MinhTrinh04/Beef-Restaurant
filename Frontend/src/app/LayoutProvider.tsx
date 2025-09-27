'use client';

import { usePathname } from 'next/navigation';
import { InfoGeneralData } from "./hooks/data-general";
import Header from "./components/common/header/Header";
import Topbar from "./components/common/topbar/Topbar";
import Footer from "./components/common/footer/Footer";

export default function LayoutProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  const hideOnRoutes = ['/coming-soon'];
  const shouldHideHeaderFooter = hideOnRoutes.includes(pathname);

  return (
    <>
      {!shouldHideHeaderFooter && (
        <>
          <Topbar 
            address={InfoGeneralData.address}
            city={InfoGeneralData.city}
            zip={InfoGeneralData.zip}
            country={InfoGeneralData.country}
            mapUrl={InfoGeneralData.urlMap}
            phone={InfoGeneralData.phone}
            email={InfoGeneralData.emailReservations}
          />
          <Header />
        </>
      )}
      <main>{children}</main>
      {!shouldHideHeaderFooter && <Footer />}
    </>
  );
} 