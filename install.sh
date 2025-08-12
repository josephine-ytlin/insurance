#!/bin/bash
set -e

# 1. 資料庫初始化
if [ -f "db/schema.sql" ]; then
  echo "請手動確認 PostgreSQL 已啟動，並建立名為 insurance 的資料庫。"
  echo "然後執行： psql -U postgres -d insurance -f db/schema.sql"
else
  echo "找不到 db/schema.sql，請確認路徑。"
fi

# 2. 後端安裝與啟動
cd insurance-backend/insurance-backend
./mvnw clean package
./mvnw spring-boot:run &
cd ../../

# 3. 前端安裝與啟動
cd insurance-frontend
npm install
npm start &
cd ..

echo "---\n啟動完成！\n後端: http://localhost:8080\n前端: http://localhost:4200\nSwagger: http://localhost:8080/swagger-ui.html\n---"