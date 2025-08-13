import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ApiResponse } from '../models/api-response';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit {
  message: string = '';
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (!token) {
        this.message = '驗證連結無效';
        this.loading = false;
        this.error = true;
        return;
      }

      // 使用後端新的泛型 APIResponse<Void> 路徑
      this.http.get<ApiResponse<void>>('/api/auth/verify', { params: { token } }).subscribe({
        next: res => {
          this.loading = false;
          if (res.success) {
            this.message = res.message || '驗證成功！';
            this.error = false;
          } else {
            this.message = res.message || '驗證失敗';
            this.error = true;
          }
        },
        error: err => {
          this.loading = false;
          this.error = true;
          this.message = err.error?.message || '驗證失敗，請確認連結是否正確';
        }
      });
    });
  }

  goLogin() {
    this.router.navigate(['/login']);
  }
}
