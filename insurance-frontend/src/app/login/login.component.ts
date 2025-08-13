import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ApiResponse } from '../models/api-response'; // 假設你有對應 TypeScript 的 ApiResponse 型別

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;
  error = '';
  verificationSent = false;  // 驗證信是否寄出提示

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) {}

  onSubmit() {
    this.loading = true;
    this.error = '';
    this.verificationSent = false;

    this.http.post<ApiResponse<string>>('/api/auth/login', {
      username: this.username,
      password: this.password
    }).subscribe({
      next: res => {
        this.loading = false;

        if (!res.success && res.message.includes('請先完成 Email 驗證')) {
          // 未驗證 Email
          this.message.success('已寄出驗證信，請至信箱點擊連結完成驗證');
          this.verificationSent = true;
          this.error = '';
        } else if (res.success) {
          // 登入成功
          this.router.navigate(['/products']);
        } else {
          // 其他失敗訊息
          this.error = res.message;
        }
      },
      error: err => {
        this.loading = false;
        // 非預期錯誤
        this.error = err.error?.message || '登入失敗，請稍後再試';
      }
    });
  }
}
