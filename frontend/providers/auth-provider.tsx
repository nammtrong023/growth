'use client';

import axios from 'axios';
import { TokenType, UserType } from '@/types';
import { useQuery } from '@tanstack/react-query';
import { getCookie, setCookie } from 'cookies-next';
import jwt from 'jsonwebtoken';
import dayjs from 'dayjs';
import React, {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useState,
} from 'react';

type ContextDataType = {
    currentUser: UserType | undefined;
    authToken: TokenType | undefined;
    emailSignUp: string;
    setEmailSignUp: (email: string) => void;
    handleCookies: (data: TokenType) => void;
    setAuthToken: (authToken: TokenType) => void;
    setCurrentUser: (currentUser: UserType | undefined) => void;
    logout: () => void;
};

const access_token = getCookie('access_token')?.valueOf() || '';
const refresh_token = getCookie('refresh_token')?.valueOf() || '';

const baseUrl = `${process.env.NEXT_PUBLIC_BASE_URL}/users`;

const AuthContext = createContext<ContextDataType>({
    currentUser: undefined,
    authToken: {
        access_token,
        refresh_token,
    },
    emailSignUp: '',
    setEmailSignUp: () => {},
    handleCookies: () => {},
    setAuthToken: () => {},
    setCurrentUser: () => {},
    logout: () => {},
});

export const useAuth = () => {
    return useContext(AuthContext);
};

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [currentUser, setCurrentUser] = useState<UserType>();
    const [emailSignUp, setEmailSignUp] = useState('');

    const [authToken, setAuthToken] = useState<TokenType | undefined>({
        access_token,
        refresh_token,
    });

    let isExpired = true;
    const user: any =
        authToken?.access_token && jwt.decode(authToken.access_token);

    if (user && typeof user !== 'string' && user.exp !== undefined) {
        isExpired = dayjs.unix(user.exp).diff(dayjs()) < 1;
    }

    const getCurrentUser = async () => {
        const response = await axios.get(`${baseUrl}/current-user`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authToken?.access_token}`,
            },
        });

        return response.data as UserType;
    };

    const { data, isSuccess } = useQuery({
        initialData: currentUser,
        queryKey: ['get-current-user'],
        queryFn: () => getCurrentUser(),
        enabled: !!authToken?.access_token && !isExpired,
    });

    useEffect(() => {
        if (authToken?.access_token && isSuccess) {
            try {
                setCurrentUser(data);
            } catch (error) {
                console.error('Error verifying token at auth:', error);
            }
        }
    }, [authToken?.access_token, data, isSuccess]);

    const handleCookies = useCallback(
        (data: TokenType) => {
            setCookie('access_token', data?.access_token);
            setCookie('refresh_token', data?.refresh_token);

            setAuthToken({
                access_token: data?.access_token,
                refresh_token: data?.refresh_token,
            });
        },
        [setAuthToken],
    );

    const logout = () => {
        setCurrentUser(undefined);
        setAuthToken(undefined);
        setCookie('refresh_token', '');
        setCookie('access_token', '');
    };

    const contextData = {
        authToken,
        currentUser,
        emailSignUp,
        setEmailSignUp,
        setAuthToken,
        handleCookies,
        logout,
        setCurrentUser,
    };

    return (
        <AuthContext.Provider value={contextData}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;
