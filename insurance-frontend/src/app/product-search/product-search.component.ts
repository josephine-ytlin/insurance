import { Component, OnInit, ViewChild, TemplateRef  } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NzModalService } from 'ng-zorro-antd/modal';
import { Product } from '../models/product';


@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.css']
})
export class ProductSearchComponent implements OnInit {
  // 篩選條件
  type: string | null = null;
  currency: string | null = null;
  notSoldout: boolean | null = null;
  priceRange: number[] = [0, 1000]; // 預設價格範圍

  // 篩選選項
  typeOptions: any[] = [];
  currencyOptions: any[] = [];
  products: Product[] = [];
  selectedProduct: any | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
    private modal: NzModalService
  ) {}

  ngOnInit() {
    this.loadAllProducts();
  }

  @ViewChild('productDetail', { static: true }) productDetailTpl!: TemplateRef<any>;

  /** 初始化載入所有產品 */
  private loadAllProducts() {
    this.http.get<any>('/api/products').subscribe({
      next: res => {
        if (res.success) {
          const products = res.data || [];
          this.products = products;
          console.debug('取回的產品資料:', res.data);
  
          this.typeOptions = Array.from(new Set(products.map((p: Product) => p.type)))
            .map(t => ({ label: t, value: t }));
          this.currencyOptions = Array.from(new Set(products.map((p: Product) => p.currency)))
            .map(c => ({ label: c, value: c }));
  
          // if (products.length > 0) {
          //   const min = Math.min(...products.map((p: Product) => p.minPrice));
          //   const max = Math.max(...products.map((p: Product) => p.maxPrice));
          //   this.priceRange = [min, max];
          // }
        } else {
          this.modal.error({
            nzTitle: '載入失敗',
            nzContent: res.message || '無法取得產品資料'
          });
        }
      },
      error: err => {
        console.error('取得所有產品失敗', err);
        this.modal.error({
          nzTitle: '載入失敗',
          nzContent: '伺服器錯誤，請稍後再試'
        });
      }
    });
  }

  /** 查詢按鈕事件 */
  onSearch() {
    // 檢查價格範圍是否為預設值
    const isPriceRangeDefault = this.priceRange[0] === 0 && this.priceRange[1] === 1000;

    if (!this.type && !this.currency && this.notSoldout === null && isPriceRangeDefault) {
      this.modal.warning({
        nzTitle: '查詢條件不足',
        nzContent: '請至少設定一個查詢條件再搜尋'
      });
      return;
    }

    this.searchProducts();
  }

  /** 呼叫查詢 API */
  private searchProducts() {
    this.http.post<any>('/api/products/search', {
      type: this.type,
      currency: this.currency,
      isSoldout: !this.notSoldout, // 有庫存為false，無庫存為true
      minPrice: this.priceRange[0],
      maxPrice: this.priceRange[1]
    }).subscribe(res => {
      if (res.success) {
        this.products = res.data || [];
      } else {
        this.modal.error({
          nzTitle: '查詢失敗',
          nzContent: res.message || '無法取得查詢結果'
        });
      }
    });
  }

  /** 商品類型下拉選擇事件 */
  onTypeChange(value: string) {
    console.log('商品類型改變:', value);
    this.notSoldout = false;
  }

  /** 幣別下拉選擇事件 */
  onCurrencyChange(value: string) {
    console.log('幣別改變:', value);
    this.notSoldout = false;
  }

  /** 顯示商品詳細資料 */
  viewProductDetail(id: number) {
    this.http.get<any>(`/api/products/${id}`).subscribe({
      next: res => {
        if (res.success) {
          this.selectedProduct = res.data; // 先設定資料
          this.modal.create({
            nzTitle: `商品詳細資料 - ${this.selectedProduct.name}`,
            nzContent: this.productDetailTpl,
            nzFooter: [
              { label: '關閉', type: 'default', onClick: () => this.modal.closeAll() },
              { label: '加入購物車', onClick: () => this.addToCart(this.selectedProduct.id), class: 'btn-add-cart' },
              { label: '購買', type: 'primary', onClick: () => this.buyProduct(this.selectedProduct.id) }
            ]
          });
        } else {
          this.modal.error({
            nzTitle: '取得詳細資料失敗',
            nzContent: res.message || '請稍後再試'
          });
        }
      },
      error: err => {
        console.error('取得商品詳細資料失敗', err);
        this.modal.error({
          nzTitle: '錯誤',
          nzContent: '伺服器錯誤，請稍後再試'
        });
      }
    });
  }

  /** 功能實作 */
  buyProduct(productId: number) {
    console.log('購買商品 ID:', productId);
    // TODO: 呼叫購買 API 或導頁
  }

  addToCart(productId: number) {
    console.log('加入購物車:', productId);
    // TODO: 呼叫購物車 API 或本地儲存
  }

  addToFavorite(productId: number) {
    console.log('加入收藏:', productId);
    // TODO: 呼叫 API 或本地儲存
  }

  shareProduct(productId: number) {
    console.log('分享商品:', productId);
    // TODO: 開啟分享視窗或複製連結
  }
  
  /** 登出 */
  logout() {
    this.http.post('/api/auth/logout', {}).subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login'])
    });
  }
}
