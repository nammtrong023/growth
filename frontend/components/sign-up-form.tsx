'use client';

import { Button } from '@/components/ui/button';
import * as z from 'zod';
import { Input } from '@/components/ui/input';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

const signupSchema = z.object({
    email: z.string().email('Vui lòng nhập email hợp lệ'),
    password: z.string().min(5, 'Vui lòng nhập password hợp lệ'),
    full_name: z.string().min(1, 'Vui lòng nhập không để trống'),
    phone_number: z
        .string()
        .regex(phoneRegex, 'Số điện thoại không hợp lệ!')
        .min(10, 'Tối thiểu 10 ký tự')
        .max(11, 'Tối đa 11 ký tự'),
    address: z.string().min(1, 'Vui lòng không để trống'),
});

export type SignupFormValues = z.infer<typeof signupSchema>;

import React, { useState } from 'react';
import { Form, FormControl, FormField, FormItem, FormLabel } from './ui/form';
import useAuthApi from '@/api/use-auth-api';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import useValidateOtp from '@/hook/use-validate-otp';
import { useAuth } from '@/providers/auth-provider';
import { Loader2 } from 'lucide-react';
import { phoneRegex } from '@/lib/utils';

const SignUpForm = () => {
    const { register } = useAuthApi();
    const { setEmailSignUp } = useAuth();

    const [hasOTP, setHasOTP] = useState(false);
    const [isRegisted, setIsRegisted] = useState(false);

    const {
        isTimeout,
        remainingTime,
        handleResend,
        formatTime,
        handleSubmit,
        setOtp,
        setRemainingTime,
        setIsTimeout,
    } = useValidateOtp();

    const form = useForm<SignupFormValues>({
        defaultValues: {
            email: '',
            full_name: '',
            password: '',
            phone_number: '',
            address: '',
        },
        resolver: zodResolver(signupSchema),
    });

    const { mutate, isPending } = useMutation({
        mutationKey: ['register'],
        mutationFn: (data: SignupFormValues) => register(data),
        onSuccess: () => {
            toast.success('Success');
            setIsRegisted(true);
        },
        onError: (error) => {
            toast.error('Email đã tồn tại');
        },
    });

    const onSubmit = (data: SignupFormValues) => {
        mutate(data);
        setEmailSignUp(data.email);
    };

    const handleSubmitOTP = () => {
        if (!hasOTP && !isRegisted) return;

        if (hasOTP) {
            handleResend();
            setHasOTP(false);
            return;
        }

        try {
            if (isRegisted && !hasOTP) {
                setIsTimeout(true);
                setRemainingTime(120);
                setHasOTP(true);
                handleSubmit();
            }
        } catch (error) {
            toast.error('Mã xác nhận không hợp lệ');
        }
    };

    return (
        <>
            <Form {...form}>
                <form
                    className='grid gap-4 py-4 w-full'
                    onSubmit={form.handleSubmit(onSubmit)}
                >
                    <FormField
                        control={form.control}
                        name='full_name'
                        render={({ field }) => (
                            <FormItem className='grid grid-cols-4 items-center gap-4'>
                                <FormLabel>Tên</FormLabel>
                                <FormControl>
                                    <Input
                                        id='name'
                                        placeholder='Tên của bạn'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='email'
                        render={({ field }) => (
                            <FormItem className='grid grid-cols-4 items-center gap-4'>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input
                                        id='email'
                                        placeholder='Email của bạn'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='password'
                        render={({ field }) => (
                            <FormItem className='grid grid-cols-4 items-center gap-4'>
                                <FormLabel>Mật khẩu</FormLabel>
                                <FormControl>
                                    <Input
                                        id='password'
                                        placeholder='Mật khẩu của bạn'
                                        type='password'
                                        className='col-span-3'
                                        {...field}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='phone_number'
                        render={({ field }) => (
                            <FormItem className='grid grid-cols-4 items-center gap-4'>
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
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name='address'
                        render={({ field }) => (
                            <FormItem className='grid grid-cols-4 items-center gap-4'>
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
                            </FormItem>
                        )}
                    />
                    <div className='flex items-center justify-between gap-x-2 w-full relative p-1'>
                        <Input
                            placeholder='Nhập mã xác nhận'
                            disabled={!isRegisted}
                            maxLength={6}
                            onChange={(e) => {
                                const value = e.target.value;
                                const sanitizedValue = value.replace(
                                    /[^0-9]/g,
                                    '',
                                );

                                if (sanitizedValue.length <= 6) {
                                    setOtp(sanitizedValue);
                                }
                            }}
                            className='focus-visible:ring-0 focus-visible:ring-offset-0 rounded-lg'
                        />

                        {!isTimeout ? (
                            <Button
                                className='absolute right-[6px] top-[6px] bg-slate-100 hover:bg-slate-100/70 h-9 rounded-lg'
                                variant='ghost'
                                type='button'
                                onClick={handleSubmitOTP}
                                disabled={!isRegisted}
                            >
                                Gửi
                            </Button>
                        ) : (
                            <Button
                                className='absolute right-[6px] top-[6px] bg-slate-100 hover:bg-slate-100/70 h-9 rounded-lg w-[140px]'
                                variant='ghost'
                                type='button'
                                disabled={true}
                            >
                                Gửi lại sau {formatTime(remainingTime)}
                            </Button>
                        )}
                    </div>
                    <Button type='submit' disabled={isPending}>
                        {isPending && <Loader2 className='animate-spin mr-2' />}
                        Đăng ký
                    </Button>
                </form>
            </Form>
        </>
    );
};

export default SignUpForm;
