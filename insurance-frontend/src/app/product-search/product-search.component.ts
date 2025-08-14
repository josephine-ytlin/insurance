import { Component, OnInit } from '@angular/core';
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
  type: string | null = null;
  currency: string | null = null;
  isBonus: boolean | null = null;
  ageRange: number[] = [0, 100];

  typeOptions: any[] = [];
  currencyOptions: any[] = [];
  products: Product[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private modal: NzModalService
  ) {}

  ngOnInit() {
    this.loadAllProducts();
  }

  /** 初始化載入所有產品 */
  private loadAllProducts() {
    this.http.get<any>('/api/products').subscribe({
      next: res => {
        if (res.success) {
          const products = res.data || [];
          this.products = products;
  
          this.typeOptions = Array.from(new Set(products.map((p: Product) => p.type)))
            .map(t => ({ label: t, value: t }));
          this.currencyOptions = Array.from(new Set(products.map((p: Product) => p.currency)))
            .map(c => ({ label: c, value: c }));
  
          if (products.length > 0) {
            const min = Math.min(...products.map((p: Product) => p.minAge));
            const max = Math.max(...products.map((p: Product) => p.maxAge));
            this.ageRange = [min, max];
          }
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
    const isAgeRangeDefault = this.ageRange[0] === 0 && this.ageRange[1] === 75;

    if (!this.type && !this.currency && this.isBonus === null && isAgeRangeDefault) {
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
      isBonus: this.isBonus,
      minAge: this.ageRange[0],
      maxAge: this.ageRange[1]
    }).subscribe(res => {
      if (res.success) {
        this.products = res.data || [];
        
        //清空選項
        this.type = null;
        this.currency = null;
        this.isBonus = null;
        this.ageRange = [0, 75]; // 或你想要的預設範圍
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
    this.isBonus = false;
  }

  /** 幣別下拉選擇事件 */
  onCurrencyChange(value: string) {
    console.log('幣別改變:', value);
    this.isBonus = false;
  }

  /** 查看商品詳細 */
  viewProductDetail(id: number) {
    this.http.get<any>(`/api/products/${id}`).subscribe({
      next: res => {
        if (res.success) {
          const p = res.data;
          this.modal.info({
            nzTitle: `商品詳細資料 - ${p.name}`,
            nzContent: `
              <p><b>類型：</b> ${p.type}</p>
              <p><b>幣別：</b> ${p.currency}</p>
              <p><b>分紅：</b> ${p.isBonus ? '是' : '否'}</p>
              <p><b>年齡範圍：</b> ${p.minAge} - ${p.maxAge}</p>
              <p><b>繳費方式：</b> ${p.paymentTerm || '無資料'}</p>
              <p><b>特色：</b> ${p.description || '無資料'}</p>
            `
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

  /** 登出 */
  logout() {
    this.http.post('/api/auth/logout', {}).subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login'])
    });
  }
}
