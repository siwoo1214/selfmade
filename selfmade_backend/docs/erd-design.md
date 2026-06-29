# ERD 설계 문서

> 작성일: 2026-06-29
> 프로젝트: selfmade (식품 이커머스)

---

## 프로젝트 개요

- **주제**: 식품 전문 이커머스 플랫폼
- **기술 스택**: Spring Boot (백엔드) + React (프론트엔드)
- **PG 연동**: 포트원(PortOne)
- **사용자 역할**: CUSTOMER / SELLER(기업) / ADMIN

---

## 도메인 목록

| 도메인 | 설명 |
|--------|------|
| USER | 회원 (CUSTOMER / SELLER / ADMIN) |
| COMPANY | 판매자 기업 정보 |
| CATEGORY | 식품 카테고리 |
| PRODUCT | 상품 |
| CART | 장바구니 |
| CART_ITEM | 장바구니 상세 항목 |
| ORDER | 주문 |
| ORDER_ITEM | 주문 상세 항목 |
| PAYMENT | 결제 (포트원 연동) |
| REVIEW | 리뷰 |

---

## 테이블 정의

### USER
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| email | varchar | 이메일 (unique) |
| password | varchar | 비밀번호 (BCrypt) |
| name | varchar | 이름 |
| phone | varchar | 전화번호 |
| role | enum | CUSTOMER / SELLER / ADMIN |
| created_at | timestamp | 가입일 |

### COMPANY
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| user_id | bigint (FK) | USER 참조 (1:1) |
| name | varchar | 회사명 |
| business_number | varchar | 사업자번호 (unique) |
| representative | varchar | 대표자명 |
| address | varchar | 사업장 주소 |
| status | enum | PENDING / APPROVED / REJECTED |
| created_at | timestamp | 신청일 |

> SELLER 역할의 유저가 직접 가입 신청 → ADMIN이 승인(APPROVED) 처리

### CATEGORY
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| name | varchar | 카테고리명 (예: 채소, 과일, 육류) |
| description | varchar | 설명 |

### PRODUCT
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| company_id | bigint (FK) | COMPANY 참조 |
| category_id | bigint (FK) | CATEGORY 참조 |
| name | varchar | 상품명 |
| description | text | 상품 설명 |
| price | int | 가격 |
| stock | int | 재고 수량 |
| status | enum | ON_SALE / SOLD_OUT / HIDDEN |
| created_at | timestamp | 등록일 |

### CART
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| user_id | bigint (FK) | USER 참조 (1:1) |
| created_at | timestamp | 생성일 |

> 회원가입 시 자동 생성, 유저에 귀속

### CART_ITEM
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| cart_id | bigint (FK) | CART 참조 |
| product_id | bigint (FK) | PRODUCT 참조 |
| quantity | int | 수량 |

### ORDER
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| user_id | bigint (FK) | USER 참조 |
| delivery_address | varchar | 배송지 |
| status | enum | PENDING / PAID / SHIPPING / DELIVERED / CANCELLED |
| total_amount | int | 총 결제금액 |
| ordered_at | timestamp | 주문일 |

### ORDER_ITEM
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| order_id | bigint (FK) | ORDER 참조 |
| product_id | bigint (FK) | PRODUCT 참조 |
| quantity | int | 수량 |
| price | int | **주문 시점 가격 스냅샷** |

> price를 별도 저장하는 이유: 나중에 상품 가격이 변경되어도 주문 내역은 당시 가격 유지

### PAYMENT
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| order_id | bigint (FK) | ORDER 참조 (1:1) |
| portone_uid | varchar | 포트원 결제 고유번호 |
| status | enum | READY / PAID / CANCELLED / FAILED |
| amount | int | 결제금액 |
| paid_at | timestamp | 결제일시 |

> portone_uid: 포트원 API 결제 검증 시 사용

### REVIEW
| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | bigint (PK) | 기본키 |
| user_id | bigint (FK) | USER 참조 |
| product_id | bigint (FK) | PRODUCT 참조 |
| order_item_id | bigint (FK) | ORDER_ITEM 참조 |
| rating | int | 평점 (1~5) |
| content | text | 리뷰 내용 |
| created_at | timestamp | 작성일 |

> order_item_id FK: 실제 구매한 사람만 리뷰 작성 가능하도록 검증

---

## 테이블 관계 요약

```
USER ──────────── COMPANY       (1:1, SELLER 유저만)
USER ──────────── CART          (1:1, 회원가입 시 자동 생성)
USER ──────────── ORDER         (1:N)
USER ──────────── REVIEW        (1:N)
COMPANY ──────── PRODUCT        (1:N)
CATEGORY ─────── PRODUCT        (1:N)
CART ─────────── CART_ITEM      (1:N)
PRODUCT ──────── CART_ITEM      (1:N)
ORDER ────────── ORDER_ITEM     (1:N)
ORDER ────────── PAYMENT        (1:1)
PRODUCT ──────── ORDER_ITEM     (1:N)
PRODUCT ──────── REVIEW         (1:N)
ORDER_ITEM ───── REVIEW         (1:0..1)
```

---

## 설계 결정 사항

1. **판매자 가입 방식**: 기업이 직접 신청 → ADMIN 승인 방식 (`COMPANY.status` 로 관리)
2. **상품-판매자 관계**: 하나의 상품은 하나의 판매사만 판매 (1:N, 역방향)
3. **리뷰 작성 조건**: 구매 이력(`ORDER_ITEM`) 확인 후 작성 허용
4. **가격 스냅샷**: `ORDER_ITEM.price` 에 주문 시점 가격 저장
5. **결제**: 포트원(PortOne) 연동, `PAYMENT.portone_uid` 로 검증
