'use client';

import React, { useEffect } from 'react';
import useFilterProductsByCat from '@/lib/filter-products-by-cat';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useColorsApi } from '@/api/use-colors-api';
import { useSizesApi } from '@/api/use-sizes-api';
import Filter from '@/components/filter';
import { useCategoriesApi } from '@/api/use-categories-api';
import NotFoundIcon from '@/components/not-found-icon';
import SortPrice from '@/components/sort-price';
import PriceSlider from '@/components/price-slider';
import MobileFilters from '@/components/mobile-filter';
import Pagination from '@/components/ui/pagination';

interface CategoryProps {
    params: { categoryId: number };
    searchParams: {
        color_id: number;
        size_id: number;
        price: number;
        sort_price: 'asc' | 'desc';
        page: number;
    };
}

const CategoryPage: React.FC<CategoryProps> = ({ params, searchParams }) => {
    const { getCategoryById } = useCategoriesApi();

    const { data: cat } = useQuery({
        queryKey: ['get-category-by-id'],
        queryFn: () => getCategoryById(params.categoryId),
    });

    const { allSizes } = useSizesApi();
    const { allColors } = useColorsApi();
    const queryClient = useQueryClient();

    const { data, isFetching, refetch } = useFilterProductsByCat({
        colorId: searchParams.color_id,
        sizeId: searchParams.size_id,
        price: searchParams.price,
        sortPrice: searchParams.sort_price,
        categoryId: params.categoryId,
        page: searchParams.page,
    });

    useEffect(() => {
        refetch();
    }, [queryClient, refetch, searchParams]);

    if (!data) return null;

    if (!allSizes || !allColors || !cat) return null;

    return (
        <div className='bg-white max-w-7xl mx-auto'>
            <div className='flex items-center'>
                <h2 className='py-10 w-full text-2xl text-center font-bold'>
                    {cat.name}
                </h2>
                <div className='ml-auto shrink-0 mr-2 md:inline-block hidden'>
                    <SortPrice />
                </div>
            </div>

            <div className='px-4 sm:px-6 lg:px-8 pb-24'>
                <div className='lg:grid lg:grid-cols-5 lg:gap-x-8'>
                    <div className='flex md:hidden items-center justify-between'>
                        <MobileFilters sizes={allSizes} colors={allColors} />
                        <SortPrice />
                    </div>
                    <div className='hidden lg:block'>
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

export default CategoryPage;
