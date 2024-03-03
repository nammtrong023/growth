'use client';

import CatBanner from '@/components/cat-banner';
import Heading from '@/components/heading';
import ProductList from '@/components/products/product-list';
import Slider from '@/components/slider';
import { useQuery } from '@tanstack/react-query';
import ProductListSkeleton from '@/components/skeleton/product-list-skeleton';
import Image from 'next/image';
import { useProductsApi } from '@/api/use-products-api';

export default function HomePage() {
    const { getFeaturedProducts, getNewProducts } = useProductsApi();

    const { data: products } = useQuery({
        queryKey: ['featured-products'],
        queryFn: getFeaturedProducts,
    });

    const { data: newProducts } = useQuery({
        queryKey: ['new-products'],
        queryFn: getNewProducts,
    });

    if (!products) return null;

    return (
        <div className='mb-[100px]'>
            <div className='relative'>
                <Image
                    src='/banner.png'
                    alt='banner'
                    width={1920}
                    height={797}
                    priority
                />
            </div>
            <div className='max-w-[1440px] mx-auto lg:px-5'>
                <div className='w-full'>
                    {!products || !newProducts ? (
                        <>
                            <ProductListSkeleton />
                            <ProductListSkeleton />
                        </>
                    ) : (
                        <>
                            <ProductList
                                title='Sản phẩm mới'
                                items={newProducts}
                            />
                            <div className='hidden md:block'>
                                <Heading title='Sản phẩm nổi bật' />
                                <Slider products={products} />
                            </div>
                        </>
                    )}
                </div>
                <div className='w-full mt-20'>
                    <CatBanner />
                </div>
            </div>
        </div>
    );
}
