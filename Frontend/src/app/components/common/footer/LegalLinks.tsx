import React from 'react';
import Link from "next/link";
// Add direct import back
import { menuLegalData } from "@/app/hooks/data-general";

// Remove props from component definition
const LegalLinks: React.FC = () => {
  // Check if the imported data and its menuLegal property exist and are valid
  if (!menuLegalData || menuLegalData.length === 0) {
    return null; // Or render a placeholder if preferred
  }

  return (
    <div className="footer__legal">
      <ul>
        {/* Map over the menuLegal array from the imported data */}
        {menuLegalData.map((link) => (
            <li key={link.id}>
                <Link href={link.link}>{link.title}</Link>
            </li>
        ))}
      </ul>
    </div>
  );
};

export default LegalLinks;
