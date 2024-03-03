'use client';

import { cn } from '@/lib/utils';
import { FormControl, FormItem, FormLabel } from '@/components/ui/form';
import { RadioGroup, RadioGroupItem } from '@/components/ui/radio-group';
import { PaymentMethod } from '@/types';
import { CreditCard, Truck } from 'lucide-react';

interface RadioGroupFormProps {
    field: {
        value: PaymentMethod | null;
        onChange: (value: PaymentMethod) => void;
    };
    disabled: boolean;
    className?: string;
}

export function RadioGroupForm({
    field,
    disabled,
    className,
}: RadioGroupFormProps) {
    return (
        <FormControl className={cn('mr-auto w-full !mt-5', className)}>
            <RadioGroup
                onValueChange={field.onChange}
                value={field.value as PaymentMethod}
                className='flex flex-col space-y-5'
                disabled={disabled}
            >
                <FormItem className='flex items-center space-x-5 space-y-0 h-10 lg:h-[52px] rounded-md lg:rounded-[10px] w-full border p-3'>
                    <FormControl>
                        <RadioGroupItem value={PaymentMethod.COD} />
                    </FormControl>
                    <Truck className='w-6 h-6 text-gray-600' />
                    <FormLabel className='text-sm cursor-pointer'>
                        <p>COD</p>
                        <p>Thanh toán khi nhận hàng</p>
                    </FormLabel>
                </FormItem>
                <FormItem className='flex items-center space-x-5 space-y-0 h-10 lg:h-[52px] rounded-md lg:rounded-[10px] w-full border p-3'>
                    <FormControl>
                        <RadioGroupItem value={PaymentMethod.CARD} />
                    </FormControl>
                    <CreditCard className='w-6 h-6 text-gray-600' />
                    <FormLabel className='text-sm cursor-pointer'>
                        Thanh toán bằng ngân hàng
                    </FormLabel>
                </FormItem>
            </RadioGroup>
        </FormControl>
    );
}
