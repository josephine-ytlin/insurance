export interface Product {
    id: number;
    name: string;
    type: string;
    currency: string;
    isSoldout: boolean;
    stockQuantity: number;
    price: number;
    notes: string;
    description: string;
    imageUrl: string;

}
