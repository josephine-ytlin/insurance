export interface Product {
    id: number;
    name: string;
    type: string;
    currency: string;
    isBonus: boolean;
    minAge: number;
    maxAge: number;
    paymentTerm?: string;
    description?: string;
}
