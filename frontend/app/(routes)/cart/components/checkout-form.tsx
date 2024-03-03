'use client';

import { Button } from '@/components/ui/button';
import * as z from 'zod';
import { Input } from '@/components/ui/input';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import React, { useCallback, useEffect } from 'react';
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from '@/components/ui/form';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import { useAuth } from '@/providers/auth-provider';
import { RadioGroupForm } from '@/components/radio-form';
import { PaymentMethod } from '@/types';
import { useOrdersApi } from '@/api/use-orders-api';
import { useCartStore } from '@/store/cart-store';
import { formatMoney, phoneRegex } from '@/lib/utils';
import { useModalStore } from '@/store/modal-store';

const checkoutSchema = z.object({
    full_name: z.string().min(1, 'Vui lòng nhập không để trống'),
    email: z.string().email('Vui lòng nhập email hợp lệ'),
    phone_number: z
        .string()
        .regex(phoneRegex, 'Số điện thoại không hợp lệ!')
        .min(10, 'Tối thiểu 10 ký tự')
        .max(11, 'Tối đa 11 ký tự'),
    address: z.string().min(1, 'Vui lòng không để trống'),
    payment_method: z.enum([PaymentMethod.COD, PaymentMethod.CARD]),
});

export type CheckoutFormValues = z.infer<typeof checkoutSchema>;

const CheckoutForm = () => {
    const { currentUser } = useAuth();
    const { createOrder } = useOrdersApi();
    const { cart, totalPrice, setTotalPrice, removeAll } = useCartStore();

    const { onOpenModal } = useModalStore();

    const form = useForm<CheckoutFormValues>({
        defaultValues: {
            full_name: '',
            email: '',
            phone_number: '',
            address: '',
            payment_method: undefined,
        },
        resolver: zodResolver(checkoutSchema),
    });

    useEffect(() => {
        const handleTotalPrice = () => {
            const total = cart.reduce((total, item) => {
                return total + Number(item.price) * Number(item.quantity);
            }, 0);

            setTotalPrice(total);
        };
        handleTotalPrice();
    }, [cart, setTotalPrice]);

    const { mutate } = useMutation({
        mutationFn: (data: any) => createOrder(data),
        onSuccess: () => removeAll(),
        onError: (error) => {
            toast.error('Something went wrong.');
        },
    });

    const getOrderDetails = useCallback(() => {
        const orderDetails = cart.map((product) => {
            return {
                product_id: product.productId,
                size: product.size,
                color: product.color,
                quantity: product.quantity,
                price: product.price,
            };
        });

        return orderDetails;
    }, [cart]);

    const onSubmit = (formValue: CheckoutFormValues) => {
        if (!currentUser) {
            return onOpenModal('auth');
        }

        const data = {
            user_id: currentUser?.id,
            total_price: totalPrice,
            order_details: getOrderDetails(),
            ...formValue,
        };

        mutate(data);
    };

    return (
        <>
            <h2 className='text-2xl font-bold text-black text-center w-full'>
                Thông tin vận chuyển
            </h2>
            <Form {...form}>
                <form
                    className='w-full space-y-5'
                    onSubmit={form.handleSubmit(onSubmit)}
                >
                    <FormField
                        control={form.control}
                        name='full_name'
                        render={({ field }) => (
                            <FormItem className='flex flex-col space-y-2 w-full'>
                                <FormLabel>Tên</FormLabel>
                                <FormControl>
                                    <Input
                                        id='Tên'
                                        placeholder='Tên của bạn'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='email'
                        render={({ field }) => (
                            <FormItem className='flex flex-col space-y-2 w-full'>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input
                                        id='email'
                                        placeholder='Email của bạn'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='phone_number'
                        render={({ field }) => (
                            <FormItem className='flex flex-col space-y-2 w-full'>
                                <FormLabel>Điện thoại</FormLabel>
                                <FormControl>
                                    <Input
                                        id='phone_number'
                                        placeholder='Điện thoại của bạn'
                                        type='phone_number'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='address'
                        render={({ field }) => (
                            <FormItem className='flex flex-col space-y-2 w-full'>
                                <FormLabel>Địa chỉ</FormLabel>
                                <FormControl>
                                    <Input
                                        id='address'
                                        placeholder='Địa chỉ của bạn'
                                        className='col-span-3'
                                        disabled={true}
                                        {...field}
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='payment_method'
                        render={({ field }) => (
                            <FormItem>
                                <RadioGroupForm
                                    className='flex items-center justify-center !mt-0'
                                    field={field}
                                    disabled={cart.length === 0}
                                />
                            </FormItem>
                        )}
                    />
                    <div className='mt-6 space-y-4'>
                        <div className='flex items-center justify-between border-t border-gray-200 pt-4'>
                            <div className='text-base font-medium text-gray-900'>
                                Tổng tiền
                            </div>
                            <span>{formatMoney(totalPrice)}</span>
                        </div>
                    </div>
                    <Button
                        disabled={cart.length === 0}
                        type='submit'
                        className='w-full mt-6'
                    >
                        Thanh toán
                    </Button>
                </form>
            </Form>
        </>
    );
};

export default CheckoutForm;
