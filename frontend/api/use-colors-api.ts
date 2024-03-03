import axios from 'axios';
import { ColorType } from '@/types';
import { useQueries } from '@tanstack/react-query';
import { BASE_URL } from '@/lib/config';

const baseUrl = `${BASE_URL}/colors`;

export const useColorsApi = () => {
    const data = useQueries({
        queries: [
            {
                queryKey: ['get-colors'],
                queryFn: async () => {
                    const response = await axios.get(baseUrl);

                    return response.data as ColorType[];
                },
            },
        ],
    });

    const getColorId = async (colorId: number) => {
        const response = await axios.get(`${baseUrl}/${colorId}`);

        return response.data as ColorType;
    };

    const allColors = data[0].data;

    return {
        allColors,
        getColorId,
    };
};
