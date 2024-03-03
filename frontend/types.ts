export type TokenType = {
    access_token: string;
    refresh_token: string;
};

export type UserType = {
    id: number;
    full_name: string;
    email: string;
    phone_number: string;
    address: string;
    orders: OrderType[];
};

export type CategoryType = {
    id: number;
    name: string;
    image: string;
    product: ProductType[];
};

export type ProductType = {
    id: number;
    price: number;
    name: string;
    images: Image[];
    category_id: number;
    description: string;
    is_featured: boolean;
    created_at: Date;
    colors: SizeType[];
    sizes: ColorType[];
};

export type ProductInCart = {
    id: string;
    productId: number;
    name: string;
    images: Image[];
    category_id: number;
    description: string;
    size: string | null;
    color: string | null;
    price: number;
    created_at: Date;
    quantity: number;
};

export type Image = {
    id: number;
    url: string;
};

export type SizeType = {
    id: number;
    value: string;
};

export type ColorType = {
    id: number;
    name: string;
    value: string;
};

export type OrderType = {
    id: number;
    total_price: number;
    user_id: number;
    address: string;
    phone_number: string;
    order_date: Date;
    order_details: OrderDetailType[];
};

export type ProductOrderType = {
    id: number;
    image: string;
    name: string;
    price: number;
};

export type OrderDetailType = {
    id: number;
    product: ProductOrderType;
    size: string;
    price: number;
    color: string;
    quantity: number;
};

export type ProductPagination = {
    products: ProductType[];
    currentPage: number;
    totalPages: number;
};

export enum PaymentMethod {
    COD = 'COD',
    CARD = 'CARD',
}
