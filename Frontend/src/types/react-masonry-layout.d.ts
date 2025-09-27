declare module 'react-masonry-layout' {
    import { ReactNode } from 'react';
    
    interface MasonrySize {
        columns: number;
        gutter: number;
        mq?: string;
    }

    interface MasonryProps {
        id: string;
        sizes: MasonrySize[];
        children?: ReactNode;
    }

    const Masonry: React.FC<MasonryProps>;
    export default Masonry;
} 