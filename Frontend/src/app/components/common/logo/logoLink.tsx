import React from 'react';
import Link from 'next/link';
import Image from 'next/image';

interface LogoLinkProps {
  src: string;
  alt: string;
  href: string;
  width: number;
  height: number;
}

const LogoLink: React.FC<LogoLinkProps> = ({ href, src, alt, width, height }) => {
  return (
    <Link className="link__logo_brand" href={href}>
      <Image src={src} priority alt={alt} width={width} height={height} />
    </Link>
  );
};

export default LogoLink;