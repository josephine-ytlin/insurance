# 商品查詢平台

## 專案簡介
- 使用者註冊、驗證、登入、登出
- 商品條件篩選查詢
- 商品細節查看


## 專案結構
- `insurance-backend/`：Spring Boot 3.5.4（Java 17）、MyBatis、Spring Security、Swagger
```
├── config/           // configuration
├── controller/       // APIs
├── dto/              // request/response DTO
├── entity/           // Domain model
├── exception/        // exception handler
├── mapper/           // DB mapping
└── service/          // domain logic
```


- `insurance-frontend/`：Angular 16 (TypeScript 5.1.3) 、ng-zorro-antd
```
├── login/             // login component
├── product-search/    // product component
└── verify/            // verify component
```
- `db/schema.sql`： users & insurance_product

## 環境需求
- Java 17 以上
- Node.js 16 以上
- PostgreSQL 14 以上
- Maven 3.8+（建議）

## 安裝與啟動

### 1. 資料庫
1. 啟動 PostgreSQL，建立 `insurance` 資料庫：
   ```bash
   createdb -U postgres insurance
   ```
2. 匯入 schema：
   ```bash
   psql -U postgres -d insurance -f db/schema.sql
   ```

### 2. 後端
1. 進入後端目錄並編譯、啟動：
   ```bash
   cd insurance-backend
   mvn clean package
   mvn spring-boot:run
   ```
2. Swagger API 文件： [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. 前端
1. 進入前端目錄並安裝、啟動：
   ```bash
   cd insurance-frontend
   npm install
   npm start
   ```
2. 前端預設網址：[http://localhost:4200](http://localhost:4200)

## APIs
- POST /api/auth/login ：使用者登入
- GET /api/auth/verify：使用者註冊驗證身份
- POST /api/auth/logout ：使用者登出
- GET /api/products： 取所有商品
- GET /api/products/{id}： 以產品id取得產品資訊
- POST /api/products/search： 依條件查詢商品
