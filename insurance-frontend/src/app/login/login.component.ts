import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NzMessageService } from 'ng-zorro-antd/message';


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
  verificationSent = false;  // 新增：驗證信是否寄出提示

  constructor(
    private http: HttpClient,
     private router: Router,
     private message: NzMessageService  // 注入訊息服務
    ) {}

  onSubmit() {
    this.loading = true;
    this.error = '';
    this.verificationSent = false;  // 先確保初始狀態
    
    this.http.post<any>('/api/login', {
      username: this.username,
      password: this.password
    }).subscribe({
      next: res => {
        this.loading = false;
        this.router.navigate(['/products']);
          
        
      },
      error: err => {
        this.loading = false;

      if (err.status === 403 && err.error?.isNewUser) {
        this.message.success('已寄出驗證信，請至信箱點擊連結完成驗證');
        this.verificationSent = true;
        this.error = '';  // 不顯示錯誤訊息
      } else {
        this.error = err.error?.message || '登入失敗';
      }
      }
    });
  }
  

}
