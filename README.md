# 保險商品查詢平台(實作練習)

## 專案簡介
本平台提供使用者註冊、驗證登入功能、保險商品查詢、條件篩選功能。


## 專案結構
- `insurance-backend/`：Spring Boot 3.5.4（Java 17）、MyBatis、Spring Security、Swagger
- `insurance-frontend/`：Angular 16 (TypeScript 5.1.3) 、ng-zorro-antd
- `db/schema.sql`：資料庫 schema

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

## 主要功能
- 使用者登入（/api/login，Session 認證）
- 使用者註冊帳號驗證（/api/verify，token信箱認證）
- 依條件查詢保險商品（/api/products, /api/products/search, /api/products/{id}）
- Swagger API 文件自動產生
- ng-zorro-antd UI元件
