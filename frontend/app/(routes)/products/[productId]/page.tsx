'use client';

import { useProductsApi } from '@/api/use-products-api';
import ProductDetails from '@/components/products/product-details';
import ProductList from '@/components/products/product-list';
import { Container } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import { FC } from 'react';

interface ProductPropsPage {
    params: {
        productId: string;
    };
}

const ProductPage: FC<ProductPropsPage> = ({ params }) => {
    const productId = params.productId as unknown as number;
    const { getProductById, getSimilarProduct } = useProductsApi();

    const { data } = useQuery({
        queryKey: ['productId', params.productId],
        queryFn: () => getProductById(productId),
    });

    const { data: similarProducts } = useQuery({
        queryKey: ['similar-products', params.productId],
        queryFn: () => getSimilarProduct(productId),
    });

    if (!data || !similarProducts) return null;

    return (
        <Container maxWidth='xl'>
            <ProductDetails item={data} />
            {similarProducts.length > 0 && (
                <div className='mt-10'>
                    <ProductList
                        title='Có thể bạn cũng thích'
                        items={similarProducts}
                    />
                </div>
            )}
        </Container>
    );
};

export default ProductPage;
