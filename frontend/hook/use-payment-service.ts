'use client';

import useAxiosPrivate from '@/hook/use-axios-private';
import { BASE_URL } from '@/lib/config';
import { ProductInCart } from '@/types';

export const usePaymentService = (cart: ProductInCart[], currency: string) => {
    const axiosPrivate = useAxiosPrivate();

    const products = cart.map((item) => {
        return {
            item: item.id,
            name: item.name,
            price: item.price,
            quantity: item.quantity,
        };
    });

    const handleCheckout = async () => {
        return await axiosPrivate.post(`${BASE_URL}/payments`, {
            products: products,
            currency: currency,
        });
    };

    return { handleCheckout };
};
