import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

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

      this.http.get<{ message: string }>('/api/verify', { params: { token } }).subscribe({
        next: res => {
          this.message = res.message || '驗證成功！';
          this.loading = false;
        },
        error: err => {
          this.message = err.error?.message || '驗證失敗，請確認連結是否正確';
          this.loading = false;
          this.error = true;
        }
      });
    });
  }

  goLogin() {
    this.router.navigate(['/login']);
  }
}
