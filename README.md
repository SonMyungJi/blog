# blog : 스프링 부트를 활용한 블로그 백엔드 서버 만들기

## 프로젝트 목적

### 1. 자바를 활용하여 필요한 클래스 구상하기
#### entity, dto, controller, service, repository

### 2. Lombok과 JPA를 이용해 데이터베이스 만들고 활용하기
#### 영속성 컨텍스트에 Entity 객체들을 저장
#### 트랜잭션 적용

### 3. CRUD를 포함한 REST API 만들기
#### 1. Create : 게시글 저장
#### 2. Update : 게시글 수정
#### 3. Delete : 게시글 삭제


## 1. 수정, 삭제 API의 request 방식 : RequestBody를 이용하여 여러 데이터를 JSON으로 받음
## 2. 어떤 상황에 어떤 방식의 request를 써야 하는가 : 간단히 데이터를 받을 때에는 PathVariable이나 Param을 써도 되나, URL에 직접적으로 정보를 받는 것이 지양되고 데이터의 양이 많을 때에는 Body로 request를 함
## 3. RESTful한 API를 설계하였는가....
## 4. 적절한 관심사 분리
## 5. API 명세서 작성
