markdown

# Mydays REST API 명세서

**문서 버전:** 1.0  
**최종 수정일:** 2025-09-03

---

## 1. 공통 가이드라인

### 1.1. Base URL

모든 API의 기본 URL은 다음과 같습니다.

[https://api.one-day-challenge.com/api/v1](https://api.one-day-challenge.com/)



### 1.2. API 버전
API 버전은 URL에 명시적으로 포함합니다. (`/`)

### 1.3. 응답 구조
모든 응답은 `meta`와 `body` 객체를 포함하는 표준 JSON 구조를 따릅니다.

- **`meta`**: API 호출의 성공 여부와 상태를 나타내는 코드 및 메시지를 포함합니다.
- **`body`**: 실제 요청에 대한 데이터 페이로드를 포함합니다. 데이터가 없는 경우 빈 객체(`{}`)를 반환합니다.

**표준 응답 구조 예시**
```json
{
    "meta": {
        "code": 200,
        "message": "요청에 성공했습니다."
    },
    "body": {
        // 실제 데이터가 여기에 위치합니다.
    }
}
```

### 1.4. 인증 (Authentication)

로그인이 필요한 모든 API는 요청 헤더에 JWT(JSON Web Token)를 포함해야 합니다.

```
Authorization: Bearer <JWT_TOKEN>
```

### 1.5. 공통 에러 코드

- **`400 Bad Request`**: 요청 값이 유효하지 않거나 필수 파라미터가 누락되었을 때
- **`401 Unauthorized`**: 인증되지 않은 사용자(유효하지 않은 토큰)일 때
- **`403 Forbidden`**: 해당 리소스에 접근할 권한이 없을 때 (예: 다른 사용자의 게시물 수정 시도)
- **`404 Not Found`**: 요청한 리소스를 찾을 수 없을 때
- **`409 Conflict`**: 리소스가 충돌할 때 (예: 중복된 이메일로 회원가입 시도)
- **`500 Internal Server Error`**: 서버 내부 로직 처리 중 에러가 발생했을 때

-----

## 2\. 인증 (Authentication)

### 2.1. 회원가입

- **Method:** `POST`
- **Endpoint:** `/auth/signup`
- **Description:** 새로운 사용자를 시스템에 등록합니다. 닉네임과 이메일은 중복될 수 없습니다.
- **Authentication:** Not Required
- **Request Body:**
  ```json
  {
      "nickname": "챌린지새싹",
      "email": "newbie@example.com",
      "password": "password123!"
  }
  ```
- **Success Response (201 Created):**
  ```json
  {
      "meta": {
          "code": 201,
          "message": "회원가입이 성공적으로 완료되었습니다."
      },
      "body": {
          "user_id": 1,
          "nickname": "챌린지새싹",
          "email": "newbie@example.com"
      }
  }
  ```

### 2.2. 로그인

- **Method:** `POST`
- **Endpoint:** `/auth/login`
- **Description:** 이메일과 비밀번호로 사용자를 인증하고, 성공 시 JWT 토큰을 발급합니다.
- **Authentication:** Not Required
- **Request Body:**
  ```json
  {
      "email": "user@example.com",
      "password": "password123!"
  }
  ```
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "로그인에 성공했습니다."
      },
      "body": {
          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
      }
  }
  ```

### 2.3. 로그아웃

- **Method:** `POST`
- **Endpoint:** `/auth/logout`
- **Description:** 사용자를 로그아웃 처리합니다. (서버 측에서 토큰을 무효화하는 로직이 필요할 수 있습니다.)
- **Authentication:** Required
- **Request Body:** (없음)
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "로그아웃 되었습니다."
      },
      "body": {}
  }
  ```

-----

## 3\. 홈 (Home)

### 3.1. 오늘의 챌린지 조회

