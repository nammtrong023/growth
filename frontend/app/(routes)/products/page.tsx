'use client';

import React, { useEffect } from 'react';
import useFilterProducts from '@/lib/filter-products';
import { useCategoriesApi } from '@/api/use-categories-api';
import { useSizesApi } from '@/api/use-sizes-api';
import { useColorsApi } from '@/api/use-colors-api';
import { useQueryClient } from '@tanstack/react-query';
import NotFoundIcon from '@/components/not-found-icon';
import Filter from '@/components/filter';
import PriceSlider from '@/components/price-slider';
import Pagination from '@/components/ui/pagination';
import SortPrice from '@/components/sort-price';
import MobileFilters from '@/components/mobile-filter';

interface AllProductsPage {
    searchParams: {
        category_id: number;
        color_id: number;
        size_id: number;
        price: number;
        sort_price: 'asc' | 'desc';
        page: number;
    };
}

const AllProductsPage = ({ searchParams }: AllProductsPage) => {
    const { allCats } = useCategoriesApi();
    const { allSizes } = useSizesApi();
    const { allColors } = useColorsApi();
    const queryClient = useQueryClient();

    const { data, isFetching, refetch } = useFilterProducts({
        colorId: searchParams.color_id,
        sizeId: searchParams.size_id,
        catId: searchParams.category_id,
        price: searchParams.price,
        sort: searchParams.sort_price,
        page: searchParams.page,
    });

    useEffect(() => {
        refetch();
    }, [queryClient, refetch, searchParams]);

    if (!data) return null;

    if (!allCats || !allSizes || !allColors) return null;

    return (
        <div className='bg-white max-w-7xl mx-auto'>
            <div className='flex items-center'>
                <h2 className='py-10 w-full text-2xl text-center font-bold'>
                    Tất cả sản phẩm ({data.products.length || '0'})
                </h2>
                <div className='ml-auto shrink-0 mr-2 md:inline-block hidden'>
                    <SortPrice />
                </div>
            </div>

            <div className='px-4 sm:px-6 lg:px-8 pb-24'>
                <div className='lg:grid lg:grid-cols-5 lg:gap-x-8'>
                    <div className='flex md:hidden items-center justify-between'>
                        <MobileFilters
                            sizes={allSizes}
                            colors={allColors}
                            cats={allCats}
                        />
                        <SortPrice />
                    </div>
                    <div className='hidden lg:block'>
                        <Filter
                            valueKey='category_id'
                            name='Danh mục'
                            data={allCats}
                        />
                        <Filter
                            valueKey='size_id'
                            name='Kích thước'
                            sizes={allSizes}
                        />
                        <Filter
                            valueKey='color_id'
                            name='Màu sắc'
                            data={allColors}
                        />
                        <PriceSlider />
                    </div>
                    <div className='mt-6 lg:col-span-4 lg:mt-0'>
                        {!isFetching && data.products.length === 0 && (
                            <div className='flex flex-col items-center justify-center mt-10'>
                                <NotFoundIcon />
                                <p className='text-black mt-2 text-base font-medium'>
                                    Không tìm thấy sản phẩm
                                </p>
                            </div>
                        )}

                        {!isFetching && data.products.length > 0 && (
                            <Pagination
                                products={data.products}
                                totalPages={data.totalPages}
                                currentPage={data.currentPage}
                                className='grid grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-4'
                            />
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AllProductsPage;
