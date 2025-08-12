import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';  // 導入 Router
import { NzModalService } from 'ng-zorro-antd/modal';

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
  products: any[] = [];

  constructor(
    private http: HttpClient, 
    private router: Router,
    private modal: NzModalService
  ) {}

  ngOnInit() {
    this.http.post<any[]>('/api/products', {}).subscribe(res => {
      console.log('API 回應:', res);

      this.products = res;
      this.typeOptions = Array.from(new Set(res.map(p => p.type))).map(t => ({ label: t, value: t }));
      this.currencyOptions = Array.from(new Set(res.map(p => p.currency))).map(c => ({ label: c, value: c }));
      // 預設年齡範圍
      const min = Math.min(...res.map(p => p.minAge));
      const max = Math.max(...res.map(p => p.maxAge));
      this.ageRange = [min, max];
    });
  }

  onSearch() {
    const isAgeRangeDefault = this.ageRange[0] === 0 && this.ageRange[1] === 75;
    if (!this.type && !this.currency && this.isBonus === null && isAgeRangeDefault) {
      this.modal.confirm({
        nzTitle: '沒有設定任何查詢條件',
        nzContent: '你沒有選擇任何查詢條件，將會顯示所有商品，確定要查詢嗎？',
        nzOkText: '確定',
        nzCancelText: '取消',
        nzOnOk: () => {
          this.searchProducts();
        }
      });
    } else {
      this.searchProducts();
    }
  }

  private searchProducts() {
    this.http.post<any[]>('/api/products/search', {
      type: this.type,
      currency: this.currency,
      isBonus: this.isBonus,
      minAge: this.ageRange[0],
      maxAge: this.ageRange[1]
    }).subscribe(res => {
      this.products = res;
      // 清空查詢條件
      this.type = null;
      this.currency = null;
      this.isBonus = null;
      this.ageRange = [0, 75];  // 或你想要的預設範圍
    });
  }
 

  logout() {
    this.http.post('/api/logout', {}).subscribe({
      next: () => {
        // 登出成功後導回登入頁面
        this.router.navigate(['/login']);
      },
      error: () => {
        // 登出失敗也導回登入頁面（或顯示錯誤訊息）
        this.router.navigate(['/login']);
      }
    });
  }

  viewProductDetail(id: number) {
    this.http.get<any>(`/api/products/${id}`).subscribe({
      next: res => {
        this.modal.info({
          nzTitle: `商品詳細資料 - ${res.name}`,
          nzContent: `
            <p><b>類型：</b> ${res.type}</p>
            <p><b>幣別：</b> ${res.currency}</p>
            <p><b>分紅：</b> ${res.isBonus ? '是' : '否'}</p>
            <p><b>年齡範圍：</b> ${res.minAge} - ${res.maxAge}</p>
            <p><b>繳費方式：</b> ${res.paymentTerm}</p>
            <p><b>特色：</b> ${res.description}</p>
          `,
          nzMaskClosable: true,
          nzOkText: '確認',   // 繁體中文
          // nzCancelText: '取消', // 繁體中文
          nzOnOk: () => console.log('詳細資料視窗關閉')
        });
      },
      error: err => {
        console.error('取得商品詳細資料失敗', err);
      }
    });
  }
}
