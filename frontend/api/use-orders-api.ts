import { OrderType } from '@/types';
import useAxiosPrivate from '@/hook/use-axios-private';
import { BASE_URL } from '@/lib/config';

const baseUrl = `${BASE_URL}/orders`;

export const useOrdersApi = () => {
    const axiosPrivate = useAxiosPrivate();

    const getOrdersByUserId = async (userId: number | undefined) => {
        const response = await axiosPrivate.get(`${baseUrl}/users/${userId}`);

        return response.data as OrderType[];
    };

    const createOrder = async (data: any) => {
        await axiosPrivate.post(baseUrl, data);
    };

    return {
        getOrdersByUserId,
        createOrder,
    };
};