- **Method:** `GET`
- **Endpoint:** `/challenges/today`
- **Description:** 오늘 날짜에 해당하는 챌린지 정보를 조회합니다.
- **Authentication:** Required
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "오늘의 챌린지 조회에 성공했습니다."
      },
      "body": {
          "challenge_id": 1,
          "challenge_date": "2025-09-03",
          "content": "오늘 하루, 가장 감사했던 일 한 가지를 사진으로 남겨보세요."
      }
  }
  ```

### 3.2. 피드(게시물 목록) 조회

- **Method:** `GET`
- **Endpoint:** `/posts`
- **Description:** 모든 사용자의 게시물 목록을 최신순으로 조회합니다. 무한 스크롤을 위한 페이지네이션을 지원합니다.
- **Authentication:** Required
- **Query Parameters:**
    - `page` (number, optional, default: 0): 조회할 페이지 번호
    - `size` (number, optional, default: 20): 한 페이지에 포함될 게시물 수
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "피드 조회에 성공했습니다."
      },
      "body": {
          "posts": [
              {
                  "post_id": 101,
                  "content": "점심시간에 본 예쁜 하늘, 감사합니다!",
                  "image_url": "https://.../image1.jpg",
                  "created_at": "2025-09-03T14:20:10Z",
                  "author": {
                      "user_id": 5,
                      "nickname": "하늘바라기",
                      "avatar_image_url": "https://.../avatar5.jpg",
                      "active_title": {
                          "title_id": 3,
                          "title_name": "꾸준함의 아이콘"
                      }
                  },
                  "like_count": 15,
                  "comment_count": 4,
                  "isLiked": true
              },
              {
                  "post_id": 100,
                  "content": "친구가 선물해준 커피 덕분에 힘이 났어요.",
                  "image_url": "https://.../image2.jpg",
                  "created_at": "2025-09-03T12:05:30Z",
                  "author": {
                      "user_id": 8,
                      "nickname": "커피조아",
                      "avatar_image_url": "https://.../avatar8.jpg",
                      "active_title": null
                  },
                  "like_count": 8,
                  "comment_count": 2,
                  "isLiked": false
              }
          ],
          "page": 0,
          "size": 20,
          "total_elements": 152,
          "total_pages": 8
      }
  }
  ```

-----

## 4\. 게시물 (Posts)

### 4.1. 게시물 생성

- **Method:** `POST`
- **Endpoint:** `/posts`
- **Description:** 오늘의 챌린지에 대한 게시물을 작성합니다. 성공 시 `Posts` 및 `User_Challenges` 테이블에 데이터가 생성됩니다.
- **Authentication:** Required
- **Request Body:** (`multipart/form-data` 권장)
    - `content` (string)
    - `image` (file)
- **Success Response (201 Created):**
  ```json
  {
      "meta": {
          "code": 201,
          "message": "게시물이 성공적으로 작성되었습니다."
      },
      "body": {
          "post_id": 102
      }
  }
  ```

### 4.2. 게시물 상세 조회

- **Method:** `GET`
- **Endpoint:** `/posts/{post_id}`
- **Description:** 특정 게시물의 상세 정보와 댓글 목록을 함께 조회합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): 조회할 게시물의 ID
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "게시물 상세 조회에 성공했습니다."
      },
      "body": {
          "post_id": 101,
          "content": "점심시간에 본 예쁜 하늘, 감사합니다!",
          "image_url": "https://.../image1.jpg",
          "created_at": "2025-09-03T14:20:10Z",
          "author": {
              "user_id": 5,
              "nickname": "하늘바라기",
              "avatar_image_url": "https://.../avatar5.jpg",
              "active_title": {
                  "title_id": 3,
                  "title_name": "꾸준함의 아이콘"
              }
          },
          "like_count": 15,
          "isLiked": true,
          "comments": [
              {
                  "comment_id": 205,
                  "content": "사진 정말 예쁘네요!",
                  "author": {
                      "user_id": 8,
                      "nickname": "커피조아",
                      "avatar_image_url": "https://.../avatar8.jpg"
                  },
                  "created_at": "2025-09-03T15:01:00Z"
              }
          ]
      }
  }
  ```

### 4.3. 게시물 삭제

- **Method:** `DELETE`
- **Endpoint:** `/posts/{post_id}`
- **Description:** 본인이 작성한 게시물을 삭제합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): 삭제할 게시물의 ID
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "게시물이 삭제되었습니다."
      },
      "body": {}
  }
  ```

