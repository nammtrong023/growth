import { VerifyEmailForm } from '@/app/(auth)/password-recovery/page';
import { UpdateProfileFormType } from '@/app/(routes)/profiles/[profileId]/page';
import { SignInFormValues } from '@/components/sign-in-form';
import { SignupFormValues } from '@/components/sign-up-form';
import useAxiosPrivate from '@/hook/use-axios-private';
import { BASE_URL } from '@/lib/config';
import { TokenType, UserType } from '@/types';
import axios from 'axios';

const useAuthApi = () => {
    const axiosPrivate = useAxiosPrivate();

    const register = async (data: SignupFormValues) => {
        await axios.post(`${BASE_URL}/auth/register`, data);
    };

    const login = async (data: SignInFormValues) => {
        const response = await axios.post(`${BASE_URL}/auth/login`, data);

        return response.data as TokenType;
    };

    const resetPassword = async (value: VerifyEmailForm) => {
        const response = await axios.post(
            `${BASE_URL}/auth/password-recovery`,
            value,
        );
        return response.data as TokenType;
    };

    const updateProfile = async (
        id: number | undefined,
        data: UpdateProfileFormType,
    ) => {
        const response = await axiosPrivate.put(
            `${BASE_URL}/users/${id}`,
            data,
        );

        return response.data as UserType;
    };

    return { register, login, resetPassword, updateProfile };
};

export default useAuthApi;
