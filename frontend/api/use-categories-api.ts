import axios from 'axios';
import { CategoryType } from '@/types';
import { useQueries } from '@tanstack/react-query';
import { BASE_URL } from '@/lib/config';

const baseUrl = `${BASE_URL}/categories`;

export const useCategoriesApi = () => {
    const data = useQueries({
        queries: [
            {
                queryKey: ['get-categories'],
                queryFn: async () => {
                    const response = await axios.get(`${baseUrl}`);

                    return response.data as CategoryType[];
                },
            },
        ],
    });

    const getCategoryById = async (categoryId: number) => {
        const response = await axios.get(`${baseUrl}/${categoryId}`);

        return response.data as CategoryType;
    };

    const allCats = data[0].data;

    return {
        allCats,
        getCategoryById,
    };
};