### 4.4. 게시물 좋아요

- **Method:** `POST`
- **Endpoint:** `/posts/{post_id}/like`
- **Description:** 특정 게시물에 '좋아요'를 누릅니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): '좋아요'할 게시물의 ID
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "게시물을 좋아합니다."
      },
      "body": {}
  }
  ```

### 4.5. 게시물 좋아요 취소

- **Method:** `DELETE`
- **Endpoint:** `/posts/{post_id}/like`
- **Description:** 특정 게시물에 눌렀던 '좋아요'를 취소합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): '좋아요'를 취소할 게시물의 ID
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "게시물 좋아요를 취소했습니다."
      },
      "body": {}
  }
  ```

-----

## 5\. 댓글 (Comments)

### 5.1. 댓글 생성

- **Method:** `POST`
- **Endpoint:** `/posts/{post_id}/comments`
- **Description:** 특정 게시물에 댓글을 작성합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): 댓글을 작성할 게시물의 ID
- **Request Body:**
  ```json
  {
      "content": "저도 오늘 하늘 봤는데 정말 예뻤어요!"
  }
  ```
- **Success Response (201 Created):**
  ```json
  {
      "meta": {
          "code": 201,
          "message": "댓글이 작성되었습니다."
      },
      "body": {
          "comment_id": 206,
          "content": "저도 오늘 하늘 봤는데 정말 예뻤어요!"
      }
  }
  ```

### 5.2. 댓글 목록 조회

- **Method:** `GET`
- **Endpoint:** `/posts/{post_id}/comments`
- **Description:** 특정 게시물의 모든 댓글을 페이지네이션으로 조회합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `post_id` (number, required): 댓글 목록을 조회할 게시물의 ID
- **Query Parameters:**
    - `page` (number, optional, default: 0): 조회할 페이지 번호
    - `size` (number, optional, default: 20): 한 페이지에 포함될 댓글 수
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "댓글 목록 조회에 성공했습니다."
      },
      "body": {
          "comments": [
              {
                  "comment_id": 206,
                  "content": "저도 오늘 하늘 봤는데 정말 예뻤어요!",
                  "author": {
                      "user_id": 12,
                      "nickname": "감성폭발",
                      "avatar_image_url": "https://.../avatar12.jpg"
                  },
                  "created_at": "2025-09-03T15:10:20Z"
              }
          ],
          "page": 0,
          "size": 20
      }
  }
  ```

### 5.3. 댓글 수정

- **Method:** `PUT`
- **Endpoint:** `/comments/{comment_id}`
- **Description:** 본인이 작성한 댓글을 수정합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `comment_id` (number, required): 수정할 댓글의 ID
- **Request Body:**
  ```json
  {
      "content": "사진과 글 모두 마음에 와닿네요!"
  }
  ```
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "댓글이 수정되었습니다."
      },
      "body": {}
  }
  ```

### 5.4. 댓글 삭제

- **Method:** `DELETE`
- **Endpoint:** `/comments/{comment_id}`
- **Description:** 본인이 작성한 댓글을 삭제합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `comment_id` (number, required): 삭제할 댓글의 ID
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "댓글이 삭제되었습니다."
      },
      "body": {}
  }
  ```

-----

## 6\. 마이페이지 (My Page)

`/me` 경로는 현재 인증된 사용자를 지칭합니다.

### 6.1. 나의 챌린지 현황 조회

- **Method:** `GET`
- **Endpoint:** `/me/status`
- **Description:** 나의 프로필 정보, 챌린지 기록, 현재 캐릭터 정보를 조회합니다.
- **Authentication:** Required
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "나의 챌린지 현황 조회에 성공했습니다."
      },
      "body": {
          "nickname": "하늘바라기",
          "avatar_image_url": "https://.../avatar5.jpg",
          "consecutive_days": 15,
          "total_completed_days": 48,
          "active_title": {
              "title_id": 3,
              "title_name": "꾸준함의 아이콘"
          },
          "character": {
              "character_id": 2,
              "name": "성장하는 새싹",
              "level": 2,
              "image_url": "https://.../character_level2.png"
          }
      }
  }
  ```

