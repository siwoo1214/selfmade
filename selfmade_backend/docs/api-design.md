# API 설계 문서

> 작성일: 2026-06-29
> 프로젝트: selfmade (식품 이커머스)

---

## 공통 사항

- **Base URL**: `/api`
- 인증이 필요한 API는 요청 헤더에 `Authorization: Bearer {accessToken}` 포함
- 공통 응답 포맷:

```json
{
  "success": true,
  "data": { },
  "message": "요청이 성공했습니다."
}
```

---

## Phase 1 — 인증 / 회원

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | `/api/auth/signup` | 회원가입 (CUSTOMER) | 누구나 |
| POST | `/api/auth/signup/seller` | 회원가입 (SELLER + COMPANY 정보) | 누구나 |
| POST | `/api/auth/login` | 로그인 | 누구나 |
| POST | `/api/auth/logout` | 로그아웃 | 로그인 유저 |
| POST | `/api/auth/reissue` | 토큰 재발급 | 로그인 유저 |
| GET | `/api/users/me` | 내 정보 조회 | 로그인 유저 |
| PATCH | `/api/users/me` | 내 정보 수정 | 로그인 유저 |
| DELETE | `/api/users/me` | 회원 탈퇴 | 로그인 유저 |

**POST `/api/auth/signup/seller` 요청 예시:**
```json
{
  "email": "seller@food.com",
  "password": "password123!",
  "name": "홍길동",
  "phone": "010-1234-5678",
  "companyName": "신선식품(주)",
  "businessNumber": "123-45-67890",
  "representative": "홍길동",
  "address": "서울시 강남구 테헤란로 123"
}
```

---

## Phase 2 — 판매자 승인

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/api/admin/companies` | 판매자 신청 목록 조회 | ADMIN |
| GET | `/api/admin/companies/{id}` | 판매자 신청 상세 조회 | ADMIN |
| PATCH | `/api/admin/companies/{id}/approve` | 판매자 승인 | ADMIN |
| PATCH | `/api/admin/companies/{id}/reject` | 판매자 거절 | ADMIN |
| GET | `/api/seller/company` | 내 회사 정보 조회 | SELLER |
| PATCH | `/api/seller/company` | 내 회사 정보 수정 | SELLER |

---

## Phase 3 — 카테고리 / 상품

### 카테고리

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/api/categories` | 카테고리 목록 조회 | 누구나 |
| POST | `/api/admin/categories` | 카테고리 등록 | ADMIN |
| PATCH | `/api/admin/categories/{id}` | 카테고리 수정 | ADMIN |
| DELETE | `/api/admin/categories/{id}` | 카테고리 삭제 | ADMIN |

### 상품

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/api/products` | 상품 목록 조회 (필터 / 검색) | 누구나 |
| GET | `/api/products/{id}` | 상품 상세 조회 | 누구나 |
| POST | `/api/seller/products` | 상품 등록 | SELLER |
| PATCH | `/api/seller/products/{id}` | 상품 수정 | SELLER |
| DELETE | `/api/seller/products/{id}` | 상품 삭제 | SELLER |
| GET | `/api/seller/products` | 내 상품 목록 조회 | SELLER |

**GET `/api/products` 쿼리 파라미터:**
```
/api/products?category=1&keyword=사과&page=0&size=20&sort=createdAt,desc
```

**POST `/api/seller/products` 요청 예시:**
```json
{
  "categoryId": 1,
  "name": "제주 감귤 3kg",
  "description": "제주도 직송 당일 수확 감귤입니다.",
  "price": 15000,
  "stock": 100
}
```

---

## Phase 4 — 장바구니

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/api/cart` | 장바구니 조회 | CUSTOMER |
| POST | `/api/cart/items` | 장바구니 상품 추가 | CUSTOMER |
| PATCH | `/api/cart/items/{itemId}` | 장바구니 수량 변경 | CUSTOMER |
| DELETE | `/api/cart/items/{itemId}` | 장바구니 상품 삭제 | CUSTOMER |
| DELETE | `/api/cart` | 장바구니 전체 비우기 | CUSTOMER |

