import axios from 'axios';
import { ProductPagination, ProductType } from '@/types';
import { BASE_URL } from '@/lib/config';

const baseUrl = `${BASE_URL}/products`;

export const useProductsApi = () => {
    const getProducts = async () => {
        const response = await axios.get(baseUrl);

        return response.data as ProductPagination;
    };

    const getFeaturedProducts = async () => {
        const response = await axios.get(`${baseUrl}/featured-products`);

        return response.data as ProductType[];
    };

    const searchProducts = async (name: string | null) => {
        const response = await axios.get(`${baseUrl}?search=${name}`);

        return response.data as ProductPagination;
    };

    const getAllProductsAndFilter = async (url: string) => {
        const response = await axios.get(url);

        return response.data as ProductPagination;
    };

    const getProductsByCat = async (url: string) => {
        const response = await axios.get(url);

        return response.data as ProductType[];
    };

    const getProductById = async (productId: number) => {
        const response = await axios.get(`${baseUrl}/${productId}`);

        return response.data as ProductType;
    };

    const getSimilarProduct = async (productId: number) => {
        const response = await axios.get(`${baseUrl}/similar/${productId}`);

        return response.data as ProductType[];
    };

    const getNewProducts = async () => {
        const response = await axios.get(
            `${baseUrl}/filter?sort_created_at=desc&limit=8&page=1`,
        );

        return response.data.products as ProductType[];
    };

    return {
        getProducts,
        getNewProducts,
        getFeaturedProducts,
        searchProducts,
        getSimilarProduct,
        getAllProductsAndFilter,
        getProductsByCat,
        getProductById,
    };
};
