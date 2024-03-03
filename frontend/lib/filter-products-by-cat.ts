'use client';

import { useProductsApi } from '@/api/use-products-api';
import { useQuery } from '@tanstack/react-query';
import qs from 'query-string';
import { BASE_URL } from './config';

interface Query {
    categoryId: number;
    colorId?: number;
    sizeId?: number;
    price?: number;
    sortPrice: 'asc' | 'desc';
    page?: number;
}

const useFilterProductsByCat = (query: Query) => {
    const { getAllProductsAndFilter } = useProductsApi();
    const URL = `${BASE_URL}/products/filter?category_id=${query.categoryId}`;

    const url = qs.stringifyUrl({
        url: URL,
        query: {
            color_id: query.colorId,
            size_id: query.sizeId,
            price: query.price,
            sort_price: query.sortPrice,
            page: query.page,
        },
    });

    const { data, isFetching, refetch } = useQuery({
        initialData: undefined,
        queryKey: ['products-by-category'],
        queryFn: () => getAllProductsAndFilter(url),
    });

    return { data, isFetching, refetch };
};

export default useFilterProductsByCat;
