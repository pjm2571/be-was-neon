# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

---

# 📖 주간 학습 계획
- ```docs/week1.md``` 에 위치합니다.

# 🖥️ 프로그램 동작

## 1) WebServer시작
- ```ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);```
  - ```POOL_SIZE = 100```
- 100개의 스레드를 가질 수 있는 스레드 풀을 생성한다.

## 2) Requset Handler 수행
- 클라이언트 소켓을 생성하여, 입력된 request에 대해 소켓을 연결한다.
- ```static``` 경로에서의 파일 조회만 성공한다.