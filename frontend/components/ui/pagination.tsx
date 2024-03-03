'use client';

import { ProductType } from '@/types';
import { ChevronRight, ChevronLeft } from 'lucide-react';
import React, { useEffect } from 'react';
import { cn } from '@/lib/utils';
import ProductCard from '../products/product-card';
import qs from 'query-string';
import { useRouter, useSearchParams } from 'next/navigation';
import { Button } from './button';

const Pagination = ({
    products,
    className,
    currentPage,
    totalPages,
}: {
    products: ProductType[];
    currentPage: number;
    totalPages: number;
    className?: string;
}) => {
    const router = useRouter();
    const searchParams = useSearchParams();
    const productsLength = products.length;

    const handlePageChange = (page: number) => {
        if (totalPages <= 1) return;
        const parsedSearchParams = qs.parse(searchParams.toString());
        const query = { ...parsedSearchParams };
        const parsedPage = page.toString();

        if (page > 1) {
            query.page = parsedPage;
        } else {
            delete query.page;
        }

        const url = qs.stringifyUrl(
            {
                url: window.location.href.split('?')[0],
                query,
            },
            { skipNull: true },
        );

        router.push(url);
    };

    useEffect(() => {
        window.scrollTo(0, 0);
    }, [searchParams]);

    return (
        <>
            <div className={cn('grid lg:grid-cols-5', className)}>
                {products.map((product) => (
                    <ProductCard key={product.id} item={product} />
                ))}
            </div>
            {productsLength > 0 && totalPages > 0 && (
                <div className='flex items-center justify-center gap-x-2'>
                    <Button
                        className='cursor-pointer p-1 w-8 h-8'
                        variant='outline'
                        onClick={() => handlePageChange(currentPage - 1)}
                    >
                        <ChevronLeft />
                    </Button>
                    {Array.from({ length: totalPages }).map((_, pageIndex) => (
                        <Button
                            key={pageIndex}
                            className={cn(
                                'cursor-pointer p-1 w-8 h-8 hover:text-blue-500',
                                currentPage == pageIndex + 1 && 'text-blue-500',
                            )}
                            variant='outline'
                            onClick={() => handlePageChange(pageIndex + 1)}
                        >
                            {pageIndex + 1}
                        </Button>
                    ))}
                    <Button
                        className='cursor-pointer p-1 w-8 h-8'
                        variant='outline'
                        onClick={() => handlePageChange(currentPage + 1)}
                    >
                        <ChevronRight />
                    </Button>
                </div>
            )}
        </>
    );
};

export default Pagination;