**POST `/api/cart/items` 요청 예시:**
```json
{
  "productId": 5,
  "quantity": 2
}
```

**GET `/api/cart` 응답 예시:**
```json
{
  "success": true,
  "data": {
    "cartId": 1,
    "items": [
      {
        "cartItemId": 3,
        "productId": 5,
        "productName": "제주 감귤 3kg",
        "price": 15000,
        "quantity": 2,
        "subtotal": 30000
      }
    ],
    "totalAmount": 30000
  }
}
```

---

## Phase 5 — 주문

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | `/api/orders` | 주문 생성 (장바구니 → 주문) | CUSTOMER |
| GET | `/api/orders` | 내 주문 목록 조회 | CUSTOMER |
| GET | `/api/orders/{id}` | 내 주문 상세 조회 | CUSTOMER |
| PATCH | `/api/orders/{id}/cancel` | 주문 취소 | CUSTOMER |
| GET | `/api/seller/orders` | 내 상품 주문 목록 조회 | SELLER |
| PATCH | `/api/seller/orders/{id}/status` | 주문 상태 변경 | SELLER |

**POST `/api/orders` 요청 예시:**
```json
{
  "deliveryAddress": "서울시 마포구 월드컵북로 123",
  "cartItemIds": [3, 4]
}
```

**PATCH `/api/seller/orders/{id}/status` 요청 예시:**
```json
{
  "status": "SHIPPING"
}
```

---

## Phase 6 — 결제 (포트원 연동)

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | `/api/payments/prepare` | 결제 사전 등록 | CUSTOMER |
| POST | `/api/payments/verify` | 결제 검증 (포트원 → 백엔드) | CUSTOMER |
| POST | `/api/payments/{id}/cancel` | 결제 취소 | CUSTOMER |
| GET | `/api/payments/{id}` | 결제 내역 조회 | CUSTOMER |

### 결제 흐름

```
① 프론트 → 백엔드  POST /api/payments/prepare  (주문금액 사전 등록)
② 프론트 → 포트원  결제창 호출 (포트원 SDK)
③ 포트원 → 프론트  결제 완료 콜백 (portoneUid 전달)
④ 프론트 → 백엔드  POST /api/payments/verify  (portoneUid로 검증 요청)
⑤ 백엔드 → 포트원  금액 위변조 검증
⑥ 백엔드 → 프론트  최종 결제 성공 응답
```

**POST `/api/payments/prepare` 요청 예시:**
```json
{
  "orderId": 10
}
```

**POST `/api/payments/verify` 요청 예시:**
```json
{
  "orderId": 10,
  "portoneUid": "imp_1234567890"
}
```

---

## Phase 7 — 리뷰

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/api/products/{id}/reviews` | 상품 리뷰 목록 조회 | 누구나 |
| POST | `/api/reviews` | 리뷰 작성 | CUSTOMER |
| PATCH | `/api/reviews/{id}` | 리뷰 수정 | CUSTOMER |
| DELETE | `/api/reviews/{id}` | 리뷰 삭제 | CUSTOMER |
| GET | `/api/users/me/reviews` | 내 리뷰 목록 조회 | CUSTOMER |

**POST `/api/reviews` 요청 예시:**
```json
{
  "orderItemId": 7,
  "rating": 5,
  "content": "신선하고 맛있어요! 다음에도 구매할게요."
}
```

**GET `/api/products/{id}/reviews` 응답 예시:**
```json
{
  "success": true,
  "data": {
    "averageRating": 4.6,
    "totalCount": 128,
    "reviews": [
      {
        "reviewId": 15,
        "userName": "홍*동",
        "rating": 5,
        "content": "신선하고 맛있어요!",
        "createdAt": "2026-06-29"
      }
    ]
  }
}
```
