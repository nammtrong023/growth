'use client';

import { useProductsApi } from '@/api/use-products-api';
import { useQuery } from '@tanstack/react-query';
import qs from 'query-string';
import { BASE_URL } from './config';

const URL = `${BASE_URL}/products/filter`;

interface Query {
    catId?: number;
    colorId?: number;
    sizeId?: number;
    price?: number;
    sort?: 'asc' | 'desc';
    page: number;
}

const useFilterProducts = (query: Query) => {
    const { getAllProductsAndFilter } = useProductsApi();

    const url = qs.stringifyUrl({
        url: URL,
        query: {
            color_id: query.colorId,
            size_id: query.sizeId,
            price: query.price,
            sort_price: query.sort,
            category_id: query.catId,
            page: query.page,
        },
    });

    const { data, isFetching, refetch } = useQuery({
        initialData: undefined,
        queryKey: ['filter-products'],
        queryFn: () => getAllProductsAndFilter(url),
    });

    return { data, isFetching, refetch };
};

export default useFilterProducts;
