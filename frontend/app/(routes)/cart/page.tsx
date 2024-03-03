'use client';
import { useEffect, useState } from 'react';
import { useCartStore } from '@/store/cart-store';
import CartItem from './components/cart-item';
import Empty from '@/components/empty';
import CheckoutForm from './components/checkout-form';

const CartPage = () => {
    const { cart } = useCartStore();
    const [isMounted, setIsMounted] = useState(false);

    useEffect(() => {
        setIsMounted(true);
    }, []);

    if (!isMounted) {
        return null;
    }

    return (
        <div className='bg-white h-full min-h-full'>
            <div className='px-4 py-16 sm:px-6 lg:px-8 w-full'>
                <div className='flex flex-col lg:flex-row justify-between gap-x-10 gap-y-10'>
                    <div className='hidden lg:block h-full w-full space-y-10'>
                        <CheckoutForm />
                    </div>
                    <div className='flex flex-col w-full'>
                        <h2 className='text-2xl font-bold text-black text-center w-full'>
                            Giỏ Hàng
                        </h2>
                        <div className='lg:col-span-7 w-full'>
                            {cart.length === 0 && (
                                <div className='w-full flex-col items-center justify-center'>
                                    <p className='text-neutral-500 text-lg font-medium text-center'>
                                        Không có sản phẩm.
                                    </p>
                                    <Empty />
                                </div>
                            )}
                            {cart.reverse().map((item, index) => (
                                <CartItem
                                    key={item.id}
                                    item={item}
                                    index={index}
                                />
                            ))}
                        </div>
                    </div>
                    <div className='lg:hidden h-full w-full space-y-10'>
                        <CheckoutForm />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CartPage;