### 6.2. 나의 챌린지 달력 조회

- **Method:** `GET`
- **Endpoint:** `/me/calendar`
- **Description:** 특정 연도와 월을 기준으로 챌린지를 완료한 날짜와 해당 게시물의 이미지 목록을 조회합니다.
- **Authentication:** Required
- **Query Parameters:**
    - `year` (number, required): 조회할 연도 (예: 2025)
    - `month` (number, required): 조회할 월 (예: 9)
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "나의 챌린지 달력 조회에 성공했습니다."
      },
      "body": {
          "year": 2025,
          "month": 9,
          "completed_posts": [
              {
                  "completed_at": "2025-09-01",
                  "image_url": "https://.../my_post_0901.jpg"
              },
              {
                  "completed_at": "2025-09-02",
                  "image_url": "https://.../my_post_0902.jpg"
              },
              {
                  "completed_at": "2025-09-03",
                  "image_url": "https://.../image1.jpg"
              }
          ]
      }
  }
  ```

### 6.3. 특정 날짜의 내 게시물 조회

- **Method:** `GET`
- **Endpoint:** `/me/posts/{date}`
- **Description:** 특정 날짜(`YYYY-MM-DD`)에 내가 작성한 게시물 정보를 조회합니다.
- **Authentication:** Required
- **Path Parameters:**
    - `date` (string, required): 'YYYY-MM-DD' 형식의 날짜 (예: '2025-09-03')
- **Success Response (200 OK):**
  게시물 상세 조회 API(`GET /posts/{post_id}`)의 응답 body와 동일한 구조를 가집니다.

### 6.4. 보유 칭호 목록 조회

- **Method:** `GET`
- **Endpoint:** `/me/titles`
- **Description:** 내가 획득한 모든 칭호의 목록을 조회합니다.
- **Authentication:** Required
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "보유 칭호 목록 조회에 성공했습니다."
      },
      "body": {
          "titles": [
              {
                  "title_id": 1,
                  "title_name": "첫 걸음",
                  "title_description": "첫 챌린지를 완료한 당신을 응원해요!",
                  "earned_at": "2025-07-15T10:00:00Z"
              },
              {
                  "title_id": 3,
                  "title_name": "꾸준함의 아이콘",
                  "title_description": "연속 15일 챌린지를 달성했습니다.",
                  "earned_at": "2025-08-30T22:15:00Z"
              }
          ]
      }
  }
  ```

### 6.5. 대표 칭호 변경

- **Method:** `PUT`
- **Endpoint:** `/me/active-title`
- **Description:** 보유한 칭호 중에서 프로필에 표시될 대표 칭호를 변경합니다.
- **Authentication:** Required
- **Request Body:**
  ```json
  {
      "title_id": 3
  }
  ```
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "대표 칭호가 변경되었습니다."
      },
      "body": {}
  }
  ```

-----

## 7\. 설정 (Settings)

> DDL에 별도의 설정 테이블이 정의되지 않았으나, 일반적인 기능 요구사항을 고려하여 API를 설계합니다. 실제 구현 시 `Users` 테이블 확장 또는 별도 테이블 생성이
> 필요합니다.

### 7.1. 알림 설정 변경

- **Method:** `PUT`
- **Endpoint:** `/me/settings/notifications`
- **Description:** 푸시 알림 수신 여부 등을 변경합니다.
- **Authentication:** Required
- **Request Body:**
  ```json
  {
      "likes_notification": true,
      "comments_notification": true,
      "challenge_reminder_notification": false
  }
  ```
- **Success Response (200 OK):**
  ```json
  {
      "meta": {
          "code": 200,
          "message": "알림 설정이 변경되었습니다."
      },
      "body": {
          "likes_notification": true,
          "comments_notification": true,
          "challenge_reminder_notification": false
      }
  }
  ```

<!-- end list -->

```
```